package com.nsergiodev.calckmarket.domain.delegates

interface Product {
    val id: Long
    val name: String
    val price: Double
    val quantity: Double
}