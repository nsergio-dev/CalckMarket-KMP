package com.nsergiodev.calckmarket.core.data.datasource

import com.nsergiodev.calckmarket.features.home.domain.model.Buy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class LocalBuyDataSource private constructor() {

    private val _buys = MutableStateFlow<List<Buy>>(emptyList())

    fun getBuys(): Flow<List<Buy>> = _buys.asStateFlow()

    fun getBuyById(id: String): Flow<Buy?> = _buys.map { list -> list.firstOrNull { it.id == id } }

    fun saveBuy(buy: Buy) {
        _buys.update { currentList ->
            listOf(buy) + currentList.filter { it.id != buy.id }
        }
    }

    fun deleteBuy(id: String) {
        _buys.update { currentList ->
            currentList.filter { it.id != id }
        }
    }

    companion object {
        val instance: LocalBuyDataSource by lazy { LocalBuyDataSource() }
    }

}
