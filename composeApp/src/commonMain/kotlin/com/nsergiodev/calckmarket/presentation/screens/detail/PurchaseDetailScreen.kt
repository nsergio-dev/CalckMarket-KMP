package com.nsergiodev.calckmarket.presentation.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nsergiodev.calckmarket.presentation.screens.detail.models.PurchaseSummaryUiState
import com.nsergiodev.calckmarket.presentation.screens.detail.models.SummaryDifferenceState
import com.nsergiodev.calckmarket.presentation.screens.detail.models.SummaryProductUi

@Composable
fun PurchaseDetailScreen(
    onClose: () -> Unit,
    onSaveTemplate: () -> Unit,
    onSharePdf: () -> Unit,
    onFinish: () -> Unit
) {
    //todo!! this is only for test visual...., impl with viewModel
    val uiState = remember {
        PurchaseSummaryUiState(
            marketName = "D1 Barrio",
            dateText = "Saturday, 28 de February de 2026 • 01:48",
            total = "22100",
            paid = "30000",
            difference = "7900",
            differenceState = SummaryDifferenceState.GREATER,
            products = listOf(
                SummaryProductUi(
                    id = "1",
                    name = "Sal",
                    quantity = "3",
                    unit = "lb",
                    unitPrice = "2500",
                    subtotal = "7500"
                ),
                SummaryProductUi(
                    id = "2",
                    name = "Arroz",
                    quantity = "2",
                    unit = "kg",
                    unitPrice = "4800",
                    subtotal = "9600"
                ),
                SummaryProductUi(
                    id = "3",
                    name = "Aceite",
                    quantity = "1",
                    unit = "und",
                    unitPrice = "5000",
                    subtotal = "5000"
                )
            )
        )
    }
    Scaffold(
        topBar = {
            PurchaseSummaryTopBar(
                onClose = onClose,
                onShare = onSharePdf
            )
        },
        bottomBar = {
            PurchaseSummaryBottomBar(
                onSaveTemplate = onSaveTemplate,
                onSharePdf = onSharePdf,
                onFinish = onFinish
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 160.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                MarketSummaryCard(
                    marketName = uiState.marketName,
                    dateText = uiState.dateText
                )
            }

            item {
                PaymentSummaryCard(
                    total = uiState.total,
                    paid = uiState.paid,
                    difference = uiState.difference,
                    differenceState = uiState.differenceState
                )
            }

            item {
                Text(
                    text = "${uiState.products.size} productos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            items(
                items = uiState.products,
                key = { it.id }
            ) { product ->
                SummaryProductCard(product = product)
            }
        }
    }
}

@Composable
private fun SummaryProductCard(
    product: SummaryProductUi
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                AssistChip(
                    onClick = {},
                    enabled = false,
                    label = {
                        Text(product.unit)
                    }
                )
            }

            Text(
                text = "${product.quantity} x ${product.unitPrice}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Subtotal: ${product.subtotal}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun MarketSummaryCard(
    marketName: String,
    dateText: String
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Storefront,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = marketName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = dateText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PaymentSummaryCard(
    total: String,
    paid: String,
    difference: String,
    differenceState: SummaryDifferenceState
) {
    val differenceColor = when (differenceState) {
        SummaryDifferenceState.GREATER -> MaterialTheme.colorScheme.error
        SummaryDifferenceState.EXACT -> MaterialTheme.colorScheme.onSurface
        SummaryDifferenceState.LESS -> Color(0xFF2E7D32)
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SummaryValueRow(label = "Total app", value = total)
            SummaryValueRow(label = "Pagado", value = paid)

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            SummaryValueRow(
                label = "Diferencia",
                value = difference,
                valueColor = differenceColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PurchaseSummaryTopBar(
    onClose: () -> Unit,
    onShare: () -> Unit
) {
    TopAppBar(
        title = {
            Text("Resumen de compra")
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
            IconButton(onClick = onShare) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Compartir"
                )
            }
        }
    )
}

@Composable
private fun PurchaseSummaryBottomBar(
    onSaveTemplate: () -> Unit,
    onSharePdf: () -> Unit,
    onFinish: () -> Unit
) {
    Surface(
        tonalElevation = 3.dp,
        shadowElevation = 8.dp
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text("Guardar como plantilla")
            }

            Button(
                onClick = onSharePdf,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text("Compartir PDF")
            }

            TextButton(
                onClick = onFinish,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Finalizar y volver al inicio")
            }
        }
    }
}


@Composable
private fun SummaryValueRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}