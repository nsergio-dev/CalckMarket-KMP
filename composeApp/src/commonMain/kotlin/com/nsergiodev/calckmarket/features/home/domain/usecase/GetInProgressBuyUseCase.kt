package com.nsergiodev.calckmarket.features.home.domain.usecase

import com.nsergiodev.calckmarket.features.home.domain.model.Buy
import com.nsergiodev.calckmarket.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class GetInProgressBuyUseCase(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<Buy?> = repository.getInProgressBuy()
}
