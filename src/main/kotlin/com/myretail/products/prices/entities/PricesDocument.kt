package com.myretail.products.prices.entities

import com.fasterxml.jackson.annotation.JsonInclude
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "prices")
data class PricesDocument(
    @Id val _id: ObjectId? = null,
    val productId: Long,
    val prices: AllPrices
) {
    fun toPricesResponse(): PricesResponse = PricesResponse(this.productId, this.prices)
}

data class PricesRequest(
    val prices: AllPrices
)

data class PricesResponse(
    val productId: Long,
    val prices: AllPrices
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AllPrices(
    val currentPrice: Price? = null,
    val regularPrice: Price? = null,
    val initialPrice: Price? = null
)

data class Price(
    val value: Double,
    val currencyCode: String
)

enum class PricesDocumentFields(val fieldName: String) {
    PRODUCT_ID("productId")
}

enum class PricesDocumentFieldsAndFailureCode(val fieldName: String, val fieldPath: String, val event: String, val failureCode: String) {

    UPDATE_CURRENT_PRICE("current_price", "prices.currentPrice", "UPDATE_CURRENT_PRICE", "Couldn't update CURRENT PRICE"),
    UPDATE_REGULAR_PRICE("regular_price", "prices.regularPrice", "UPDATE_REGULAR_PRICE", "Couldn't update REGULAR PRICE"),
    UPDATE_INITIAL_PRICE("initial_price", "prices.initialPrice", "UPDATE_INITIAL_PRICE", "Couldn't update INITIAL PRICE");

    companion object {
        fun getPricesDocumentFieldsAndFailureCode(priceType: String): PricesDocumentFieldsAndFailureCode {
            return values().first { it.fieldName.uppercase() == priceType.uppercase() }
        }
    }
}
