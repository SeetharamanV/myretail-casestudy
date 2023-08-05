package com.myretail.products.prices.controllers

import com.myretail.products.AbstractSpecification
import com.myretail.products.attributes.domains.AttributeType
import com.myretail.products.attributes.entities.AttributesDocument
import com.myretail.products.attributes.repositories.AttributesRepository
import com.myretail.products.attributes.services.AttributeExclusionsHandler
import com.myretail.products.attributes.services.AttributesService

class AttributesServiceSpec extends AbstractSpecification {
    def attributeExclusionsHandler = Mock(AttributeExclusionsHandler)
    def attributesRepository = Mock(AttributesRepository)
    def attributesService = new AttributesService(attributeExclusionsHandler, attributesRepository)
    def "Attributes Service test - process for one product."() {
        given:
        def attributesDocument = GroovyMock(AttributesDocument)
        def excludes = "name"

        when:
        def result = attributesService.process(123, excludes)

        then:
        1 * attributeExclusionsHandler.extractExclusions(excludes) >> [AttributeType.NAME]
        1 * attributesRepository.getAttributesDocumentByProductId(123) >> attributesDocument
        1 * attributeExclusionsHandler.applyExclusions(attributesDocument, [AttributeType.NAME]) >> attributesDocument
        attributesDocument == result

        0 * _
    }
}
