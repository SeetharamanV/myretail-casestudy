package com.myretail.products.details.services

import com.myretail.products.details.entities.DetailsDocument
import org.springframework.stereotype.Component

@Component
class DetailsService(private val pricesCoreService: PricesCoreService, private val attributesCoreService: AttributesCoreService) {
    fun getDetailsForProductId(productId: Long, excludes: String): DetailsDocument {
        val prices = pricesCoreService.getPricesForProductId(productId).prices
        val attributes = attributesCoreService.getAttributesForProductId(productId, excludes).attributes
        return DetailsDocument(productId, attributes.name, prices.currentPrice)
    }
}
