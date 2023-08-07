package com.myretail.products.details.clients

import com.myretail.products.details.entities.PricesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductPricesClient {
    @GET("{productId}")
    fun getPricesByProductId(@Path("productId") productId: Long): Call<PricesResponse>
}
