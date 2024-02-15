package com.example.jpastudy

import com.example.jpastudy.entity.Guitar
import com.example.jpastudy.repository.GuitarRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GuitarService(
    private val guitarRepository: GuitarRepository
) {
    fun registerInStore(guitar: Guitar): Long {
        return guitarRepository.save(guitar).id
    }

    fun getGuitar(id: Long): Guitar {
        return guitarRepository.findById(id).get()
    }
}