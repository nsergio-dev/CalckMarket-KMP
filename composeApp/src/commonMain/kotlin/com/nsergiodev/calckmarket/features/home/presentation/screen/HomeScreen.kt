package com.nsergiodev.calckmarket.features.home.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nsergiodev.calckmarket.features.home.presentation.component.BuyItemCard
import com.nsergiodev.calckmarket.features.home.presentation.component.EmptyHistoryView
import com.nsergiodev.calckmarket.features.home.presentation.component.HomeSummaryHeader
import com.nsergiodev.calckmarket.features.home.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onNavigateToAddProduct: () -> Unit,
    onNavigateToDetail: (buyId: String) -> Unit,
    viewModel: HomeViewModel = viewModel { HomeViewModel() }
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToAddProduct,
                shape = RoundedCornerShape(18.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.navigationBarsPadding(),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Nueva Compra"
                    )
                },
                text = {
                    Text(
                        text = "Nueva Compra",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // App title / header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CalkMarket 🛒",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Control inteligente de compras",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Summary metrics
            HomeSummaryHeader(
                totalSpent = uiState.totalSpent,
                totalPurchases = uiState.totalPurchases,
                averagePerPurchase = uiState.averagePerPurchase
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Historial de Compras",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (uiState.buys.isEmpty()) {
                EmptyHistoryView(
                    onStartShopping = onNavigateToAddProduct,
                    modifier = Modifier.padding(top = 12.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(
                        items = uiState.buys,
                        key = { it.id }
                    ) { buy ->
                        BuyItemCard(
                            buy = buy,
                            onClick = { onNavigateToDetail(buy.id) },
                            onDelete = { viewModel.onDeleteBuy(buy.id) }
                        )
                    }
                }
            }
        }
    }
}
