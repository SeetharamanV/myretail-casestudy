package com.myretail.products.details.services

import com.myretail.products.AbstractSpecification
import com.myretail.products.details.clients.ProductPricesClient
import com.myretail.products.details.entities.PricesResponse
import com.myretail.products.details.exceptions.DetailsCallException
import com.myretail.products.details.exceptions.DetailsException
import com.myretail.products.details.exceptions.DetailsExceptionConstants
import com.myretail.products.details.exceptions.DetailsNotFoundException
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class PricesCoreServiceSpec extends AbstractSpecification {
    def pricesClient = Mock(ProductPricesClient)
    def pricesCoreService = new PricesCoreService(pricesClient)

    def "Prices Core Service - get by product id - happy path."() {
        given:
        def pricesResponse = easyRandom.nextObject(PricesResponse)
        def mockCall = Mock(Call)
        def retrofitResponse = Response.success(pricesResponse)

        when:
        def result = pricesCoreService.getPricesForProductId(123)

        then:
        1 * mockCall.execute() >> retrofitResponse
        1 * pricesClient.getPricesByProductId(123) >> mockCall
        and:
        pricesResponse == result

        0 * _
    }

    def "Prices Core Service - get by product id - 404."() {
        given:
        def mockCall = Mock(Call)
        def retrofitResponse = Response.error(404, ResponseBody.create(MediaType.get("application/json"), ""))

        when:
        pricesCoreService.getPricesForProductId(123)

        then:
        1 * mockCall.execute() >> retrofitResponse
        1 * pricesClient.getPricesByProductId(123) >> mockCall

        and:
        DetailsNotFoundException caught = thrown(DetailsNotFoundException.class)
        caught.message == DetailsExceptionConstants.DETAILS_PRICES_NOT_FOUND.message

        0 * _
    }

    def "Prices Core Service - get by product id - 500."() {
        given:
        def mockCall = Mock(Call)
        def retrofitResponse = Response.error(500, ResponseBody.create(MediaType.get("application/json"), ""))

        when:
        pricesCoreService.getPricesForProductId(123)

        then:
        1 * mockCall.execute() >> retrofitResponse
        1 * pricesClient.getPricesByProductId(123) >> mockCall

        and:
        DetailsCallException caught = thrown(DetailsCallException.class)
        caught.message == DetailsExceptionConstants.DETAILS_PRICES_CALL_ERROR.message

        0 * _
    }
}
