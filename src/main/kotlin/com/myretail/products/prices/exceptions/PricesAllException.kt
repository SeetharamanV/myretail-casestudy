package com.myretail.products.prices.exceptions

import com.myretail.products.prices.exceptions.PricesExceptionConstants.PRICES_DATABASE_EXCEPTION
import com.myretail.products.prices.exceptions.PricesExceptionConstants.PRICES_DUPLICATE_RECORD_EXCEPTION
import com.myretail.products.prices.exceptions.PricesExceptionConstants.PRICES_NOT_FOUND

open class PricesException(
    override val message: String,
    private val code: String,
    override val cause: Throwable? = null
) : RuntimeException("$code - $message") {
    override fun toString(): String {
        return "AttributesException(message='$message', code='$code', cause=$cause)"
    }

    fun toPricesExceptionResponse(): PricesExceptionResponse {
        return PricesExceptionResponse(code = code, message = message)
    }
}

open class PricesBadRequestException(cause: Throwable?) :
    PricesException(code = "Bad request", message = "Oh no bad request", cause = cause)

open class PricesNotFoundException(code: String = PRICES_NOT_FOUND.code) :
    PricesException(code = code, message = PRICES_NOT_FOUND.message)

open class PricesDatabaseException(code: String = PRICES_DATABASE_EXCEPTION.code, message: String = PRICES_DATABASE_EXCEPTION.message, cause: Throwable? = null) :
    PricesException(code = code, message = message, cause = cause)

open class PricesDuplicateRecordException(cause: Throwable?) :
    PricesException(code = PRICES_DUPLICATE_RECORD_EXCEPTION.code, message = PRICES_DUPLICATE_RECORD_EXCEPTION.message, cause = cause)
