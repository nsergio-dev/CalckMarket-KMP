package com.nsergiodev.calckmarket.features.addproduct.data.repository

import com.nsergiodev.calckmarket.core.data.datasource.LocalBuyDataSource
import com.nsergiodev.calckmarket.features.addproduct.domain.repository.AddProductRepository
import com.nsergiodev.calckmarket.features.home.domain.model.Buy

class AddProductRepositoryImpl(
    private val localDataSource: LocalBuyDataSource = LocalBuyDataSource.instance
) : AddProductRepository {

    override suspend fun saveBuy(buy: Buy) {
        localDataSource.saveBuy(buy)
    }

}
