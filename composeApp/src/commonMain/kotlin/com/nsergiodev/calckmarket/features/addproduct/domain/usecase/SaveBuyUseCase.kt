package com.nsergiodev.calckmarket.features.addproduct.domain.usecase

import com.nsergiodev.calckmarket.features.addproduct.domain.repository.AddProductRepository
import com.nsergiodev.calckmarket.features.home.domain.model.Buy

class SaveBuyUseCase(
    private val repository: AddProductRepository
) {
    suspend operator fun invoke(buy: Buy) = repository.saveBuy(buy)
}
