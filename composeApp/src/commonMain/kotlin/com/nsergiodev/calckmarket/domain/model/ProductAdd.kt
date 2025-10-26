package com.nsergiodev.calckmarket.domain.model

import com.nsergiodev.calckmarket.domain.delegates.Product

data class ProductAdd(
    override val id: Long,
    override val name: String,
    override val price: Double,
    override val quantity: Double,
    val total: Double
): Product