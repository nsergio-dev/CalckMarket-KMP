package com.nsergiodev.calckmarket.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nsergiodev.calckmarket.presentation.screens.addproduct.AddProductScreen
import com.nsergiodev.calckmarket.presentation.screens.home.HomeScreen
import com.nsergiodev.calckmarket.presentation.screens.market.MarketScreen

@Composable
fun NavigationController() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.Home
    ) {
        composable<Screens.Home> {
            HomeScreen {
                navController.navigate(Screens.AddProductScreen)
            }
        }

        composable<Screens.AddProductScreen> {
            AddProductScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onPayClick = {
                    navController.popBackStack()
                }

            )
        }

    }
}