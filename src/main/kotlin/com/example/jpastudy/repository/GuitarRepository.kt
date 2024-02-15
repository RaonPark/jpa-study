package com.example.jpastudy.repository

import com.example.jpastudy.entity.Guitar
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface GuitarRepository: JpaRepository<Guitar, Long> {
    fun getByGuitarName(guitarName: String): Guitar

}