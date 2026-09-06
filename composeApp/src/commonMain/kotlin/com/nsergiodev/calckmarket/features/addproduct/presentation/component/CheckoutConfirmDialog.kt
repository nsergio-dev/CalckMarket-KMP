package com.nsergiodev.calckmarket.features.addproduct.presentation.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nsergiodev.calckmarket.core.utils.asCurrency

@Composable
fun CheckoutConfirmDialog(
    show: Boolean,
    marketName: String,
    totalAmount: Double,
    itemCount: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (!show) return

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = "Confirmar y Guardar Compra",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "¿Deseas finalizar la compra en $marketName por un total de ${totalAmount.asCurrency()} ($itemCount productos)? Se guardará en tu historial.",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar Compra", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Seguir Comprando")
            }
        }
    )
}

@Composable
fun EmptyCartDialog(
    show: Boolean,
    onDismiss: () -> Unit
) {
    if (!show) return

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        title = { Text("Carrito Vacío") },
        text = {
            Text("Agrega al menos un producto a la lista antes de finalizar la compra.")
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Entendido")
            }
        }
    )
}
