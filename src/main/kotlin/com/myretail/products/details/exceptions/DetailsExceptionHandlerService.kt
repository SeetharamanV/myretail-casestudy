package com.myretail.products.details.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class DetailsExceptionHandlerService : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [DetailsNotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleDetailsNotFoundException(ex: DetailsNotFoundException) = ex.toDetailsExceptionResponse()

    @ExceptionHandler(value = [DetailsCallException::class])
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleDetailsCallException(ex: DetailsCallException) = ex.toDetailsExceptionResponse()
}
