package com.example.jpastudy

import com.example.jpastudy.entity.Guitar
import com.example.jpastudy.entity.dto.PostGuitar
import com.example.jpastudy.repository.GuitarRepository
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import jakarta.persistence.EntityManager
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ProxyEntityEqualityTest(
    entityManager: EntityManager,
    guitarRepository: GuitarRepository,
    guitarService: GuitarService
) : BehaviorSpec({

    given("프록시와 실제 엔티티의 동등성 비교") {
        `when`("프록시 객체를 먼저 가져오고 실제 엔티티 객체를 나중에 가져온다.") {
            val postGuitar = PostGuitar(guitarName = "Lakewood M32CP", serialNumber = "1i2jfjd8823723s", company = "Lakewood", isSale = true)
            val anotherGuitar = Guitar(postGuitar)
            val savedId = guitarRepository.save(anotherGuitar).id
            entityManager.clear()
            val proxy = guitarRepository.getReferenceById(savedId)
//            val real = entityManager.find(Guitar::class.java, 1L)

            then("proxy와 real은 같아야한다.") {
                println("proxy Type = ${proxy::class}")
//                println("real Type = ${real::class}")
//                (real == proxy) shouldBe true
            }
        }
    }
}) {
    override fun extensions() = listOf(SpringExtension)
}