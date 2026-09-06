package com.nsergiodev.calckmarket.core.di

import com.nsergiodev.calckmarket.core.data.database.CalkMarketDatabase
import com.nsergiodev.calckmarket.core.data.database.getRoomDatabase
import org.koin.dsl.module

val coreModule = module {
    single {
        getRoomDatabase(builder = get())
    }

    single {
        get<CalkMarketDatabase>().buyDao()
    }
}
