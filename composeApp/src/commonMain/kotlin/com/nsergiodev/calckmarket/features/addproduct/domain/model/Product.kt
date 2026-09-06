package com.nsergiodev.calckmarket.features.addproduct.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Double,
    val total: Double = price * quantity
)
