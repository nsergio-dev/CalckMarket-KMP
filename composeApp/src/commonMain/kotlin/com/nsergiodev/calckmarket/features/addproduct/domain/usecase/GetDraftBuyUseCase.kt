package com.nsergiodev.calckmarket.features.addproduct.domain.usecase

import com.nsergiodev.calckmarket.features.addproduct.domain.repository.AddProductRepository
import com.nsergiodev.calckmarket.features.home.domain.model.Buy
import kotlinx.coroutines.flow.Flow

class GetDraftBuyUseCase(
    private val repository: AddProductRepository
) {
    operator fun invoke(): Flow<Buy?> = repository.getInProgressBuy()
}
