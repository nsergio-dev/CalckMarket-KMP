package com.nsergiodev.calckmarket.presentation.screens.addproduct.models

data class UiProductRow(
    val id: Long,
    val name: String,
    val qty: Int,
    val unit: UiUnit,
    val unitPrice: Double,
) {
    val subtotal: Double get() = qty * unitPrice
}