package com.example.jpastudy

import com.example.jpastudy.entity.Guitar
import com.example.jpastudy.entity.dto.PostGuitar
import com.example.jpastudy.repository.GuitarRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import jakarta.persistence.EntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class EntityEqualityTest(
    guitarRepository: GuitarRepository,
    guitarService: GuitarService,
    entityManager: EntityManager
) :  BehaviorSpec({
    fun extensions() = listOf(SpringExtension)
    lateinit var guitar: Guitar

    beforeTest {
        val postGuitar = PostGuitar(guitarName = "Martin OM-35", serialNumber = "182ufjdh2736sxxw", company = "Martin & CO", isSale = true)
        guitar = Guitar(postGuitar)
    }

    given("영속성 컨텍스트가 같은 경우 엔티티 동등성(Equality) 비교") {
        `when`("기타를 스토어에 등록한다.") {
            val savedId = guitarService.registerInStore(guitar)
            val savedGuitar = guitarRepository.findById(savedId).get()

            then("guitar와 savedGuitar는 주소값이 같은 엔티티여야 한다.") {
                guitar === savedGuitar
            }
        }
    }

    given("영속성 컨텍스트가 다른 경우 엔티티 동등성 비교") {
        `when`("기타를 스토어에 등록한다.") {
            val savedId = guitarService.registerInStore(guitar)
            val savedGuitar = guitarRepository.findById(savedId).get()

            then("savedGuitar와 anotherGuitar는 달라야한다.") {

                (guitar === savedGuitar) shouldBe false
            }
        }
    }


})