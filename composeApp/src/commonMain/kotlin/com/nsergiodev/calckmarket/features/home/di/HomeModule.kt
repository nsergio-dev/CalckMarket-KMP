package com.nsergiodev.calckmarket.features.home.di

import com.nsergiodev.calckmarket.features.home.data.repository.HomeRepositoryImpl
import com.nsergiodev.calckmarket.features.home.domain.repository.HomeRepository
import com.nsergiodev.calckmarket.features.home.domain.usecase.DeleteBuyUseCase
import com.nsergiodev.calckmarket.features.home.domain.usecase.DiscardInProgressBuyUseCase
import com.nsergiodev.calckmarket.features.home.domain.usecase.GetBuysUseCase
import com.nsergiodev.calckmarket.features.home.domain.usecase.GetInProgressBuyUseCase
import com.nsergiodev.calckmarket.features.home.presentation.viewmodel.HomeViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    single<HomeRepository> {
        HomeRepositoryImpl(buyDao = get())
    }

    factoryOf(::GetBuysUseCase)
    factoryOf(::GetInProgressBuyUseCase)
    factoryOf(::DiscardInProgressBuyUseCase)
    factoryOf(::DeleteBuyUseCase)
    viewModelOf(::HomeViewModel)
}
