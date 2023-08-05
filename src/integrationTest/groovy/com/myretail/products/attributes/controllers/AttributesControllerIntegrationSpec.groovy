package com.myretail.products.attributes.controllers

import com.myretail.products.AbstractRestIntegrationSpecification
import org.springframework.test.web.servlet.MvcResult

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals

class AttributesControllerIntegrationSpec extends AbstractRestIntegrationSpecification {

    def "Test Attributes Core Controller - No 'exclude' passed, returns everything "() {
        given:
        String expectedResponse = """{
            "product_id":1233,
            "attributes":{
                "name":"Acme Glue",
                "department":"Home",
                "unit_of_measure":"pounds"
            }
        }""" as String

        when: "Get attributes for an product"
        MvcResult result = mockGet("/attributes/v1/products/1233?key=testkey1","Bearer `testtoken`").andReturn()

        then:
        result.response.status == 200
        assertJsonEquals(expectedResponse.toString(), result.response.contentAsString)

        0 * _
    }

    def "Test Attributes Core Controller - Bad 'exclude' attribute passed "() {
        given:
        String expectedErrorResponse = """{
            "message":"Invalid attribute type passed for 'exclude'.",
            "code":"ATTRIBUTES-4000"
        }""" as String

        when:
        MvcResult result = mockGet("/attributes/v1/products/1233?excludes=test&key=testkey1","Bearer `testtoken`").andReturn()

        then:
        result.response.status == 400
        assertJsonEquals(expectedErrorResponse.toString(), result.response.contentAsString)

        0 * _
    }

    def "Test Attributes Core Controller - 'exclude' name and department "() {
        given:
        String expectedResponse = """{
            "product_id":1233,
            "attributes":{
                "unit_of_measure":"pounds"
            }
        }""" as String

        when:
        MvcResult result = mockGet("/attributes/v1/products/1233?excludes=name, department&key=testkey1","Bearer `testtoken`").andReturn()

        then:
        result.response.status == 200
        assertJsonEquals(expectedResponse.toString(), result.response.contentAsString)

        0 * _
    }
}
