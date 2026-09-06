package com.nsergiodev.calckmarket.features.home.data.repository

import com.nsergiodev.calckmarket.core.data.datasource.LocalBuyDataSource
import com.nsergiodev.calckmarket.features.home.domain.model.Buy
import com.nsergiodev.calckmarket.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class HomeRepositoryImpl(
    private val localDataSource: LocalBuyDataSource = LocalBuyDataSource.instance
) : HomeRepository {

    override fun getBuys(): Flow<List<Buy>> {
        return localDataSource.getBuys()
    }

    override suspend fun deleteBuy(id: String) {
        localDataSource.deleteBuy(id)
    }
}
