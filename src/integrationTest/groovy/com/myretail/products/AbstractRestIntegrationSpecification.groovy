package com.myretail.products

import com.myretail.products.prices.entities.AllPrices
import com.myretail.products.prices.entities.Price
import com.myretail.products.prices.entities.PricesDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

@AutoConfigureMockMvc
abstract class AbstractRestIntegrationSpecification extends AbstractIntegrationSpecification {
    @Autowired
    MockMvc mockMvc

    protected static PRODUCT_ID = 123456L
    protected static DEFAULT_PRICE = 1.99
    protected static DEFAULT_CURRENCY_CODE = "USD"

    def mockGet(String url, String authorization) {
        return mockMvc.perform(get(url)
                .header("authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
    }

    def mockPost(String url, String authorization, String postBody) {
        return mockMvc.perform(post(url)
                .content(postBody)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, authorization)
                .accept(MediaType.APPLICATION_JSON))
    }

    def mockPut(String url, String authorization, String putBody) {
        return mockMvc.perform(put(url)
                .content(putBody)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, authorization)
                .accept(MediaType.APPLICATION_JSON))
    }

    def mockDelete(String url) {
        return mockMvc.perform(delete(url)
                .header(AUTH_HEADER_NAME, AUTH_HEADER_VALUE)
                .accept(MediaType.APPLICATION_JSON))
    }

    PricesDocument saveTestPrices() {
        PricesDocument pricesDocument = new PricesDocument(
                null,
                PRODUCT_ID,
                new AllPrices(
                        new Price(
                            DEFAULT_PRICE as Double,
                            DEFAULT_CURRENCY_CODE as String,
                        ),
                        new Price(
                            DEFAULT_PRICE as Double,
                            DEFAULT_CURRENCY_CODE as String,
                        ),
                        new Price(
                            DEFAULT_PRICE as Double,
                            DEFAULT_CURRENCY_CODE as String,
                        )
                )
        )
        return savePricesDocument(pricesDocument)
    }
}
