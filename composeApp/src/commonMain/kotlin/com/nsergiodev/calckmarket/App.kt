package com.nsergiodev.calckmarket

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.nsergiodev.calckmarket.core.navigation.NavigationController
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        NavigationController()
    }
}