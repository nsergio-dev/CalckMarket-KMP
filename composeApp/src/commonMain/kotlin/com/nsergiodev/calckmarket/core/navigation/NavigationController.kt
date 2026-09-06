package com.nsergiodev.calckmarket.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nsergiodev.calckmarket.features.addproduct.presentation.screen.AddProductScreen
import com.nsergiodev.calckmarket.features.detail.presentation.screen.PurchaseDetailScreen
import com.nsergiodev.calckmarket.features.home.presentation.screen.HomeScreen

@Composable
fun NavigationController() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.Home
    ) {
        composable<Screens.Home> {
            HomeScreen(
                onNavigateToAddProduct = {
                    navController.navigate(Screens.AddProductScreen)
                },
                onNavigateToDetail = { buyId ->
                    navController.navigate(Screens.PurchaseDetail(buyId = buyId))
                }
            )
        }

        composable<Screens.AddProductScreen> {
            AddProductScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onPurchaseFinished = { buyId ->
                    navController.navigate(Screens.PurchaseDetail(buyId = buyId)) {
                        popUpTo<Screens.Home>()
                    }
                }
            )
        }

        composable<Screens.PurchaseDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Screens.PurchaseDetail>()
            PurchaseDetailScreen(
                buyId = route.buyId,
                onClose = {
                    navController.popBackStack()
                },
                onFinish = {
                    navController.popBackStack(Screens.Home, inclusive = false)
                }
            )
        }
    }
}