package com.myretail.products.details.exceptions

open class DetailsException(
    override val message: String,
    private val code: String,
    override val cause: Throwable? = null
) : RuntimeException("$code - $message") {
    override fun toString(): String {
        return "DetailsException(message='$message', code='$code', cause=$cause)"
    }

    fun toDetailsExceptionResponse(): DetailsExceptionResponse {
        return DetailsExceptionResponse(code = code, message = message)
    }
}

open class DetailsNotFoundException(code: String, message: String) :
    DetailsException(code = code, message = message)

open class DetailsCallException(code: String, message: String) :
    DetailsException(code = code, message = message)
