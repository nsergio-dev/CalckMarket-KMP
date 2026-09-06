package com.nsergiodev.calckmarket

import androidx.compose.runtime.Composable
import com.nsergiodev.calckmarket.core.navigation.NavigationController
import com.nsergiodev.calckmarket.core.theme.CalkMarketTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    CalkMarketTheme {
        NavigationController()
    }
}