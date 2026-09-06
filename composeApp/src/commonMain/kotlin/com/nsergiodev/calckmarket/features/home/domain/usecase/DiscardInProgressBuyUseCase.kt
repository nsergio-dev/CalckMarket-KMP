package com.nsergiodev.calckmarket.features.home.domain.usecase

import com.nsergiodev.calckmarket.features.home.domain.repository.HomeRepository

class DiscardInProgressBuyUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke() = repository.discardDraftBuys()
}
