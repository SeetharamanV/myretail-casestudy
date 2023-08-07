package com.myretail.products.prices.controllers

import com.myretail.products.AbstractSpecification
import com.myretail.products.attributes.controllers.AttributesController
import com.myretail.products.attributes.entities.AttributesDocument
import com.myretail.products.attributes.services.AttributesService

class AttributesControllerSpec extends AbstractSpecification {
    def attributesService = Mock(AttributesService)
    def attributesController = new AttributesController(attributesService)
    def "Attributes Controller test - get for one product."() {
        given:
        def attributesDocument = GroovyMock(AttributesDocument)
        def excludes = "name"

        when:
        def result = attributesController.getAttributesByProductId(123, excludes)

        then:
        !result
        1 * attributesService.process(123, excludes) >> attributesDocument
        attributesDocument == result

        0 * _
    }
}
