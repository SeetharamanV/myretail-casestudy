package com.myretail.products.details.entities

import com.fasterxml.jackson.annotation.JsonInclude

data class AttributesResponse(
    val productId: Long,
    val attributes: Attributes
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Attributes(
    val name: String?,
    val department: String?,
    val unitOfMeasure: String?
)
