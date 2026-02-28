package com.nsergiodev.calckmarket.presentation.screens.addproduct

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.nsergiodev.calckmarket.presentation.screens.addproduct.components.PayDifferenceBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onBackClick: () -> Unit = {},
    onPayClick: () -> Unit = {}
) {

    var marketName by remember { mutableStateOf("D1") }

    var showDialogSureToLeave by remember { mutableStateOf(false) }
    var showDialogEmptyProducts by remember { mutableStateOf(false) }
    var showPaySheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBarContent(
                onBack = onBackClick,
                marketName = marketName,
                dateText = "22 feb, 18:06",
                statusChipText = marketName
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        AddProductScreenUi(
            modifier = Modifier.padding(innerPadding),
            onPay = {
                showPaySheet = true
            }
        )
    }

    MarketNameDialog(
        showDialog = marketName.isEmpty(),
        onDismiss = { marketName = "" },
        onConfirm = { marketName = it }
    )

    EmptyProductsDialog(
        showDialog = showDialogEmptyProducts,
        onDismiss = { showDialogEmptyProducts = false },
        onConfirm = { showDialogEmptyProducts = false }
    )

    SureToLeaveDialog(
        showDialog = showDialogSureToLeave,
        onDismiss = { showDialogSureToLeave = false },
        onConfirm = {
            /* dummyCurrentBuys.add(
                 Buy(
                     marketName = marketName,
                     products = listProducts
                 )
             )*/
            showDialogSureToLeave = false
            onPayClick.invoke()
        }
    )

    PayDifferenceBottomSheet(
        show = showPaySheet,
        total = 2000,
        onDismiss = { showPaySheet = false },
        onConfirm = { paidValue ->
            showPaySheet = false
            onPayClick.invoke()
        }
    )
}

@Composable
private fun EmptyProductsDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Datos vacios") },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun MarketNameDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    if (showDialog) {
        var text by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            title = { Text("Tienda") },
            text = {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Ej. D1, Ara, Olímpica") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (text.isNotBlank()) onConfirm(text)
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Composable
fun SureToLeaveDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text("¿No olvidas nada mas?")
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirm
                ) {
                    Text("Pagar")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun TopBarContent(
    onBack: () -> Unit,
    marketName: String,
    dateText: String,
    statusChipText: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar")
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            ) {
                Text(
                    text = marketName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AssistChip(
                onClick = {},
                label = { Text(statusChipText) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
                /*border = AssistChipDefaults.assistChipBorder(
                            borderColor = MaterialTheme.colorScheme.outlineVariant
                        )*/
            )
        }

        Divider(
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(Modifier.height(10.dp))
    }
}