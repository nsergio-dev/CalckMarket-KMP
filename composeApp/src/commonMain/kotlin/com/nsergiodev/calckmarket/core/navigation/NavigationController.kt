package com.nsergiodev.calckmarket.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nsergiodev.calckmarket.presentation.screens.addproduct.AddProductScreen
import com.nsergiodev.calckmarket.presentation.screens.detail.PurchaseDetailScreen
import com.nsergiodev.calckmarket.presentation.screens.home.HomeScreen
import kotlinx.coroutines.launch

@Composable
fun NavigationController() {

    val navController = rememberNavController()

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Función auxiliar para mostrar el mensaje
    fun showToast(message: String) {
        scope.launch {
            snackBarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.Home,
            modifier = Modifier.padding(paddingValues) // Respetar el espacio del Scaffold
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
                        //todo!!, add viewModel and find purchase by id
                        navController.navigate(Screens.PurchaseDetailScreen)
                    }

                )
            }

            composable<Screens.PurchaseDetailScreen> {
                PurchaseDetailScreen(
                    onClose = {
                        goToHomeAndClearStack(navController)
                    },
                    onSaveTemplate = {
                        showToast("Plantilla guardada (Test)")
                        goToHomeAndClearStack(navController)
                    },
                    onSharePdf = {
                        showToast("PDF compartido (Test)")
                        goToHomeAndClearStack(navController)
                    },
                    onFinish = {
                        goToHomeAndClearStack(navController)
                    }
                )
            }

        }
    }
}

private fun goToHomeAndClearStack(navController: NavController) {
    navController.navigate(Screens.Home) {
        popUpTo(Screens.Home) {
            inclusive = true
        }
    }
}