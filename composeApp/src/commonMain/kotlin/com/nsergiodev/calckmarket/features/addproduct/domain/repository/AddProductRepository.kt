package com.nsergiodev.calckmarket.features.addproduct.domain.repository

import com.nsergiodev.calckmarket.features.home.domain.model.Buy
import kotlinx.coroutines.flow.Flow

interface AddProductRepository {
    fun getInProgressBuy(): Flow<Buy?>
    fun getBuyById(id: String): Flow<Buy?>
    suspend fun saveBuy(buy: Buy)
    suspend fun deleteDraftBuys()
    suspend fun deleteBuy(id: String)
}
