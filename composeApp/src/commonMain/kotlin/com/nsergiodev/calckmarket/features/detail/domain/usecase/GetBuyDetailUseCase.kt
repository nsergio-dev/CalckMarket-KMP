package com.nsergiodev.calckmarket.features.detail.domain.usecase

import com.nsergiodev.calckmarket.core.data.datasource.LocalBuyDataSource
import com.nsergiodev.calckmarket.features.home.domain.model.Buy
import kotlinx.coroutines.flow.Flow

class GetBuyDetailUseCase(
    private val localDataSource: LocalBuyDataSource = LocalBuyDataSource.instance
) {
    operator fun invoke(buyId: String): Flow<Buy?> {
        return localDataSource.getBuyById(buyId)
    }
}
