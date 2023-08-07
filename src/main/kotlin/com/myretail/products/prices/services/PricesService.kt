package com.myretail.products.prices.services

import com.myretail.products.prices.entities.Price
import com.myretail.products.prices.entities.PricesDocument
import com.myretail.products.prices.entities.PricesDocumentFieldsAndFailureCode
import com.myretail.products.prices.entities.PricesRequest
import com.myretail.products.prices.entities.PricesResponse
import com.myretail.products.prices.exceptions.PricesDuplicateRecordException
import com.myretail.products.prices.exceptions.PricesNotFoundException
import com.myretail.products.prices.repositories.PricesRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service

@Service
class PricesService(private val pricesRepository: PricesRepository) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getPricesByProductId(productId: Long): PricesResponse {
        val productDocument = pricesRepository.findByProductId(productId) ?: throw PricesNotFoundException()
        return productDocument.toPricesResponse()
    }

    fun createPricesForProduct(
        productId: Long,
        pricesRequest: PricesRequest
    ): PricesResponse? {
        try {
            val savedPricesDocument = pricesRepository.save(PricesDocument(productId = productId, prices = pricesRequest.prices))
            return savedPricesDocument.toPricesResponse()
        } catch (duplicateKeyException: DuplicateKeyException) {
            throw PricesDuplicateRecordException(duplicateKeyException)
        }
    }

    fun updatePricesForProduct(
        productId: Long,
        priceType: String,
        price: Price
    ): PricesResponse? {
        val pricesDocumentFields = PricesDocumentFieldsAndFailureCode.getPricesDocumentFieldsAndFailureCode(priceType)
        return pricesRepository.updateSectionsInPricesDocument(
            productId,
            mapOf(pricesDocumentFields.fieldPath to price),
            pricesDocumentFields
        )?.toPricesResponse()
    }
}
