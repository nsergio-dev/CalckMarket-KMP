package com.nsergiodev.calckmarket.domain.delegates

interface Buy {

    val marketName: String

    val products: List<Product>

    fun countProducts(): Int = products.count()

    fun sumOfPrices(): Double = products.sumOf { it.price }
}
