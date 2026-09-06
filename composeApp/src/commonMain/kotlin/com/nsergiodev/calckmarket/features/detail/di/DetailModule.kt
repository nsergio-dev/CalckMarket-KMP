package com.nsergiodev.calckmarket.features.detail.di

import com.nsergiodev.calckmarket.features.detail.domain.usecase.GetBuyDetailUseCase
import com.nsergiodev.calckmarket.features.detail.presentation.viewmodel.PurchaseDetailViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    factoryOf(::GetBuyDetailUseCase)
    viewModel { params ->
        PurchaseDetailViewModel(
            buyId = params.get(),
            getBuyDetailUseCase = get()
        )
    }
}
