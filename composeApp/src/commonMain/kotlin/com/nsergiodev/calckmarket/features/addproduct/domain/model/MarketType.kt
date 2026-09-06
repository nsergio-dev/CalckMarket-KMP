package com.nsergiodev.calckmarket.features.addproduct.domain.model

import androidx.compose.ui.graphics.Color
import com.nsergiodev.calckmarket.core.theme.AraColor
import com.nsergiodev.calckmarket.core.theme.CarullaColor
import com.nsergiodev.calckmarket.core.theme.D1Color
import com.nsergiodev.calckmarket.core.theme.ExitoColor
import com.nsergiodev.calckmarket.core.theme.GenericMarketColor
import com.nsergiodev.calckmarket.core.theme.JumboColor
import com.nsergiodev.calckmarket.core.theme.OlimpicaColor

enum class MarketType(val displayName: String, val brandColor: Color) {
    D1("D1", D1Color),
    EXITO("Éxito", ExitoColor),
    ARA("Ara", AraColor),
    CARULLA("Carulla", CarullaColor),
    JUMBO("Jumbo", JumboColor),
    OLIMPICA("Olímpica", OlimpicaColor),
    OTRO("Otro", GenericMarketColor);

    companion object {
        fun fromName(name: String): MarketType {
            val normalized = name.trim().lowercase()
            return entries.firstOrNull {
                normalized.contains(it.displayName.lowercase())
            } ?: OTRO
        }
    }
}
