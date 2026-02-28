package com.nsergiodev.calckmarket.core.navigation
import kotlinx.serialization.Serializable

@Serializable
sealed class Screens {

    @Serializable
    object Home: Screens()

    @Serializable
    object AddProductScreen: Screens()

    @Serializable
    object PurchaseDetailScreen: Screens()

}