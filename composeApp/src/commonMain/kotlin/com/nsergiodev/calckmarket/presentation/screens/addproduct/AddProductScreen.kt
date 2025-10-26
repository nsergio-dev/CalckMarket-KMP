package com.nsergiodev.calckmarket.presentation.screens.addproduct

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.nsergiodev.calckmarket.core.utils.CurrencyVisualTransformation
import com.nsergiodev.calckmarket.core.utils.asCurrency
import com.nsergiodev.calckmarket.core.utils.isEmptySafe
import com.nsergiodev.calckmarket.domain.model.Buy
import com.nsergiodev.calckmarket.domain.model.ProductAdd
import com.nsergiodev.calckmarket.presentation.screens.home.dummyCurrentBuys
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onBackClick: () -> Unit = {},
    onPayClick: () -> Unit = {}
) {

    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val listStateProductState = rememberLazyListState()

    var listProducts by remember {
        mutableStateOf(
            emptyList<ProductAdd>()
        )
    }

    val valueToPay by derivedStateOf {
        listProducts.sumOf { it.total }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            if (listProducts.isNotEmpty()) {
                listStateProductState.animateScrollToItem(listProducts.lastIndex)
            }
        }
    }

    var marketName by remember { mutableStateOf("") }
    var showDialogSureToLeave by remember { mutableStateOf(false) }
    var showDialogEmptyProducts by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mercando en $marketName") },
                navigationIcon = {
                    IconButton(
                        onClick = { showDialogSureToLeave = true }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            keyboard?.hide()
                            focusManager.clearFocus()
                        }
                    )
                }
        ) {

            MarketNameDialog(
                showDialog = marketName.isEmpty(),
                onDismiss = { marketName = "" },
                onConfirm = { marketName = it }
            )

            Form(
                keyboard,
                focusManager,
                onAddClick = {
                    listProducts = listProducts + it
                    coroutineScope.launch {
                        if (listProducts.isNotEmpty()) {
                            listStateProductState.animateScrollToItem(listProducts.lastIndex)
                        }
                    }
                }
            )

            Products(listProducts, listStateProductState)

            Footer(
                valueToPay = valueToPay,
                onPayClick = {
                    if (listProducts.isNotEmpty()) {
                        showDialogSureToLeave = true
                    } else {
                        showDialogEmptyProducts = true
                    }
                }
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
                    dummyCurrentBuys.add(
                        Buy(
                            marketName = marketName,
                            products = listProducts
                        )
                    )
                    listProducts = emptyList()
                    onPayClick.invoke()
                }
            )

        }
    }
}

@Composable
fun EmptyProductsDialog(
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
fun MarketNameDialog(
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
private fun Form(
    keyboard: SoftwareKeyboardController?,
    focusManager: FocusManager,
    onAddClick: (ProductAdd) -> Unit
) {

    var nameProduct by remember { mutableStateOf("") }
    var priceProduct by remember { mutableStateOf("") }
    var quantityProduct by remember { mutableStateOf("") }

    var isNameProductInvalid by remember { mutableStateOf(false) }
    var isPriceProductInvalid by remember { mutableStateOf(false) }
    var isQuantityProductInvalid by remember { mutableStateOf(false) }

    val totalProduct by remember {
        derivedStateOf {
            val quantity = quantityProduct.toDoubleOrNull() ?: 0.0
            val priceProductToDouble = priceProduct.toDoubleOrNull() ?: 0.0
            priceProductToDouble * quantity
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            NameProductInput(
                nameProduct,
                isNameProductInvalid = isNameProductInvalid,
                onChangeIsInValid = { isNameProductInvalid = it },
                onNameProductChange = { nameProduct = it }
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PriceInput(
                    priceProduct = priceProduct,
                    modifier = Modifier.weight(1f),
                    isPriceProductInvalid = isPriceProductInvalid,
                    onChangeIsInValid = { isPriceProductInvalid = it },
                    onPriceProductChange = { priceProduct = it }
                )

                QuantityInput(
                    quantityProduct = quantityProduct,
                    modifier = Modifier.weight(1f),
                    isQuantityProductInvalid = isQuantityProductInvalid,
                    onChangeIsInValid = { isQuantityProductInvalid = it },
                    onQuantityProductChange = { quantityProduct = it },
                    keyboard = keyboard,
                    focusManager = focusManager
                )
            }

            Spacer(Modifier.height(16.dp))

            TotalInput(
                modifier = Modifier.fillMaxWidth(),
                totalProduct = totalProduct.asCurrency()
            )

            Spacer(Modifier.height(16.dp))
            AddProductButton(

                onAddClick = {

                    isPriceProductInvalid = (priceProduct.toDoubleOrNull() ?: 0.0) == 0.0
                    isQuantityProductInvalid = !quantityProduct.isEmptySafe()
                    isNameProductInvalid = !nameProduct.isEmptySafe()

                    val isValidForm: Boolean = checkIsValidInputs(
                        isNameProductInvalid = isNameProductInvalid,
                        isPriceProductInvalid = isPriceProductInvalid,
                        isQuantityProductInvalid = isQuantityProductInvalid
                    )

                    if (isValidForm) {
                        val product = ProductAdd(
                            id = (dummyCurrentBuys.count() + 1).toLong(),
                            name = nameProduct,
                            price = priceProduct.toDoubleOrNull() ?: 0.0,
                            quantity = quantityProduct.toDoubleOrNull() ?: 0.0,
                            total = totalProduct
                        )

                        nameProduct = ""
                        priceProduct = ""
                        quantityProduct = ""
                        isPriceProductInvalid = false
                        isQuantityProductInvalid = false
                        isNameProductInvalid = false
                        keyboard?.hide()
                        focusManager.clearFocus()
                        onAddClick(product)
                    }
                }
            )
        }

    }
}

@Composable
fun TotalInput(modifier: Modifier, totalProduct: String) {
    OutlinedTextField(
        modifier = modifier,
        value = totalProduct.asCurrency(),
        onValueChange = {},
        label = { Text("Total") },
        enabled = false,
        singleLine = true
    )
}

private fun checkIsValidInputs(
    isNameProductInvalid: Boolean,
    isPriceProductInvalid: Boolean,
    isQuantityProductInvalid: Boolean
): Boolean {
    return !isNameProductInvalid && !isPriceProductInvalid && !isQuantityProductInvalid
}

@Composable
fun QuantityInput(
    quantityProduct: String,
    modifier: Modifier,
    isQuantityProductInvalid: Boolean,
    onChangeIsInValid: (Boolean) -> Unit,
    onQuantityProductChange: (String) -> Unit,
    keyboard: SoftwareKeyboardController?,
    focusManager: FocusManager,
) {

    var isInvalidInput by remember {
        mutableStateOf(isQuantityProductInvalid)
    }

    OutlinedTextField(
        modifier = modifier,
        value = quantityProduct,
        onValueChange = {

            onQuantityProductChange(it)
            if (isInvalidInput) {
                isInvalidInput = !it.isEmptySafe()
            }
            onChangeIsInValid.invoke(isInvalidInput)
        },
        label = { Text("Cantidad") },
        singleLine = true,
        isError = isQuantityProductInvalid,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboard?.hide()
                focusManager.clearFocus()
            }
        )
    )
}

@Composable
private fun NameProductInput(
    nameProduct: String,
    isNameProductInvalid: Boolean,
    onChangeIsInValid: (Boolean) -> Unit,
    onNameProductChange: (String) -> Unit
) {

    var isInvalidInput by remember {
        mutableStateOf(isNameProductInvalid)
    }

    OutlinedTextField(
        value = nameProduct,
        onValueChange = {
            onNameProductChange(it)
            if (isInvalidInput) {
                isInvalidInput = !it.isEmptySafe()
            }
            onChangeIsInValid.invoke(isInvalidInput)
        },
        label = { Text("Nombre del producto") },
        placeholder = { Text("Ej. Arroz Diana") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = isNameProductInvalid,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { }
        )
    )
}


@Composable
fun ColumnScope.AddProductButton(
    onAddClick: () -> Unit
) {


    Button(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        onClick = onAddClick
    ) {
        Text("Agregar producto")
    }

}

@Composable
private fun ColumnScope.Products(
    products: List<ProductAdd>,
    listStateProductState: LazyListState
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        if (products.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay productos agregados")
            }
        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listStateProductState,
                verticalArrangement = Arrangement.spacedBy(14.dp),
                reverseLayout = true
            ) {
                items(products) { item ->
                    ItemProductList(item)
                }
            }
        }
    }
}

@Composable
private fun ItemProductList(item: ProductAdd) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        ListItem(
            modifier = Modifier,
            headlineContent = { Text(item.name) },
            supportingContent = {
                Text("Cant: ${item.quantity} — ${item.price.asCurrency()}")
            },
            trailingContent = {
                Text(
                    item.total.asCurrency(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        )
    }
}

@Composable
private fun Footer(
    valueToPay: Double,
    onPayClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = valueToPay.asCurrency(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Button(onClick = onPayClick) {
                Text("Pagar")
            }
        }
    }
}

@Composable
private fun PriceInput(
    modifier: Modifier,
    priceProduct: String,
    isPriceProductInvalid: Boolean,
    onChangeIsInValid: (Boolean) -> Unit,
    onPriceProductChange: (String) -> Unit
) {
    var isInvalidInput by remember {
        mutableStateOf(isPriceProductInvalid)
    }

    OutlinedTextField(
        modifier = modifier,
        value = priceProduct,
        isError = isPriceProductInvalid,
        onValueChange = {

            val valueToDouble = it.filter { char -> char.isDigit() }.toDoubleOrNull() ?: 0.0
            onPriceProductChange(
                it.filter { char -> char.isDigit() }
            )

            if (isInvalidInput) {
                isInvalidInput = valueToDouble <= 0.0
            }
            onChangeIsInValid(isInvalidInput)
        },
        label = { Text("Costo") },
        placeholder = { Text("$0.00") },
        singleLine = true,
        visualTransformation = CurrencyVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
    )
}