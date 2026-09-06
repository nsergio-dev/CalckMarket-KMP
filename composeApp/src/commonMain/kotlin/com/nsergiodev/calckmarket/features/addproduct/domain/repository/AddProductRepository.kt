package com.nsergiodev.calckmarket.features.addproduct.domain.repository

import com.nsergiodev.calckmarket.features.home.domain.model.Buy

interface AddProductRepository {
    suspend fun saveBuy(buy: Buy)
}
