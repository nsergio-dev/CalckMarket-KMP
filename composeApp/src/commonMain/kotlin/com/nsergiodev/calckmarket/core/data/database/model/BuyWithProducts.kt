package com.nsergiodev.calckmarket.core.data.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.nsergiodev.calckmarket.core.data.database.entity.BuyEntity
import com.nsergiodev.calckmarket.core.data.database.entity.ProductEntity

data class BuyWithProducts(
    @Embedded val buy: BuyEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "buyId"
    )
    val products: List<ProductEntity>
)
