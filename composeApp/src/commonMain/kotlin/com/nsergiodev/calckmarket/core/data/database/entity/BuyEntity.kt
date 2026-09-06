package com.nsergiodev.calckmarket.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buys")
data class BuyEntity(
    @PrimaryKey val id: String,
    val marketName: String,
    val dateEpochMillis: Long,
    val paidAmount: Double,
    val isCompleted: Boolean = true
)
