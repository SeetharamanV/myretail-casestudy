package com.myretail.products.prices.exceptions

data class PricesExceptionConstant(val code: String, val message: String)

object PricesExceptionConstants {
    val PRICES_NOT_FOUND = PricesExceptionConstant(code = "PRICES-4040", message = "Couldn't find prices for the given product id.")
    val PRICES_DUPLICATE_RECORD_EXCEPTION = PricesExceptionConstant(code = "PRICES-4000", message = "Prices document already exists for product id.")
    val PRICES_DATABASE_EXCEPTION = PricesExceptionConstant(code = "PRICES-5000", message = "Exception occurred while attempting to update prices for product id.")
}
