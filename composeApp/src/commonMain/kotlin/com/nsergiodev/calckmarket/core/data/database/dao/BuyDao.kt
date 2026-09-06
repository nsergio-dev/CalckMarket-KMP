package com.nsergiodev.calckmarket.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nsergiodev.calckmarket.core.data.database.entity.BuyEntity
import com.nsergiodev.calckmarket.core.data.database.entity.ProductEntity
import com.nsergiodev.calckmarket.core.data.database.model.BuyWithProducts
import kotlinx.coroutines.flow.Flow

@Dao
interface BuyDao {

    @Transaction
    @Query("SELECT * FROM buys WHERE isCompleted = 1 ORDER BY dateEpochMillis DESC")
    fun getAllCompletedBuys(): Flow<List<BuyWithProducts>>

    @Transaction
    @Query("SELECT * FROM buys WHERE isCompleted = 0 ORDER BY dateEpochMillis DESC LIMIT 1")
    fun getInProgressBuy(): Flow<BuyWithProducts?>

    @Transaction
    @Query("SELECT * FROM buys WHERE id = :id LIMIT 1")
    fun getBuyById(id: String): Flow<BuyWithProducts?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuy(buy: BuyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("DELETE FROM products WHERE buyId = :buyId")
    suspend fun deleteProductsByBuyId(buyId: String)

    @Transaction
    suspend fun insertBuyWithProducts(buy: BuyEntity, products: List<ProductEntity>) {
        insertBuy(buy)
        deleteProductsByBuyId(buy.id)
        if (products.isNotEmpty()) {
            insertProducts(products)
        }
    }

    @Query("DELETE FROM buys WHERE id = :id")
    suspend fun deleteBuyById(id: String)

    @Query("DELETE FROM buys WHERE isCompleted = 0")
    suspend fun deleteDraftBuys()
}
