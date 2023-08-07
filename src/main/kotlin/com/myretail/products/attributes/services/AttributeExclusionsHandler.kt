package com.myretail.products.attributes.services

import com.myretail.products.attributes.domains.AttributeType
import com.myretail.products.attributes.entities.Attributes
import com.myretail.products.attributes.entities.AttributesDocument
import com.myretail.products.attributes.exceptions.AttributesBadRequestException
import org.springframework.stereotype.Service

@Service
class AttributeExclusionsHandler {
    fun extractExclusions(excludes: String): List<AttributeType> {
        if (excludes.isNullOrBlank()) return arrayListOf()
        try {
            return excludes.replace(" ", "").split(",")
                .map { p -> AttributeType.valueOf(p.uppercase()) }
                .toMutableList()
        } catch (ex: IllegalArgumentException) {
            throw AttributesBadRequestException(ex)
        }
    }

    fun applyExclusions(attributesDocument: AttributesDocument, excludeList: List<AttributeType>): AttributesDocument {
        if (excludeList.isNullOrEmpty()) return attributesDocument

        val name = if (AttributeType.NAME in excludeList) null else attributesDocument.attributes.name
        val department = if (AttributeType.DEPARTMENT in excludeList) null else attributesDocument.attributes.department
        val unitOfMeasure = if (AttributeType.UNIT_OF_MEASURE in excludeList) null else attributesDocument.attributes.unitOfMeasure
        return attributesDocument.copy(
            attributes = Attributes(name, department, unitOfMeasure)
        )
    }
}
