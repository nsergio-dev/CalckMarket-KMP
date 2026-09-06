package com.nsergiodev.calckmarket.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screens {

    @Serializable
    data object Home : Screens()

    @Serializable
    data object AddProductScreen : Screens()

    @Serializable
    data class PurchaseDetail(val buyId: String) : Screens()
}