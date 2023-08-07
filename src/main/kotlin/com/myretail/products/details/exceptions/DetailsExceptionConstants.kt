package com.myretail.products.details.exceptions

data class DetailsExceptionConstant(val code: String, val message: String)

object DetailsExceptionConstants {
    val DETAILS_PRICES_NOT_FOUND = DetailsExceptionConstant(code = "DETAILS-4040", message = "Couldn't find prices for the given product id.")
    val DETAILS_ATTRIBUTES_NOT_FOUND = DetailsExceptionConstant(code = "DETAILS-40401", message = "Couldn't find attributes for the given product id.")
    val DETAILS_PRICES_CALL_ERROR = DetailsExceptionConstant(code = "DETAILS-5000", message = "Call failure to get prices for the given product id.")
    val DETAILS_ATTRIBUTES_CALL_ERROR = DetailsExceptionConstant(code = "DETAILS-5001", message = "Call failure to get attributes for the given product id.")
}
