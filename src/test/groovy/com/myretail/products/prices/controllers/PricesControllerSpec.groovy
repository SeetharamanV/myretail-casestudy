package com.myretail.products.prices.controllers

import com.myretail.products.AbstractSpecification
import com.myretail.products.prices.entities.Price
import com.myretail.products.prices.entities.PricesRequest
import com.myretail.products.prices.entities.PricesResponse
import com.myretail.products.prices.services.PricesService

class PricesControllerSpec extends AbstractSpecification {
    def pricesService = Mock(PricesService)
    def pricesController = new PricesController(pricesService)
    def "Prices Controller test - get by product id - happy path."() {
        given:
        def expectedPriceResponse = GroovyMock(PricesResponse)

        when:
        def result = pricesController.getPricesByProductId(PRODUCT_ID)

        then:
        expectedPriceResponse == result
        1 * pricesService.getPricesByProductId(PRODUCT_ID) >> expectedPriceResponse

        0 * _
    }

    def "Prices Controller test - create prices for product id - happy path."() {
        given:
        def request = GroovyMock(PricesRequest)
        def expectedPriceResponse = GroovyMock(PricesResponse)

        when:
        def result = pricesController.createPricesForProductId(PRODUCT_ID, request)

        then:
        expectedPriceResponse == result
        1 * pricesService.createPricesForProduct(PRODUCT_ID, request) >> expectedPriceResponse

        0 * _
    }

    def "Prices Controller test - update current_price for product id - happy path."() {
        given:
        def request = GroovyMock(Price)
        def priceType = "current_price"
        def expectedPriceResponse = GroovyMock(PricesResponse)

        when:
        def result = pricesController.updatePricesForProductId(PRODUCT_ID, priceType, request)

        then:
        expectedPriceResponse == result
        1 * pricesService.updatePricesForProduct(PRODUCT_ID, priceType, request) >> expectedPriceResponse

        0 * _
    }
}
