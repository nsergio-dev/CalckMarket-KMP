package com.nsergiodev.calckmarket.features.home.domain.repository

import com.nsergiodev.calckmarket.features.home.domain.model.Buy
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getCompletedBuys(): Flow<List<Buy>>
    fun getInProgressBuy(): Flow<Buy?>
    suspend fun deleteBuy(id: String)
    suspend fun discardDraftBuys()
}
