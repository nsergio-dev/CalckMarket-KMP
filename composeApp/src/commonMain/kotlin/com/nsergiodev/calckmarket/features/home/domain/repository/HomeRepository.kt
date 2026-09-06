package com.nsergiodev.calckmarket.features.home.domain.repository

import com.nsergiodev.calckmarket.features.home.domain.model.Buy
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getBuys(): Flow<List<Buy>>
    suspend fun deleteBuy(id: String)
}
