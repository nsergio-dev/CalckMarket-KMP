package com.nsergiodev.calckmarket.domain.model

import com.nsergiodev.calckmarket.domain.delegates.Buy
import com.nsergiodev.calckmarket.domain.delegates.Product

data class Buy(
    override val marketName: String,
    override val products: List<Product>
): Buy
