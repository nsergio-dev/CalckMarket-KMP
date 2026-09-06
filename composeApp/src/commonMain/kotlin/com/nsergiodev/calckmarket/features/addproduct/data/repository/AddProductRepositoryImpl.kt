package com.nsergiodev.calckmarket.features.addproduct.data.repository

import com.nsergiodev.calckmarket.core.data.database.dao.BuyDao
import com.nsergiodev.calckmarket.core.data.database.mapper.toDomain
import com.nsergiodev.calckmarket.core.data.database.mapper.toEntity
import com.nsergiodev.calckmarket.features.addproduct.domain.repository.AddProductRepository
import com.nsergiodev.calckmarket.features.home.domain.model.Buy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AddProductRepositoryImpl(
    private val buyDao: BuyDao
) : AddProductRepository {

    override fun getInProgressBuy(): Flow<Buy?> {
        return buyDao.getInProgressBuy().map { it?.toDomain() }
    }

    override fun getBuyById(id: String): Flow<Buy?> {
        return buyDao.getBuyById(id).map { it?.toDomain() }
    }

    override suspend fun saveBuy(buy: Buy) {
        val (buyEntity, productEntities) = buy.toEntity()
        buyDao.insertBuyWithProducts(buyEntity, productEntities)
    }

    override suspend fun deleteDraftBuys() {
        buyDao.deleteDraftBuys()
    }

    override suspend fun deleteBuy(id: String) {
        buyDao.deleteBuyById(id)
    }
}
