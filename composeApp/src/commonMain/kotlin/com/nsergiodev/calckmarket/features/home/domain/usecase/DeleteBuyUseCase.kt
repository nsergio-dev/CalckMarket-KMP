package com.nsergiodev.calckmarket.features.home.domain.usecase

import com.nsergiodev.calckmarket.features.home.domain.repository.HomeRepository

class DeleteBuyUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(id: String) = repository.deleteBuy(id)
}
