package com.example.jpastudy.entity

import com.example.jpastudy.converter.BooleanToSaleOrNotConverter
import com.example.jpastudy.entity.dto.PostGuitar
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator

@Entity
@SequenceGenerator(name = "GUITAR_SEQ_GENERATOR", sequenceName = "GUITAR_SEQ", initialValue = 1, allocationSize = 1)
data class Guitar(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUITAR_SEQ_GENERATOR")
    val id: Long = 1L,
    var guitarName: String,
    var serialNumber: String,
    var company: String,

    @Convert(converter = BooleanToSaleOrNotConverter::class)
    val isSale: Boolean
) {
    constructor(postGuitar: PostGuitar) : this(
        guitarName = postGuitar.guitarName,
        serialNumber = postGuitar.serialNumber,
        company = postGuitar.company,
        isSale = postGuitar.isSale
    )
}