package com.myretail.products.configs

import com.fasterxml.jackson.databind.ObjectMapper
import com.myretail.products.details.clients.ProductAttributesClient
import com.myretail.products.details.clients.ProductPricesClient
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.binder.okhttp3.OkHttpMetricsEventListener
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.time.Duration

@Configuration
class RetrofitConfig(
    private val partnerApisProperties: PartnerApisProperties,
    private val objectMapper: ObjectMapper,
    private val meterRegistry: MeterRegistry
) {
    @Bean
    fun productPricesClient(): ProductPricesClient =
        getRetrofit(partnerApisProperties.prices)
            .create(ProductPricesClient::class.java)

    @Bean
    fun productAttributesClient(): ProductAttributesClient =
        getRetrofit(partnerApisProperties.attributes)
            .create(ProductAttributesClient::class.java)

    fun getRetrofit(urlProperties: UrlProperties): Retrofit = Retrofit.Builder()
        .baseUrl(urlProperties.url)
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
        .client(getOkHttpClient(urlProperties.timeout))
        .build()

    private fun getOkHttpClient(timeout: Long = 10L): OkHttpClient {
        var client = OkHttpClient().newBuilder()
            .readTimeout(Duration.ofSeconds(timeout))
            .addInterceptor { chain -> buildChain(chain = chain) }
            .build()

        client = client.newBuilder()
            .eventListener(
                OkHttpMetricsEventListener
                    .builder(meterRegistry, "okhttp_requests")
                    .tag { req, _ -> Tag.of("real_uri", req.url.encodedPath) }
                    .build()
            )
            .build()

        return client
    }

    private fun buildChain(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val originalHttpUrl = original.url

        // Adding api key as query param
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(API_KEY_PATH, partnerApisProperties.apiKey)
            .build()

        // Adding authorization token to header
        val alteredRequest = chain
            .request()
            .newBuilder()
//                .addHeader(API_KEY_HEADER, partnerApisProperties.apiKey)
            .addHeader(AUTH_HEADER, partnerApisProperties.authorizationToken)
            .url(url)
            .build()
        return chain.proceed(alteredRequest)
    }

    companion object {
//        private const val API_KEY_HEADER = "x-api-key"
        private const val AUTH_HEADER = "authorization"
        private const val API_KEY_PATH = "key"
    }
}
