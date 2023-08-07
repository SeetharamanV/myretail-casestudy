package com.myretail.products.details.services

import com.myretail.products.AbstractSpecification
import com.myretail.products.details.clients.ProductPricesClient
import com.myretail.products.details.entities.AttributesResponse
import com.myretail.products.details.entities.PricesResponse
import retrofit2.Call
import retrofit2.Response

class DetailsServiceSpec extends AbstractSpecification {
    def pricesCoreService = Mock(PricesCoreService)
    def attributesCoreService = Mock(AttributesCoreService)
    def detailsService = new DetailsService(pricesCoreService, attributesCoreService)

    def "Details Service Spec - get by product id - happy path."() {
        given:
        def pricesResponse = easyRandom.nextObject(PricesResponse)
        def attributesResponse = easyRandom.nextObject(AttributesResponse)

        when:
        def result = detailsService.getDetailsForProductId(123,"name")

        then:
        1 * pricesCoreService.getPricesForProductId(123) >> pricesResponse
        1 * attributesCoreService.getAttributesForProductId(123, "name") >> attributesResponse
        and:
        result
        pricesResponse.prices.currentPrice == result.currentPrice
        attributesResponse.attributes.name == result.name

        0 * _
    }
}
