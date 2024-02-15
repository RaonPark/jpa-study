package com.example.jpastudy

import com.example.jpastudy.entity.Guitar
import com.example.jpastudy.entity.dto.PostGuitar
import com.example.jpastudy.repository.GuitarRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController @Autowired constructor(
    private val guitarRepository: GuitarRepository
) {
    @PostMapping("/guitar")
    fun saveGuitar(@RequestBody postGuitar: PostGuitar): Long {
        val guitar = Guitar(postGuitar)
        return guitarRepository.save(guitar).id
    }

    @GetMapping("/guitar")
    fun getGuitar(@RequestParam guitarName: String): Guitar {
        return guitarRepository.getByGuitarName(guitarName)
    }
}