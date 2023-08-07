package com.myretail.products.prices.controllers

import com.myretail.products.AbstractSpecification
import com.myretail.products.attributes.domains.AttributeType
import com.myretail.products.attributes.entities.Attributes
import com.myretail.products.attributes.entities.AttributesDocument
import com.myretail.products.attributes.exceptions.AttributesBadRequestException
import com.myretail.products.attributes.exceptions.AttributesExceptionConstants
import com.myretail.products.attributes.services.AttributeExclusionsHandler

class AttributeExclusionsHandlerSpec extends AbstractSpecification {
    def attributeExtractor = new AttributeExclusionsHandler()

    def "Attribute Extractor - Invalid attribute exclude passed."() {
        when:
        attributeExtractor.extractExclusions("name, department, times")

        then: "Validate exception type and error messages"
        def thrown = thrown(AttributesBadRequestException)
        AttributesExceptionConstants.INVALID_ATTRIBUTE_EXCLUDES_PASSED.message == thrown.message

        0 * _
    }

    def "Attribute Extractor - happy path."() {
        when:
        def result = attributeExtractor.extractExclusions("name, DEPARTMENT")

        then: "matches the excludes"
        [AttributeType.NAME, AttributeType.DEPARTMENT] == result

        0 * _
    }

    def "Attribute Extractor - empty exclude passed."() {
        when:
        def result = attributeExtractor.extractExclusions("")

        then: "empty"
        result.isEmpty()

        0 * _
    }

    def "Attribute Apply exclusion - empty exclude passed."() {
        when:
        def attributes = new Attributes("Acme Glue", "Home", "pounds")
        def attributesDocument = new AttributesDocument(123, attributes)
        def result = attributeExtractor.applyExclusions(attributesDocument, [])

        then: "empty"
        attributesDocument == result

        0 * _
    }

    def "Attribute Apply exclusion - name + department exclusions passed."() {
        when:
        def attributes = new Attributes("Acme Glue", "Home", "pounds")
        def attributesDocument = new AttributesDocument(123, attributes)
        def result = attributeExtractor.applyExclusions(attributesDocument, [AttributeType.NAME, AttributeType.DEPARTMENT])

        then: "empty"
        !result.attributes.name
        !result.attributes.department
        "pounds" == result.attributes.unitOfMeasure

        0 * _
    }

    def "Attribute Apply exclusion - name + department + unit of measure exclusions passed."() {
        when:
        def attributes = new Attributes("Acme Glue", "Home", "pounds")
        def attributesDocument = new AttributesDocument(123, attributes)
        def result = attributeExtractor.applyExclusions(attributesDocument, [AttributeType.NAME, AttributeType.DEPARTMENT, AttributeType.UNIT_OF_MEASURE])

        then: "empty"
        !result.attributes.name
        !result.attributes.department
        !result.attributes.unitOfMeasure

        0 * _
    }

    def "Attribute Apply exclusion - random exclusions passed."() {
        when:
        def attributes = new Attributes("Acme Glue", "Home", "pounds")
        def attributesDocument = new AttributesDocument(123, attributes)
        def result = attributeExtractor.applyExclusions(attributesDocument, ["random"])

        then: "empty"
        "Acme Glue" == result.attributes.name
        "Home" == result.attributes.department
        "pounds" == result.attributes.unitOfMeasure

        0 * _
    }


}
