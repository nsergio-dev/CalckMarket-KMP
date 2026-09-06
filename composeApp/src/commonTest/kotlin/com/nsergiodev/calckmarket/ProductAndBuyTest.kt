package com.nsergiodev.calckmarket

import com.nsergiodev.calckmarket.features.addproduct.domain.model.MarketType
import com.nsergiodev.calckmarket.features.addproduct.domain.model.Product
import com.nsergiodev.calckmarket.features.home.domain.model.Buy
import kotlin.test.Test
import kotlin.test.assertEquals

class ProductAndBuyTest {

    @Test
    fun productTotal_shouldCalculatePriceMultipliedByQuantity() {
        val product = Product(
            id = "1",
            name = "Leche",
            price = 4500.0,
            quantity = 3.0
        )
        assertEquals(13500.0, product.total)
    }

    @Test
    fun buyTotalAmount_shouldSumAllProductTotalsCorrectly() {
        val products = listOf(
            Product(id = "1", name = "Arroz", price = 3000.0, quantity = 2.0), // 6000
            Product(id = "2", name = "Aceite", price = 12000.0, quantity = 1.5) // 18000
        )
        val buy = Buy(
            id = "b1",
            marketName = "D1 Calle 80",
            dateEpochMillis = 1700000000000L,
            products = products
        )

        assertEquals(24000.0, buy.totalAmount)
        assertEquals(2, buy.productCount)
        assertEquals(MarketType.D1, buy.marketType)
    }

    @Test
    fun marketType_shouldIdentifySupermarketCorrectlyFromName() {
        assertEquals(MarketType.D1, MarketType.fromName("Tienda D1"))
        assertEquals(MarketType.EXITO, MarketType.fromName("Éxito Wow"))
        assertEquals(MarketType.CARULLA, MarketType.fromName("Carulla Express"))
        assertEquals(MarketType.ARA, MarketType.fromName("Supermercado Ara"))
        assertEquals(MarketType.OTRO, MarketType.fromName("Fruver Don Pedro"))
    }
}
