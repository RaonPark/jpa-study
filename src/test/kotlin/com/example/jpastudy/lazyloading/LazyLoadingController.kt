package com.example.jpastudy.lazyloading

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LazyLoadingController {
    @GetMapping("/companyName")
    fun getGuitarCompanyName(guitar: LazyLoadingGuitar): String {
        return guitar.company.companyName
    }
}