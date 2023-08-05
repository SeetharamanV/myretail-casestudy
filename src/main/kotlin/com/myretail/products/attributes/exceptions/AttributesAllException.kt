package com.myretail.products.attributes.exceptions

import com.myretail.products.attributes.exceptions.AttributesExceptionConstants.INVALID_ATTRIBUTE_EXCLUDES_PASSED

open class AttributesException(
    override val message: String,
    private val code: String,
    override val cause: Throwable? = null
) : RuntimeException("$code - $message") {
    override fun toString(): String {
        return "AttributesException(message='$message', code='$code', cause=$cause)"
    }
    fun toAttributeExceptionResponse(): AttributeExceptionResponse {
        return AttributeExceptionResponse(code = code, message = message)
    }
}

open class AttributesBadRequestException(cause: Throwable?) :
    AttributesException(code = INVALID_ATTRIBUTE_EXCLUDES_PASSED.code, message = INVALID_ATTRIBUTE_EXCLUDES_PASSED.message, cause = cause)
