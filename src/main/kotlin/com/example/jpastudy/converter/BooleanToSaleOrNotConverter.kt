package com.example.jpastudy.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class BooleanToSaleOrNotConverter: AttributeConverter<Boolean, String> {
    override fun convertToDatabaseColumn(attribute: Boolean?): String {
        return if(attribute != null && attribute) "SALE" else "NOT FOR SALE"
    }

    override fun convertToEntityAttribute(dbData: String?): Boolean {
        return "SALE" == dbData
    }
}
