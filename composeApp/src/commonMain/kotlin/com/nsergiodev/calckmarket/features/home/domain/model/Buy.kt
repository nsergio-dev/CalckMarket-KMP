package com.nsergiodev.calckmarket.features.home.domain.model

import com.nsergiodev.calckmarket.core.utils.formatEpochToReadableDate
import com.nsergiodev.calckmarket.features.addproduct.domain.model.MarketType
import com.nsergiodev.calckmarket.features.addproduct.domain.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class Buy(
    val id: String,
    val marketName: String,
    val dateEpochMillis: Long,
    val products: List<Product>,
    val paidAmount: Double = products.sumOf { it.total }
) {
    val totalAmount: Double
        get() = products.sumOf { it.total }

    val differenceAmount: Double
        get() = paidAmount - totalAmount

    val productCount: Int
        get() = products.size

    val formattedDate: String
        get() = formatEpochToReadableDate(dateEpochMillis)

    val marketType: MarketType
        get() = MarketType.fromName(marketName)
}
