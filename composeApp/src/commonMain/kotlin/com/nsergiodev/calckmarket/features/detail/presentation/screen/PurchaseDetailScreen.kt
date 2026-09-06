package com.nsergiodev.calckmarket.features.detail.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nsergiodev.calckmarket.features.detail.presentation.component.MarketSummaryCard
import com.nsergiodev.calckmarket.features.detail.presentation.component.PaymentSummaryCard
import com.nsergiodev.calckmarket.features.detail.presentation.component.SummaryProductCard
import com.nsergiodev.calckmarket.features.detail.presentation.viewmodel.PurchaseDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseDetailScreen(
    buyId: String,
    onClose: () -> Unit,
    onFinish: () -> Unit,
    viewModel: PurchaseDetailViewModel = viewModel { PurchaseDetailViewModel(buyId = buyId) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val buy = uiState.buy

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Factura y Resumen",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onSharePdf() }) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Compartir"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            if (buy != null) {
                PurchaseSummaryBottomBar(
                    onSaveTemplate = { viewModel.onSaveTemplate() },
                    onSharePdf = { viewModel.onSharePdf() },
                    onFinish = onFinish
                )
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (buy == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No se encontró la información de la compra.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    MarketSummaryCard(
                        marketName = buy.marketName,
                        dateText = buy.formattedDate,
                        marketType = buy.marketType
                    )
                }

                item {
                    PaymentSummaryCard(
                        total = buy.totalAmount,
                        paid = buy.paidAmount,
                        difference = buy.differenceAmount
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Productos comprados (${buy.productCount})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                items(
                    items = buy.products,
                    key = { it.id }
                ) { product ->
                    SummaryProductCard(product = product)
                }
            }
        }

        if (uiState.showTemplateSavedMessage) {
            AlertDialog(
                onDismissRequest = { viewModel.onDismissFeedback() },
                shape = RoundedCornerShape(20.dp),
                title = { Text("Plantilla Guardada") },
                text = { Text("Esta lista de compra ha sido guardada como plantilla para futuras ocasiones.") },
                confirmButton = {
                    TextButton(onClick = { viewModel.onDismissFeedback() }) {
                        Text("Aceptar", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        if (uiState.showShareMessage) {
            AlertDialog(
                onDismissRequest = { viewModel.onDismissFeedback() },
                shape = RoundedCornerShape(20.dp),
                title = { Text("Compartir Factura") },
                text = { Text("El resumen de compra ha sido preparado para compartir.") },
                confirmButton = {
                    TextButton(onClick = { viewModel.onDismissFeedback() }) {
                        Text("Aceptar", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

@Composable
private fun PurchaseSummaryBottomBar(
    onSaveTemplate: () -> Unit,
    onSharePdf: () -> Unit,
    onFinish: () -> Unit
) {
    Surface(
        tonalElevation = 3.dp,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onSaveTemplate,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(46.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text("Guardar como plantilla", fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = onSharePdf,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text("Compartir Factura / PDF", fontWeight = FontWeight.Bold)
            }

            TextButton(
                onClick = onFinish,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Finalizar y volver al inicio",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
