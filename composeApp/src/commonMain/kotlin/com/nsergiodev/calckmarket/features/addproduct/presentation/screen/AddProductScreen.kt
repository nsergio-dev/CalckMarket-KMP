package com.nsergiodev.calckmarket.features.addproduct.presentation.screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nsergiodev.calckmarket.features.addproduct.presentation.component.EditProductDialog
import com.nsergiodev.calckmarket.features.addproduct.presentation.component.EmptyCartDialog
import com.nsergiodev.calckmarket.features.addproduct.presentation.component.MarketSelector
import com.nsergiodev.calckmarket.features.addproduct.presentation.component.PayDifferenceBottomSheet
import com.nsergiodev.calckmarket.features.addproduct.presentation.component.ProductInputForm
import com.nsergiodev.calckmarket.features.addproduct.presentation.component.ProductItemCard
import com.nsergiodev.calckmarket.features.addproduct.presentation.component.ShoppingBottomBar
import com.nsergiodev.calckmarket.features.addproduct.presentation.viewmodel.AddProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onBackClick: () -> Unit,
    onPurchaseFinished: (buyId: String) -> Unit,
    viewModel: AddProductViewModel = viewModel { AddProductViewModel() }
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.isSavedSuccess, uiState.savedBuyId) {
        val buyId = uiState.savedBuyId
        if (uiState.isSavedSuccess && buyId != null) {
            onPurchaseFinished(buyId)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Canasta de Compras",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            if (uiState.products.isNotEmpty()) {
                ShoppingBottomBar(
                    totalAmount = uiState.totalAmount,
                    itemCount = uiState.totalItems,
                    onCheckoutClick = {
                        viewModel.onShowPayDifferenceSheet(true)
                    }
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    )
                }
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Supermarket Selector
            MarketSelector(
                selectedMarketType = uiState.selectedMarketType,
                currentMarketName = uiState.marketName,
                onMarketSelected = { marketType, customName ->
                    viewModel.onMarketSelected(marketType, customName)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Input Form Card
            ProductInputForm(
                onAddProduct = { name, price, qty ->
                    viewModel.onAddProduct(name, price, qty)
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Products list header
            Text(
                text = "Productos en la canasta (${uiState.totalItems})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.products.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aún no has agregado productos a esta compra.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 24.dp)
                ) {
                    items(
                        items = uiState.products,
                        key = { it.id }
                    ) { product ->
                        ProductItemCard(
                            product = product,
                            onIncrement = { viewModel.onIncrementQuantity(product.id) },
                            onDecrement = { viewModel.onDecrementQuantity(product.id) },
                            onEdit = { viewModel.onSetEditingProduct(product) },
                            onRemove = { viewModel.onRemoveProduct(product.id) }
                        )
                    }
                }
            }
        }

        // Dialogs & Sheets
        EditProductDialog(
            product = uiState.editingProduct,
            onDismiss = { viewModel.onSetEditingProduct(null) },
            onConfirm = { updated -> viewModel.onUpdateProduct(updated) }
        )

        PayDifferenceBottomSheet(
            show = uiState.showPayDifferenceSheet,
            total = uiState.totalAmount,
            onDismiss = { viewModel.onShowPayDifferenceSheet(false) },
            onConfirm = { paidAmount -> viewModel.onCompletePurchase(paidAmount) }
        )

        EmptyCartDialog(
            show = uiState.showEmptyProductsAlert,
            onDismiss = { viewModel.onShowEmptyProductsAlert(false) }
        )
    }
}
