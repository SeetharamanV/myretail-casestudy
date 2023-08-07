package com.myretail.products.prices.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class PricesExceptionHandlerService : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [PricesBadRequestException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handlePricesBadRequestException(ex: PricesBadRequestException) = ex.toPricesExceptionResponse()

    @ExceptionHandler(value = [PricesDuplicateRecordException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handlePricesDuplicateRecordException(ex: PricesDuplicateRecordException) = ex.toPricesExceptionResponse()

    @ExceptionHandler(value = [PricesNotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handlePricesNotFoundException(ex: PricesNotFoundException) = ex.toPricesExceptionResponse()

    @ExceptionHandler(value = [PricesDatabaseException::class])
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handlePricesDatabaseException(ex: PricesDatabaseException) = ex.toPricesExceptionResponse()
}
