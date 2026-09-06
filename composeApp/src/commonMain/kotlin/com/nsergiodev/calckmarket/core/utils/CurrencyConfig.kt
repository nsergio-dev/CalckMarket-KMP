package com.nsergiodev.calckmarket.core.utils

/**
 * Configuración global para el formateo de moneda en la aplicación.
 * Permite cambiar fácilmente entre COP (sin decimales) o monedas internacionales (USD, EUR con decimales).
 */
object CurrencyConfig {
    var localeIdentifier: String = "es_CO"
    var showDecimals: Boolean = false
    var currencySymbol: String = "$"
}
