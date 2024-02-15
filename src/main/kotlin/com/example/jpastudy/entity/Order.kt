package com.example.jpastudy.entity

import jakarta.persistence.*

@Entity
@Table(name = "ORDERS")
@SequenceGenerator(name = "ORDERS_SEQ_GENERATOR", sequenceName = "ORDERS_SEQ", initialValue = 1, allocationSize = 1)
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERS_SEQ_GENERATOR")
    val id: String,
    val address: String,
    @ManyToOne
    @JoinColumn
    val member: Member
) {
}