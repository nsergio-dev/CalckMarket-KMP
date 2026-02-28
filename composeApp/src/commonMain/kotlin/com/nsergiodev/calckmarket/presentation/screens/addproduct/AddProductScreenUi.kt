package com.nsergiodev.calckmarket.presentation.screens.addproduct

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nsergiodev.calckmarket.core.utils.CurrencyVisualTransformation
import com.nsergiodev.calckmarket.core.utils.asCurrency
import com.nsergiodev.calckmarket.presentation.screens.addproduct.models.UiNewProduct
import com.nsergiodev.calckmarket.presentation.screens.addproduct.models.UiProductRow
import com.nsergiodev.calckmarket.presentation.screens.addproduct.models.UiUnit
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreenUi(
    modifier: Modifier,
    onPay: () -> Unit,
) {

    var listProductsUI by remember {
        mutableStateOf(
            emptyList<UiProductRow>()
        )
    }

    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val total = remember(listProductsUI) { listProductsUI.sumOf { it.subtotal } }

    val listStateProductState = rememberLazyListState()

    val productCount = listProductsUI.sumOf { it.qty }

    val nameProductFocus = remember { FocusRequester() }

    Column(
        modifier = modifier
            .padding(horizontal = 14.dp)
    ) {

        NewProductForm(
            modifier = Modifier.fillMaxWidth(),
            productFocus = nameProductFocus,
            onAdd = { new ->
                val newProduct = UiProductRow(
                    id = (listProductsUI.maxOfOrNull { it.id } ?: 0L) + 1L,
                    name = new.name,
                    unitPrice = new.unitPrice,
                    unit = new.unit,
                    qty = new.qty,
                )

                listProductsUI = listProductsUI + newProduct

                coroutineScope.launch {
                    if (listProductsUI.isNotEmpty()) {
                        listStateProductState.animateScrollToItem(listProductsUI.lastIndex)
                    }
                    nameProductFocus(nameProductFocus, keyboardController)
                }
            },
        )

        Spacer(Modifier.height(12.dp))

        ProductsListCard(
            listStateProductState = listStateProductState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            products = listProductsUI,
            onInc = { id ->
                listProductsUI = listProductsUI.map { p ->
                    if (p.id != id) {
                        p
                    } else {
                        val newQty = p.qty + 1
                        p.copy(qty = newQty)
                    }
                }
            },
            onDec = { id ->
                listProductsUI = listProductsUI.map { product ->
                    if (product.id != id) {
                        product
                    } else {
                        val newQty = (product.qty - 1).coerceAtLeast(1)
                        product.copy(qty = newQty)
                    }
                }
            },
            onDelete = { id ->
                listProductsUI = listProductsUI.filterNot { it.id == id }
            },
        )

        Spacer(Modifier.height(10.dp))

        FooterBar(
            modifier = Modifier.fillMaxWidth(),
            productCount = productCount,
            total = total,
            onPay = onPay
        )

        Spacer(Modifier.height(10.dp))

        LaunchedEffect(Unit) {
            nameProductFocus(nameProductFocus, keyboardController)
        }
    }
}

private suspend fun nameProductFocus(
    nameProductFocus: FocusRequester,
    keyboardController: SoftwareKeyboardController?
) {
    delay(300)
    nameProductFocus.requestFocus()
    keyboardController?.show()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewProductForm(
    modifier: Modifier,
    productFocus: FocusRequester,
    onAdd: (UiNewProduct) -> Unit
) {
    val shape = RoundedCornerShape(18.dp)

    var name by remember { mutableStateOf("sal") }
    var qty by remember { mutableStateOf("2") }
    var unit by remember { mutableStateOf(UiUnit.UNIT) }
    var unitPrice by remember { mutableStateOf("3500") }

    ElevatedCard(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Agregar producto",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth()
                    .focusRequester(productFocus),
                value = name,
                onValueChange = { name = it },
                label = { Text("Producto") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                CantNumberField(
                    modifier = Modifier.weight(1.2f),
                    value = qty,
                    uni = unit.short,
                    onValueChange = { qty = it.filter { ch -> ch.isDigit() } }
                )

                UnitDropDown(
                    modifier = Modifier.weight(1.1f),
                    value = unit,
                    onChange = { unit = it }
                )

            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                UnitPriceInput(
                    modifier = Modifier.weight(1.1f),
                    value = unitPrice,
                    uni = unit.short,
                    onValueChange = { raw ->
                        unitPrice = raw.filter { it.isDigit() }
                    }
                )

                TotalPriceInput(
                    modifier = Modifier.weight(1.1f),
                    value = (unitPrice.toIntOrNull() ?: 0) * (qty.toIntOrNull() ?: 0),
                    onValueChange = { raw ->
                    }
                )

            }

            Spacer(Modifier.height(12.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = {
                    val safeQty = qty.toIntOrNull()?.coerceAtLeast(1) ?: 1
                    val safePrice = unitPrice.toDoubleOrNull() ?: 0.0
                    if (name.isNotBlank() && safePrice > 0.0) {
                        onAdd(
                            UiNewProduct(
                                name = name.trim(),
                                qty = safeQty,
                                unit = unit,
                                unitPrice = safePrice
                            )
                        )
                        name = ""
                        qty = ""
                        unit = UiUnit.UNIT
                        unitPrice = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Agregar", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun CantNumberField(
    modifier: Modifier,
    value: String,
    uni: String,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        modifier = modifier,
        value = value,
        label = { Text("Cant / $uni") },
        placeholder = { Text("$ 100") },
        onValueChange = onValueChange,
        singleLine = true,
        shape = RoundedCornerShape(14.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnitDropDown(
    modifier: Modifier,
    value: UiUnit,
    onChange: (UiUnit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier) {

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                value = value.label,
                label = { Text("Kg, Lb, Uni...") },
                onValueChange = {},
                singleLine = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(14.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                UiUnit.entries.forEach { u ->
                    DropdownMenuItem(
                        text = { Text(u.label) },
                        onClick = {
                            onChange(u)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun UnitPriceInput(
    modifier: Modifier,
    value: String,
    uni: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            label = { Text("Precio / $uni") },
            visualTransformation = CurrencyVisualTransformation(),
            placeholder = { Text("9999") },
            shape = RoundedCornerShape(14.dp)
        )
    }
}

@Composable
private fun TotalPriceInput(
    modifier: Modifier,
    value: Int,
    onValueChange: (String) -> Unit
) {
    Column(modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value.asCurrency(),
            enabled = false,
            label = { Text("Total") },
            onValueChange = onValueChange,
            singleLine = true,
            visualTransformation = CurrencyVisualTransformation(),
            placeholder = { Text("$ 999.999") },
            shape = RoundedCornerShape(14.dp)
        )
    }
}

/* ------------------------------- Products list ------------------------------ */

@Composable
private fun ProductsListCard(
    modifier: Modifier,
    products: List<UiProductRow>,
    onInc: (id: Long) -> Unit,
    onDec: (id: Long) -> Unit,
    onDelete: (id: Long) -> Unit,
    listStateProductState: LazyListState
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        if (products.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No hay productos agregados",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp),
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                state = listStateProductState
            ) {
                items(products, key = { it.id }) { item ->
                    ProductRow(
                        item = item,
                        onInc = { onInc(item.id) },
                        onDec = { onDec(item.id) },
                        onDelete = { onDelete(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductRow(
    item: UiProductRow,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onDelete: () -> Unit
) {
    val amountColor = MaterialTheme.colorScheme.primary

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                QtyStepper(
                    qty = item.qty,
                    onInc = onInc,
                    onDec = onDec
                )

                Spacer(Modifier.width(14.dp))

                AssistChip(
                    onClick = {},
                    label = { Text(item.unit.label) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    /*                    border = AssistChipDefaults.assistChipBorder(
                                            borderColor = MaterialTheme.colorScheme.outlineVariant
                                        )*/
                )

                Spacer(Modifier.width(10.dp))
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = "${item.unitPrice.asCurrency()} / ${item.unit.short}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxSize()
            )

            Spacer(Modifier.height(10.dp))

            Divider(color = MaterialTheme.colorScheme.outlineVariant)

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Subtotal",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = item.subtotal.asCurrency(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = amountColor
                )
            }
        }
    }
}

@Composable
private fun QtyStepper(
    qty: Int,
    onInc: () -> Unit,
    onDec: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        StepCircleButton(icon = Icons.Default.Remove, onClick = onDec)
        Spacer(Modifier.width(10.dp))
        Text(text = qty.toString(), fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.width(10.dp))
        StepCircleButton(icon = Icons.Default.Add, onClick = onInc)
    }
}

@Composable
private fun StepCircleButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Icon(icon, contentDescription = null)
    }
}

/* --------------------------------- Footer --------------------------------- */

@Composable
private fun FooterBar(
    modifier: Modifier,
    productCount: Int,
    total: Double,
    onPay: () -> Unit
) {
    val amountColor = MaterialTheme.colorScheme.primary

    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$productCount productos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = total.asCurrency(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
        }

        Spacer(Modifier.height(8.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            onClick = onPay,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Finalizar / Pagar", fontWeight = FontWeight.SemiBold)
        }
    }
}



