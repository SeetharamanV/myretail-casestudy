package com.myretail.products.details.services

import com.myretail.products.details.clients.ProductAttributesClient
import com.myretail.products.details.entities.AttributesResponse
import com.myretail.products.details.exceptions.DetailsCallException
import com.myretail.products.details.exceptions.DetailsExceptionConstants
import com.myretail.products.details.exceptions.DetailsNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class AttributesCoreService(private val attributesClient: ProductAttributesClient) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getAttributesForProductId(productId: Long, excludes: String): AttributesResponse {
        val response = attributesClient.getAttributesByProductId(productId, excludes).execute()
        logger.info("$response")
        if (response.isSuccessful && null != response.body()) {
            return response.body()!!
        } else {
            if (response.code() == HttpStatus.NOT_FOUND.value()) {
                logger.error("Error:${DetailsExceptionConstants.DETAILS_ATTRIBUTES_NOT_FOUND.message}, Response:$response")
                throw DetailsNotFoundException(DetailsExceptionConstants.DETAILS_ATTRIBUTES_NOT_FOUND.code, DetailsExceptionConstants.DETAILS_ATTRIBUTES_NOT_FOUND.message)
            } else {
                logger.error("Error:${DetailsExceptionConstants.DETAILS_ATTRIBUTES_CALL_ERROR.message}, Response:$response")
                throw DetailsCallException(DetailsExceptionConstants.DETAILS_ATTRIBUTES_CALL_ERROR.code, DetailsExceptionConstants.DETAILS_ATTRIBUTES_CALL_ERROR.message)
            }
        }
    }
}
