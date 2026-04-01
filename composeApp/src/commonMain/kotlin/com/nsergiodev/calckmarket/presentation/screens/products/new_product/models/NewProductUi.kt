package com.nsergiodev.calckmarket.presentation.screens.products.new_product.models

data class NewProductUi(
    val name: String,
    val price: Int,
    val unit: String,
    val store: String,
    val isFavorite: Boolean
)