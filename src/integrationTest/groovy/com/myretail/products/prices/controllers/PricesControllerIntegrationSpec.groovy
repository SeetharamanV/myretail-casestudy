package com.myretail.products.prices.controllers

import com.myretail.products.AbstractRestIntegrationSpecification
import org.springframework.test.web.servlet.MvcResult

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals

class PricesControllerIntegrationSpec extends AbstractRestIntegrationSpecification {

    def "Create Prices for a product id."() {
        given:
        String newPrices = """{
            "prices":{
                "current_price":{
                    "value":1.99,
                    "currency_code":"USD"
                },
                "regular_price":{
                    "value":1.99,
                    "currency_code":"USD"
                },
                "initial_price":{
                    "value":1.99,
                    "currency_code":"USD"
                }
            }
        }""" as String

        String expectedResponse = """{
            "product_id":$PRODUCT_ID,
            "prices":{
                "current_price":{
                    "value":1.99,
                    "currency_code":"USD"
                },
                "regular_price":{
                    "value":1.99,
                    "currency_code":"USD"
                },
                "initial_price":{
                    "value":1.99,
                    "currency_code":"USD"
                }
            }
        }""" as String

        when:
        MvcResult result = mockPost("/prices/v1/products/$PRODUCT_ID?key=testkey1","Bearer `testtoken`", newPrices).andReturn()

        then:
        result.response.status == 201
        assertJsonEquals(expectedResponse.toString(), result.response.contentAsString)

        0 * _
    }

    def "Get existing Prices for a product id."() {
        given:
        saveTestPrices()
        String expectedResponse = """{
            "product_id":$PRODUCT_ID,
            "prices":{
                "current_price":{
                    "value":1.99,
                    "currency_code":"USD"
                },
                "regular_price":{
                    "value":1.99,
                    "currency_code":"USD"
                },
                "initial_price":{
                    "value":1.99,
                    "currency_code":"USD"
                }
            }
        }""" as String

        when:
        MvcResult result = mockGet("/prices/v1/products/$PRODUCT_ID?key=testkey1","Bearer `testtoken`").andReturn()

        then:
        result.response.status == 200
        assertJsonEquals(expectedResponse.toString(), result.response.contentAsString)

        0 * _
    }

    def "Update current_price for a product id."() {
        given:
        def savedPrices = saveTestPrices()
        def updatedCurrentPrice = 19.99
        String request = """{
            "value":$updatedCurrentPrice,
            "currency_code":"USD"
        }""" as String

        String expectedResponse = """{
            "product_id":$PRODUCT_ID,
            "prices":{
                "current_price":{
                    "value":$updatedCurrentPrice,
                    "currency_code":"USD"
                },
                "regular_price":{
                    "value":1.99,
                    "currency_code":"USD"
                },
                "initial_price":{
                    "value":1.99,
                    "currency_code":"USD"
                }
            }
        }""" as String

        when:
        MvcResult result = mockPut("/prices/v1/products/$PRODUCT_ID/price_types/current_price?key=testkey1","Bearer `testtoken`", request).andReturn()

        then:
        updatedCurrentPrice != savedPrices.prices.currentPrice.value
        result.response.status == 200
        assertJsonEquals(expectedResponse.toString(), result.response.contentAsString)

        0 * _
    }

    def "Test Price Core Controller - Bad Request "() {
        when:
        MvcResult result = mockGet("/prices/v1/products/ABC?key=testkey1","Bearer `testtoken`").andReturn()

        then:
        result.response.status == 400

        0 * _
    }
}
