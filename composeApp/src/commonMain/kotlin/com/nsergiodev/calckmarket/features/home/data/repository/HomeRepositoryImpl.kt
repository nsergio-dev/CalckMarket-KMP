package com.nsergiodev.calckmarket.features.home.data.repository

import com.nsergiodev.calckmarket.core.data.database.dao.BuyDao
import com.nsergiodev.calckmarket.core.data.database.mapper.toDomain
import com.nsergiodev.calckmarket.features.home.domain.model.Buy
import com.nsergiodev.calckmarket.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeRepositoryImpl(
    private val buyDao: BuyDao
) : HomeRepository {

    override fun getCompletedBuys(): Flow<List<Buy>> {
        return buyDao.getAllCompletedBuys().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getInProgressBuy(): Flow<Buy?> {
        return buyDao.getInProgressBuy().map { it?.toDomain() }
    }

    override suspend fun deleteBuy(id: String) {
        buyDao.deleteBuyById(id)
    }

    override suspend fun discardDraftBuys() {
        buyDao.deleteDraftBuys()
    }
}
