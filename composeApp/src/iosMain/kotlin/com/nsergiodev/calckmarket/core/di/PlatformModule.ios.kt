package com.nsergiodev.calckmarket.core.di

import com.nsergiodev.calckmarket.core.data.database.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single {
        getDatabaseBuilder()
    }
}
