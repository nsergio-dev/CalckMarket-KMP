package com.nsergiodev.calckmarket.features.addproduct.di

import com.nsergiodev.calckmarket.features.addproduct.data.repository.AddProductRepositoryImpl
import com.nsergiodev.calckmarket.features.addproduct.domain.repository.AddProductRepository
import com.nsergiodev.calckmarket.features.addproduct.domain.usecase.DiscardDraftBuyUseCase
import com.nsergiodev.calckmarket.features.addproduct.domain.usecase.GetDraftBuyUseCase
import com.nsergiodev.calckmarket.features.addproduct.domain.usecase.SaveBuyUseCase
import com.nsergiodev.calckmarket.features.addproduct.presentation.viewmodel.AddProductViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val addProductModule = module {
    single<AddProductRepository> {
        AddProductRepositoryImpl(buyDao = get())
    }

    factoryOf(::SaveBuyUseCase)
    factoryOf(::GetDraftBuyUseCase)
    factoryOf(::DiscardDraftBuyUseCase)
    viewModelOf(::AddProductViewModel)
}
