package com.nsergiodev.calckmarket.core.data.database.mapper

import com.nsergiodev.calckmarket.core.data.database.entity.BuyEntity
import com.nsergiodev.calckmarket.core.data.database.entity.ProductEntity
import com.nsergiodev.calckmarket.core.data.database.model.BuyWithProducts
import com.nsergiodev.calckmarket.features.addproduct.domain.model.Product
import com.nsergiodev.calckmarket.features.home.domain.model.Buy

fun BuyWithProducts.toDomain(): Buy {
    return Buy(
        id = buy.id,
        marketName = buy.marketName,
        dateEpochMillis = buy.dateEpochMillis,
        products = products.map { it.toDomain() },
        paidAmount = buy.paidAmount,
        isCompleted = buy.isCompleted
    )
}

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        price = price,
        quantity = quantity,
        total = total
    )
}

fun Buy.toEntity(): Pair<BuyEntity, List<ProductEntity>> {
    val buyEntity = BuyEntity(
        id = id,
        marketName = marketName,
        dateEpochMillis = dateEpochMillis,
        paidAmount = paidAmount,
        isCompleted = isCompleted
    )

    val productEntities = products.map { product ->
        ProductEntity(
            id = product.id,
            buyId = id,
            name = product.name,
            price = product.price,
            quantity = product.quantity,
            total = product.total
        )
    }

    return Pair(buyEntity, productEntities)
}
