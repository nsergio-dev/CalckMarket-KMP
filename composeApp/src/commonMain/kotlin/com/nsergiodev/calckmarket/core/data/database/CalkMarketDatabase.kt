package com.nsergiodev.calckmarket.core.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.nsergiodev.calckmarket.core.data.database.dao.BuyDao
import com.nsergiodev.calckmarket.core.data.database.entity.BuyEntity
import com.nsergiodev.calckmarket.core.data.database.entity.ProductEntity

const val DB_FILE_NAME = "calkmarket.db"

@Database(
    entities = [BuyEntity::class, ProductEntity::class],
    version = 1,
    exportSchema = false
)
@ConstructedBy(CalkMarketDatabaseConstructor::class)
abstract class CalkMarketDatabase : RoomDatabase() {
    abstract fun buyDao(): BuyDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object CalkMarketDatabaseConstructor : RoomDatabaseConstructor<CalkMarketDatabase> {
    override fun initialize(): CalkMarketDatabase
}
