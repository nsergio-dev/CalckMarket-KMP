package com.nsergiodev.calckmarket.features.addproduct.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsergiodev.calckmarket.core.utils.CurrencyVisualTransformation
import com.nsergiodev.calckmarket.core.utils.asCurrency
import kotlin.math.abs

enum class PayDifferenceState {
    GREATER,
    EXACT,
    LESS,
    EMPTY
}

data class PayDifferenceUi(
    val state: PayDifferenceState,
    val difference: Double = 0.0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayDifferenceBottomSheet(
    show: Boolean,
    total: Double,
    onDismiss: () -> Unit,
    onConfirm: (paid: Double) -> Unit
) {
    if (!show) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var paidValue by remember { mutableStateOf(if (total > 0) total.toLong().toString() else "") }

    val differenceUi = remember(total, paidValue) {
        buildPayDifference(total = total, paidValue = paidValue)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Resumen de Pago y Diferencia",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            SummaryRow(
                label = "Total calculado en app:",
                value = total.asCurrency()
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = paidValue,
                onValueChange = { raw ->
                    paidValue = raw.filter { it.isDigit() }
                },
                label = { Text("Valor pagado en caja") },
                placeholder = { Text("$0") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = CurrencyVisualTransformation()
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            DifferenceSection(
                differenceUi = differenceUi
            )

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                onClick = {
                    val paid = paidValue.toDoubleOrNull() ?: total
                    onConfirm(paid)
                },
                shape = RoundedCornerShape(14.dp),
                enabled = paidValue.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Guardar y ver factura",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun DifferenceSection(
    differenceUi: PayDifferenceUi
) {
    val positiveColor = Color(0xFF2E7D32)
    val negativeColor = MaterialTheme.colorScheme.error

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Diferencia:",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            when (differenceUi.state) {
                PayDifferenceState.GREATER -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ErrorOutline,
                            contentDescription = null,
                            tint = negativeColor
                        )
                        Text(
                            text = "+${differenceUi.difference.asCurrency()}",
                            color = negativeColor,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                PayDifferenceState.EXACT -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = positiveColor
                        )
                        Text(
                            text = "Exacto",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = positiveColor
                        )
                    }
                }

                PayDifferenceState.LESS -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = positiveColor
                        )
                        Text(
                            text = "-${differenceUi.difference.asCurrency()}",
                            color = positiveColor,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                PayDifferenceState.EMPTY -> {
                    Text(
                        text = "$0",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        when (differenceUi.state) {
            PayDifferenceState.GREATER -> {
                Text(
                    text = "El valor pagado fue mayor al total calculado. Puede que falte un producto por registrar o que el precio de algún artículo haya sido diferente.",
                    color = negativeColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            PayDifferenceState.LESS -> {
                Text(
                    text = "El valor pagado fue menor al total calculado. Puede deberse a un descuento aplicado o a una diferencia en el precio de algún artículo.",
                    color = positiveColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            else -> Unit
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

private fun buildPayDifference(
    total: Double,
    paidValue: String
): PayDifferenceUi {
    val paid = paidValue.toDoubleOrNull() ?: return PayDifferenceUi(
        state = PayDifferenceState.EMPTY
    )

    val difference = paid - total

    return when {
        difference > 0 -> PayDifferenceUi(
            state = PayDifferenceState.GREATER,
            difference = difference
        )

        difference == 0.0 -> PayDifferenceUi(
            state = PayDifferenceState.EXACT,
            difference = 0.0
        )

        else -> PayDifferenceUi(
            state = PayDifferenceState.LESS,
            difference = abs(difference)
        )
    }
}
