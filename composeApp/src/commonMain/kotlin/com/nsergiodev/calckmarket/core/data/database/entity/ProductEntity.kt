package com.nsergiodev.calckmarket.core.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = BuyEntity::class,
            parentColumns = ["id"],
            childColumns = ["buyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["buyId"])]
)
data class ProductEntity(
    @PrimaryKey val id: String,
    val buyId: String,
    val name: String,
    val price: Double,
    val quantity: Double,
    val total: Double
)
