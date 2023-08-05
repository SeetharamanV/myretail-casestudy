package com.myretail.products.attributes.entities

import com.fasterxml.jackson.annotation.JsonInclude

data class AttributesDocument(
    val productId: Long,
    val attributes: Attributes
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Attributes(
    val name: String?,
    val department: String?,
    val unitOfMeasure: String?
)
