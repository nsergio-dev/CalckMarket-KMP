package com.nsergiodev.calckmarket.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsergiodev.calckmarket.core.navigation.Screens
import com.nsergiodev.calckmarket.core.utils.asCurrency
import com.nsergiodev.calckmarket.domain.delegates.Buy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (Screens) -> Unit,
    openProductsScreen: () -> Unit
) {

    val listState = rememberLazyListState()
    /*LaunchedEffect(Unit) {
        List(20) {
            val buy = com.nsergiodev.calckmarket.domain.model.Buy(
                marketName = "Market Name $it",
                products = listOf(
                    _root_ide_package_.com.nsergiodev.calckmarket.domain.model.ProductAdd(
                        id = it.toLong(),
                        name = "Producto",
                        price = it.toDouble(),
                        quantity = it.toDouble(),
                        total = it.toDouble()
                    )
                )
            )
            dummyCurrentBuys.add(buy)
            listState.scrollToItem(dummyCurrentBuys.lastIndex)
        }

    }*/
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigate(Screens.NewPurchaseScreen) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear compra"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CalkMarket",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
            Spacer(
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Bienvenido \n\nPulsa + para iniciar",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )

            if (dummyCurrentBuys.isEmpty()) {
                Text("Aqui apareceran tus ultimas compras")
                OutlinedButton(
                    content = { Text("Ver ultimas compras") },
                    onClick = {

                    }
                )
                OutlinedButton(
                    content = { Text("Productos") },
                    onClick = openProductsScreen
                )
            } else {
                Card(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        reverseLayout = true
                    ) {
                        items(dummyCurrentBuys) { item ->
                            BuyItemCard(item)
                        }
                    }
                }

            }

        }
    }
}

@Composable
private fun BuyItemCard(item: Buy) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        ListItem(
            headlineContent = { Text(item.marketName) },
            supportingContent = {
                Column {
                    Text("Cant: ${item.countProducts()}")
                    Text("14 Oct 2017")
                }
            },
            trailingContent = {
                Text(
                    text = item.sumOfPrices().asCurrency(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        )
    }
}

val dummyCurrentBuys = mutableStateListOf<Buy>()
