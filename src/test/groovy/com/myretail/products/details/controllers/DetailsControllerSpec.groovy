package com.myretail.products.details.controllers

import com.myretail.products.AbstractSpecification
import com.myretail.products.details.entities.DetailsDocument
import com.myretail.products.details.entities.Price
import com.myretail.products.details.services.DetailsService

class DetailsControllerSpec extends AbstractSpecification {
    def detailsService = Mock(DetailsService)
    def detailsController = new DetailsController(detailsService)

    def "Details Controller test - get by product id - happy path."() {
        given:
        def expectedDetailDocument = new DetailsDocument(123, "Acme Glue", new Price(BigDecimal.ONE.toDouble(), "USD"))

        when:
        def result = detailsController.getDetailsByProductId(123, "name, attribute")

        then:
        expectedDetailDocument == result
        1 * detailsService.getDetailsForProductId(123, "name, attribute") >> expectedDetailDocument

        0 * _
    }
}
