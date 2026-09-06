package com.nsergiodev.calckmarket.core.di

import com.nsergiodev.calckmarket.features.addproduct.di.addProductModule
import com.nsergiodev.calckmarket.features.detail.di.detailModule
import com.nsergiodev.calckmarket.features.home.di.homeModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

expect fun platformModule(): Module

fun initKoin(koinConfig: KoinAppDeclaration? = null) {
    startKoin {
        koinConfig?.invoke(this)
        modules(
            coreModule,
            homeModule,
            addProductModule,
            detailModule,
            platformModule()
        )
    }
}
