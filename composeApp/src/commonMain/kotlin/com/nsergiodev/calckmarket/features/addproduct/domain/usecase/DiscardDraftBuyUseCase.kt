package com.nsergiodev.calckmarket.features.addproduct.domain.usecase

import com.nsergiodev.calckmarket.features.addproduct.domain.repository.AddProductRepository

class DiscardDraftBuyUseCase(
    private val repository: AddProductRepository
) {
    suspend operator fun invoke() = repository.deleteDraftBuys()
}
