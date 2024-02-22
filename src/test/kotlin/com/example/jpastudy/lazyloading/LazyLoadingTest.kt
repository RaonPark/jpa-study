package com.example.jpastudy.lazyloading

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import jakarta.persistence.*
import org.hibernate.LazyInitializationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.client.RestTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
class LazyLoadingTest @Autowired constructor(
    val lazyLoadingGuitarRepository: LazyLoadingGuitarRepository,
    val companyRepository: CompanyRepository,
    val entityManager: EntityManager,
    val entityChecker: EntityChecker,
    val restTemplate: RestTemplate,
): BehaviorSpec({
    lateinit var company: Company
    lateinit var lazyLoadingGuitar: LazyLoadingGuitar
    var id = 1L


    beforeEach {

    }

    given("영속 상태의 엔티티를 find()한 후") {
        company = Company.createCompany("Lakewood")
        companyRepository.saveCompany(company)
        lazyLoadingGuitar = LazyLoadingGuitar.createGuitar(company, "Lakewood M-32CP")
        id = lazyLoadingGuitarRepository.saveGuitar(lazyLoadingGuitar)
        `when`("그 엔티티에서 다른 lazy-loading 엔티티를 접근하는 경우") {
            val contextGuitar = lazyLoadingGuitarRepository.getGuitar(id)
            println("${entityChecker.getManagedEntities()}")
            then("해당 엔티티는 준영속상태이므로 하이버네이트를 사용하는 경우 LazyInitializationException이 발생한다.") {
                val exception = shouldThrow<LazyInitializationException> {
                    val httpHeader = HttpHeaders()
                    httpHeader.contentType = MediaType.APPLICATION_JSON
                    val httpEntity = HttpEntity<LazyLoadingGuitar>(contextGuitar, httpHeader)
                    println(httpEntity.toString())
                    val response = restTemplate.exchange("http://localhost:8080/companyName", HttpMethod.GET, httpEntity, String::class.java)
                    println(response.body)
                }
            }
        }
    }

}) {

}

@Entity
data class LazyLoadingGuitar(
    @Id
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    val company: Company,

    val guitarName: String
) {
    companion object {
        fun createGuitar(company: Company, guitarName: String): LazyLoadingGuitar {
            return LazyLoadingGuitar(1L, company, guitarName)
        }
    }
}

@Entity
data class Company(
    @Id
    val id: Long,

    val companyName: String
) {
    companion object {
        fun createCompany(companyName: String): Company {
            return Company(2L, companyName)
        }
    }
}

@Repository
class LazyLoadingGuitarRepository {
    @PersistenceContext
    lateinit var em: EntityManager

    fun getGuitar(id: Long): LazyLoadingGuitar {
        return em.find(LazyLoadingGuitar::class.java, id)
    }

    fun saveGuitar(lazyLoadingGuitar: LazyLoadingGuitar): Long {
        em.persist(lazyLoadingGuitar)
        em.flush()
        return lazyLoadingGuitar.id
    }
}

@Repository
class CompanyRepository {
    @PersistenceContext
    lateinit var em: EntityManager

    fun saveCompany(company: Company) {
        em.persist(company)
        em.flush()
    }
}

@Service
class EntityChecker {
    @PersistenceContext
    lateinit var em: EntityManager

    fun getManagedEntities(): Set<Class<*>> {
        return em.metamodel.entities.map { it.javaType }.toSet()
    }

    fun clearContext() {
        em.flush()
        em.clear()
    }
}