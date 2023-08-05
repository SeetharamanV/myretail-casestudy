package com.myretail.products.details.services

import com.myretail.products.AbstractSpecification
import com.myretail.products.details.clients.ProductAttributesClient
import com.myretail.products.details.entities.AttributesResponse
import com.myretail.products.details.exceptions.DetailsCallException
import com.myretail.products.details.exceptions.DetailsExceptionConstants
import com.myretail.products.details.exceptions.DetailsNotFoundException
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class AttributesCoreServiceSpec extends AbstractSpecification {
    def attributesClient = Mock(ProductAttributesClient)
    def attributesCoreService = new AttributesCoreService(attributesClient)

    def "Attributes Core Service - get by product id - happy path."() {
        given:
        def attributesResponse = easyRandom.nextObject(AttributesResponse)
        def mockCall = Mock(Call)
        def retrofitResponse = Response.success(attributesResponse)

        when:
        def result = attributesCoreService.getAttributesForProductId(123, "name, attribute")

        then:
        1 * mockCall.execute() >> retrofitResponse
        1 * attributesClient.getAttributesByProductId(123, "name, attribute") >> mockCall
        and:
        attributesResponse == result

        0 * _
    }

    def "Attributes Core Service - get by product id - 404."() {
        given:
        def mockCall = Mock(Call)
        def retrofitResponse = Response.error(404, ResponseBody.create(MediaType.get("application/json"), ""))

        when:
        attributesCoreService.getAttributesForProductId(123, "name")

        then:
        1 * mockCall.execute() >> retrofitResponse
        1 * attributesClient.getAttributesByProductId(123,"name") >> mockCall

        and:
        DetailsNotFoundException caught = thrown(DetailsNotFoundException.class)
        caught.message == DetailsExceptionConstants.DETAILS_ATTRIBUTES_NOT_FOUND.message

        0 * _
    }

    def "Attributes Core Service - get by product id - 500."() {
        given:
        def mockCall = Mock(Call)
        def retrofitResponse = Response.error(500, ResponseBody.create(MediaType.get("application/json"), ""))

        when:
        attributesCoreService.getAttributesForProductId(123, "name")

        then:
        1 * mockCall.execute() >> retrofitResponse
        1 * attributesClient.getAttributesByProductId(123,"name") >> mockCall

        and:
        DetailsCallException caught = thrown(DetailsCallException.class)
        caught.message == DetailsExceptionConstants.DETAILS_ATTRIBUTES_CALL_ERROR.message

        0 * _
    }
}
