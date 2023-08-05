package com.myretail.products.details.clients

import com.myretail.products.details.entities.AttributesResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductAttributesClient {
    @GET("{productId}")
    fun getAttributesByProductId(@Path("productId") productId: Long, @Query("excludes") excludes: String = ""): Call<AttributesResponse>
}
