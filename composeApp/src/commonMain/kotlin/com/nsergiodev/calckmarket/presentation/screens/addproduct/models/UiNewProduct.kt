package com.nsergiodev.calckmarket.presentation.screens.addproduct.models

data class UiNewProduct(
    val name: String,
    val qty: Int,
    val unit: UiUnit,
    val unitPrice: Double
)