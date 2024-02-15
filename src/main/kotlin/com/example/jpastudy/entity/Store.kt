package com.example.jpastudy.entity

import jakarta.persistence.*

@Entity
@SequenceGenerator(name = "STORE_SEQ_GENERATOR", sequenceName = "STORE_SEQ", initialValue = 1, allocationSize = 1)
data class Store(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STORE_SEQ_GENERATOR")
    val id: String,
    val name: String,
    @OneToMany
    @JoinColumn
    val guitars: MutableList<Guitar>,
) {
}