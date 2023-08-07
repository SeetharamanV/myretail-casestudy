package com.myretail.products.prices.services

import com.myretail.products.AbstractSpecification
import com.myretail.products.prices.entities.AllPrices
import com.myretail.products.prices.entities.PricesDocument
import com.myretail.products.prices.entities.PricesDocumentFieldsAndFailureCode
import com.myretail.products.prices.entities.PricesRequest
import com.myretail.products.prices.exceptions.PricesExceptionConstants
import com.myretail.products.prices.exceptions.PricesNotFoundException
import com.myretail.products.prices.repositories.PricesRepository

class PricesServiceSpec extends AbstractSpecification {
    def pricesRepository = Mock(PricesRepository)
    def pricesService = new PricesService(pricesRepository)

    def "Get by product id - happy path."() {
        given:
        def pricesDocument = easyRandom.nextObject(PricesDocument)
        def expectedPriceResponse = pricesDocument.toPricesResponse()

        when:
        def result = pricesService.getPricesByProductId(PRODUCT_ID)

        then:
        expectedPriceResponse == result
        1 * pricesRepository.findByProductId(PRODUCT_ID) >> pricesDocument

        0 * _
    }

    def "Get by product id - not found."() {
        when:
        pricesService.getPricesByProductId(PRODUCT_ID)

        then:
        1 * pricesRepository.findByProductId(PRODUCT_ID) >> null
        PricesNotFoundException caught = thrown(PricesNotFoundException)
        caught.message == PricesExceptionConstants.PRICES_NOT_FOUND.message

        0 * _
    }

    def "Prices Services test - create prices for product id - happy path."() {
        given:
        def prices = easyRandom.nextObject(AllPrices)
        def request = new PricesRequest(prices)
        def priceDocument = new PricesDocument(null, PRODUCT_ID, prices)
        def expectedPriceResponse = priceDocument.toPricesResponse()

        when:
        def result = pricesService.createPricesForProduct(PRODUCT_ID, request)

        then:
        expectedPriceResponse == result
        1 * pricesRepository.save(priceDocument) >> priceDocument

        0 * _
    }    
    
    def "Prices Services test - update current_price for product id - happy path."() {
        given:
        def prices = easyRandom.nextObject(AllPrices)
        def price = prices.currentPrice
        def priceType = "current_price"
        def priceDocument = new PricesDocument(null, PRODUCT_ID, prices)
        def expectedPriceResponse = priceDocument.toPricesResponse()
        def pricesDocumentFields = PricesDocumentFieldsAndFailureCode.UPDATE_CURRENT_PRICE
        def updateMap = [("prices.currentPrice"): (price)]

        when:
        def result = pricesService.updatePricesForProduct(PRODUCT_ID, priceType, price)

        then:
        expectedPriceResponse == result
        1 * pricesRepository.updateSectionsInPricesDocument(PRODUCT_ID, updateMap, pricesDocumentFields) >> priceDocument

        0 * _
    }
}
