package com.myretail.products.details.services

import com.myretail.products.details.clients.ProductPricesClient
import com.myretail.products.details.entities.PricesResponse
import com.myretail.products.details.exceptions.DetailsCallException
import com.myretail.products.details.exceptions.DetailsExceptionConstants.DETAILS_PRICES_CALL_ERROR
import com.myretail.products.details.exceptions.DetailsExceptionConstants.DETAILS_PRICES_NOT_FOUND
import com.myretail.products.details.exceptions.DetailsNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class PricesCoreService(private val pricesClient: ProductPricesClient) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getPricesForProductId(productId: Long): PricesResponse {
        val response = pricesClient.getPricesByProductId(productId).execute()
        logger.info("$response")
        if (response.isSuccessful && null != response.body()) {
            return response.body()!!
        } else {
            if (response.code() == HttpStatus.NOT_FOUND.value()) {
                logger.error("Error:${DETAILS_PRICES_NOT_FOUND.message}, Response:$response")
                throw DetailsNotFoundException(DETAILS_PRICES_NOT_FOUND.code, DETAILS_PRICES_NOT_FOUND.message)
            } else {
                logger.error("Error:${DETAILS_PRICES_CALL_ERROR.message}, Response:$response")
                throw DetailsCallException(DETAILS_PRICES_CALL_ERROR.code, DETAILS_PRICES_CALL_ERROR.message)
            }
        }
    }
}
