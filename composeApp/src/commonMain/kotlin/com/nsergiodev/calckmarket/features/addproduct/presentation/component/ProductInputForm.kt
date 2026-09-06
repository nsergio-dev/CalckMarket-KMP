package com.nsergiodev.calckmarket.features.addproduct.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsergiodev.calckmarket.core.utils.CurrencyVisualTransformation
import com.nsergiodev.calckmarket.core.utils.asCurrency

@Composable
fun ProductInputForm(
    onAddProduct: (String, Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val nameFocusRequester = remember { FocusRequester() }

    var name by remember { mutableStateOf("") }
    var rawPrice by remember { mutableStateOf("") }
    var rawQuantity by remember { mutableStateOf("1") }

    var isNameError by remember { mutableStateOf(false) }
    var isPriceError by remember { mutableStateOf(false) }
    var isQuantityError by remember { mutableStateOf(false) }

    val numericPrice by remember {
        derivedStateOf { rawPrice.toDoubleOrNull() ?: 0.0 }
    }
    val numericQuantity by remember {
        derivedStateOf { rawQuantity.toDoubleOrNull() ?: 0.0 }
    }
    val subtotalPreview by remember {
        derivedStateOf { numericPrice * numericQuantity }
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Agregar Producto",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Product Name
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    if (isNameError && it.isNotBlank()) isNameError = false
                },
                label = { Text("Nombre del producto") },
                placeholder = { Text("Ej. Leche entera, Arroz, Huevos") },
                isError = isNameError,
                supportingText = if (isNameError) {
                    { Text("Ingresa el nombre del producto") }
                } else null,
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(nameFocusRequester)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Unit Price Input
                OutlinedTextField(
                    value = rawPrice,
                    onValueChange = {
                        val digits = it.filter { char -> char.isDigit() }
                        rawPrice = digits
                        if (isPriceError && (digits.toDoubleOrNull() ?: 0.0) > 0) {
                            isPriceError = false
                        }
                    },
                    label = { Text("Precio Unit.") },
                    placeholder = { Text("$0") },
                    isError = isPriceError,
                    supportingText = if (isPriceError) {
                        { Text("Precio > 0") }
                    } else null,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = CurrencyVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.weight(1.2f)
                )

                // Quantity Input
                OutlinedTextField(
                    value = rawQuantity,
                    onValueChange = {
                        rawQuantity = it
                        if (isQuantityError && (it.toDoubleOrNull() ?: 0.0) > 0) {
                            isQuantityError = false
                        }
                    },
                    label = { Text("Cant.") },
                    isError = isQuantityError,
                    supportingText = if (isQuantityError) {
                        { Text("Cant. > 0") }
                    } else null,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    ),
                    modifier = Modifier.weight(0.8f)
                )
            }

            if (subtotalPreview > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Subtotal: ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = subtotalPreview.asCurrency(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    val validName = name.isNotBlank()
                    val validPrice = numericPrice > 0.0
                    val validQty = numericQuantity > 0.0

                    isNameError = !validName
                    isPriceError = !validPrice
                    isQuantityError = !validQty

                    if (validName && validPrice && validQty) {
                        onAddProduct(name, numericPrice, numericQuantity)
                        name = ""
                        rawPrice = ""
                        rawQuantity = "1"
                        nameFocusRequester.requestFocus()
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.AddShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Agregar al Carrito",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
