package com.example.jpastudy.corountine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.coroutines.*

fun now() = ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.MILLIS)

fun log(msg: String) = println("${now()}:${Thread.currentThread()}: ${msg}")

fun launchInGlobalScope() {
    GlobalScope.launch {
        log("coroutine started.")
    }
}

fun runBlockingExample() {
    runBlocking {
        launch {
            log("GlobalScope.launch started.")
        }
    }
}

fun yieldExample() {
    runBlocking {
        launch {
            log("1")
            yield()
            log("3")
            yield()
            log("5")
        }
        log("after first launch")
        launch {
            log("2")
            delay(100L)
            log("4")
            delay(1000L)
            log("6")
        }
    }
}

suspend fun yieldThreeTimes() {
    log("1")
    delay(1000L)
    yield()
    log("2")
    delay(1000L)
    yield()
    log("3")
    delay(1000L)
    yield()
}

interface Generator<out R, in T> {
    fun next(param: T): R?
}

@RestrictsSuspension
interface GeneratorBuilder<in T, R> {
    suspend fun yield(value: T): R // generator는 yield를 사용할 수 있어야 한다.
    suspend fun yieldAll(generator: Generator<T, R>, param: R)
}

fun <T, R> generate(block: suspend GeneratorBuilder<T, R>.(R) -> Unit):
        Generator<T, R> {
    val coroutine = GeneratorCoroutine<T, R>()
    val initial: suspend (R) -> Unit = { result -> block(coroutine, result) }
    coroutine.nextStep = { param -> initial.startCoroutine(param, coroutine) }
    return coroutine
}

internal class GeneratorCoroutine<T, R>: Generator<T, R>,GeneratorBuilder<T, R>, Continuation<Unit> {
    lateinit var nextStep: (R) -> Unit
    private var lastValue: T? = null
    private var lastException: Throwable? = null

    // Generator 구현
    override fun next(param: R): T? {
        nextStep(param)
        lastException?.let { throw it }
        return lastValue
    }

// GeneratorBuilder 구현
    override suspend fun yield(value: T): R = kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn {
        cont ->
            lastValue = value
        nextStep = { param -> cont.resume(param) }
        kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
    }

    override suspend fun yieldAll(generator: Generator<T, R>, param: R): Unit = kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn sc@ {
        cont ->
            lastValue = generator.next(param)
            if(lastValue == null) return@sc Unit
            nextStep = {
                param ->
                    lastValue = generator.next(param)
                    if(lastValue == null) cont.resume(Unit)
            }
            kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
    }
    override val context: CoroutineContext get() = EmptyCoroutineContext

    override fun resumeWith(result: Result<Unit>) {
        result.onSuccess { lastValue = null }
            .onFailure { lastException = it }
    }
}

fun idMaker() = generate<Int, Unit> {
    var index = 0
    while(index < 3)
        yield(index++)
}

class CoroutineTest {
    @Test
    @DelicateCoroutinesApi
    fun coroutineTest() {
        log("test() started.")
//        launchInGlobalScope()
//        runBlockingExample()
//        yieldExample()
//        GlobalScope.launch { yieldThreeTimes() }
        val gen = idMaker()
        println(gen.next(Unit)) // 0
        println(gen.next(Unit)) // 1
        println(gen.next(Unit)) // 2
        println(gen.next(Unit)) // null
        log("launchInGlobalScope() executed")
//        Thread.sleep(5000)
        log("test terminated.")
    }
}