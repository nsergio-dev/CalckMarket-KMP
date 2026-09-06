package com.nsergiodev.calckmarket.features.detail.domain.usecase

import com.nsergiodev.calckmarket.core.data.database.dao.BuyDao
import com.nsergiodev.calckmarket.core.data.database.mapper.toDomain
import com.nsergiodev.calckmarket.features.home.domain.model.Buy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBuyDetailUseCase(
    private val buyDao: BuyDao
) {
    operator fun invoke(buyId: String): Flow<Buy?> {
        return buyDao.getBuyById(buyId).map { it?.toDomain() }
    }
}
