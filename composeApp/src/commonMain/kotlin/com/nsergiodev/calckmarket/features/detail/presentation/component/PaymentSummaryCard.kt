package com.nsergiodev.calckmarket.features.detail.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nsergiodev.calckmarket.core.utils.asCurrency

@Composable
fun PaymentSummaryCard(
    total: Double,
    paid: Double,
    difference: Double,
    modifier: Modifier = Modifier
) {
    val differenceColor = when {
        difference > 0 -> MaterialTheme.colorScheme.error
        difference == 0.0 -> MaterialTheme.colorScheme.onSurface
        else -> Color(0xFF2E7D32)
    }

    val differenceLabel = when {
        difference > 0 -> "+${difference.asCurrency()}"
        difference == 0.0 -> "Exacto"
        else -> "-${kotlin.math.abs(difference).asCurrency()}"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryValueRow(
                label = "Total calculado en app",
                value = total.asCurrency()
            )
            SummaryValueRow(
                label = "Valor pagado en caja",
                value = paid.asCurrency()
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            SummaryValueRow(
                label = "Diferencia",
                value = differenceLabel,
                valueColor = differenceColor
            )
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
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}
