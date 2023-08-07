package com.myretail.products.attributes.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class AttributesExceptionHandlerService : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [AttributesBadRequestException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAttributesBadRequestException(ex: AttributesBadRequestException) = ex.toAttributeExceptionResponse()
}
