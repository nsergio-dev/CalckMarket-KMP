package com.nsergiodev.calckmarket

import androidx.compose.ui.window.ComposeUIViewController
import com.nsergiodev.calckmarket.core.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}