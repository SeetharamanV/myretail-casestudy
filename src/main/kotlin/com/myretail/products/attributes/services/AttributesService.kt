package com.myretail.products.attributes.services

import com.myretail.products.attributes.entities.AttributesDocument
import com.myretail.products.attributes.repositories.AttributesRepository
import org.springframework.stereotype.Service

@Service
class AttributesService(private val attributeExclusionsHandler: AttributeExclusionsHandler, private val attributesRepository: AttributesRepository) {
    fun process(productId: Long, excludes: String): AttributesDocument {
        val excludeList = attributeExclusionsHandler.extractExclusions(excludes)
        val attributesDocument = attributesRepository.getAttributesDocumentByProductId(productId)
        return attributeExclusionsHandler.applyExclusions(attributesDocument, excludeList)
    }
}
