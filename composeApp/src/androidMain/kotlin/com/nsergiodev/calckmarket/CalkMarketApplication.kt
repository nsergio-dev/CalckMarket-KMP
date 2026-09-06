package com.nsergiodev.calckmarket

import android.app.Application
import com.nsergiodev.calckmarket.core.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class CalkMarketApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@CalkMarketApplication)
            androidLogger()
        }
    }
}
