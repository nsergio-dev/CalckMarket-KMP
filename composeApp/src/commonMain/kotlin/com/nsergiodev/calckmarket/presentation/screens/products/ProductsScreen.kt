package com.nsergiodev.calckmarket.presentation.screens.products

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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nsergiodev.calckmarket.core.utils.asCurrency
import com.nsergiodev.calckmarket.presentation.screens.products.new_product.ui.NewProductSheet

data class ProductItemUi(
    val id: String,
    val name: String,
    val price: Int,
    val unit: String,
    val updatedAt: String,
    val isFavorite: Boolean,
    val store: String
) {
    val priceFormatted: String get() = price.asCurrency()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    onAddClick: () -> Unit = {}
) {
    var search by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var sortExpanded by remember { mutableStateOf(false) }
    var selectedSort by remember { mutableStateOf("Recientes") }
    var showNewProduct by remember { mutableStateOf(false) }

    var products by remember {
        mutableStateOf(
            listOf(
                ProductItemUi("1", "Sal", 2500, "lb", "19/2/2026", false, store = "D1"),
                ProductItemUi("2", "Leche", 3200, "L", "19/2/2026", false, store = "Éxito"),
                ProductItemUi("3", "Huevos", 8500, "docena", "19/2/2026", false, store = "Carrefour"),
                ProductItemUi("4", "Arroz", 4800, "kg", "17/2/2026", true, store = "Jumbo"),
                ProductItemUi("5", "Pan", 2000, "unidad", "15/2/2026", false, store = "D1")
            )
        )
    }

    var editing by remember { mutableStateOf<ProductItemUi?>(null) }

    val categories = listOf("Todos", "Éxito", "Carrefour", "Jumbo", "Favoritos")
    val sortOptions = listOf("Recientes", "Nombre", "Precio")

    val filtered = remember(products, search, selectedCategory, selectedSort) {
        val query = search.trim()

        var list = products

        // filtro por categoría
        list = when (selectedCategory) {
            "Favoritos" -> list.filter { it.isFavorite }
            "Todos" -> list
            else -> list.filter { it.store == selectedCategory }
        }

        // filtro por búsqueda
        if (query.isNotEmpty()) {
            list = list.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        // orden
        list = when (selectedSort) {
            "Nombre" -> list.sortedBy { it.name.lowercase() }
            "Precio" -> list.sortedBy { it.price }
            else -> list // "Recientes" (si luego tienes fecha real, ordenas por fecha)
        }

        list
    }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Productos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    actions = {
                        IconButton(onClick = { showNewProduct = true }) {
                            Icon(Icons.Outlined.Add, contentDescription = "Agregar producto")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar productos...") },
                    leadingIcon = { Icon(Icons.Outlined.Search, null) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        val selected = selectedCategory == category
                        AssistChip(
                            onClick = { selectedCategory = category },
                            label = { Text(category, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (selected) MaterialTheme.colorScheme.secondaryContainer
                                else MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = if (selected) MaterialTheme.colorScheme.onSecondaryContainer
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            item {
                ExposedDropdownMenuBox(
                    expanded = sortExpanded,
                    onExpandedChange = { sortExpanded = !sortExpanded }
                ) {
                    OutlinedTextField(
                        value = "Ordenar: $selectedSort",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(0.44f),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sortExpanded) },
                        shape = RoundedCornerShape(16.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = sortExpanded,
                        onDismissRequest = { sortExpanded = false }
                    ) {
                        sortOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedSort = option
                                    sortExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            items(
                items = filtered,
                key = { it.id }
            ) { product ->
                ProductCard(
                    product = product,
                    onFavoriteClick = {
                        products = products.map {
                            if (it.id == product.id) it.copy(isFavorite = !it.isFavorite) else it
                        }
                    },
                    onEditClick = {
                        editing = product
                    }
                )
            }

            item {
                if (filtered.isEmpty()) {
                    Text(
                        text = "No hay productos para mostrar",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        NewProductSheet(
            show = showNewProduct,
            onDismiss = { showNewProduct = false },
            onConfirm = { newItem ->
                showNewProduct = false

                val newId = (products.size + 1).toString()

                products = listOf(
                    ProductItemUi(
                        id = newId,
                        name = newItem.name,
                        price = newItem.price,
                        unit = newItem.unit,
                        updatedAt = "Hoy",
                        isFavorite = false,
                        store = if (newItem.store.isBlank()) "D1" else newItem.store
                    )
                ) + products
            }
        )
    }

    EditProductDialog(
        product = editing,
        onDismiss = { editing = null },
        onSave = { updated ->
            products = products.map { if (it.id == updated.id) updated else it }
            editing = null
        }
    )
}

@Composable
private fun ProductCard(
    product: ProductItemUi,
    onFavoriteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Inventory2,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = product.priceFormatted,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = " / ${product.unit}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "Último cambio: ${product.updatedAt}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (product.isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Favorito",
                        tint = if (product.isFavorite) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun EditProductDialog(
    product: ProductItemUi?,
    onDismiss: () -> Unit,
    onSave: (ProductItemUi) -> Unit
) {
    if (product == null) return

    var name by remember(product.id) { mutableStateOf(product.name) }
    var price by remember(product.id) { mutableStateOf(product.price.toString()) }
    var unit by remember(product.id) { mutableStateOf(product.unit) }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar producto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it.filter(Char::isDigit) },
                    label = { Text("Precio") },
                    prefix = { Text("$") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Unidad") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val newPrice = price.toIntOrNull() ?: product.price
                    onSave(product.copy(name = name.trim(), price = newPrice, unit = unit.trim()))
                }
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

