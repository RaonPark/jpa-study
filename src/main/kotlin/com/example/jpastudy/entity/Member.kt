package com.example.jpastudy.entity

import jakarta.persistence.*

@Entity
@SequenceGenerator(name = "MEMBER_SEQ_GENERATOR", sequenceName = "MEMBER_SEQ", initialValue = 1, allocationSize = 1)
data class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    val id: String,
    val username: String,
) {

}