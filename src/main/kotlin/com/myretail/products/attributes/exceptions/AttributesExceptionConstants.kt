package com.myretail.products.attributes.exceptions

data class AttributesExceptionConstant(val code: String, val message: String)

object AttributesExceptionConstants {
    val INVALID_ATTRIBUTE_EXCLUDES_PASSED = AttributesExceptionConstant(code = "ATTRIBUTES-4000", message = "Invalid attribute type passed for 'exclude'.")
}
