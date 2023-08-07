package com.myretail.products

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomjankes.wiremock.WireMockGroovy
import com.myretail.products.configs.PartnerApisProperties
import com.myretail.products.prices.entities.PricesDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper
import org.springframework.context.annotation.ImportResource
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.BootstrapWith
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@BootstrapWith(SpringBootTestContextBootstrapper)
@SpringBootTest(classes = [MyRetailApplication])
@ActiveProfiles(profiles = 'integration')
//@ContextConfiguration(locations=["classpath:/application.yml"])
//@ImportResource(["classpath*:application.yml"])
abstract class AbstractIntegrationSpecification extends Specification {

    @Autowired
    ObjectMapper objectMapper

    @Autowired
    PartnerApisProperties partnerApisProperties

    @Autowired
    MongoTemplate mongoTemplate

    static WireMockServer wireMockServer = new WireMockServer(8090)
    static WireMockGroovy wireMock = new WireMockGroovy(8090)

    static final String COLLECTION = "prices"
    static final Query EMPTY_QUERY = new Query()

    protected String AUTH_HEADER_NAME = "authorization"
    protected String PRICES_URI = "/my_retail/prices/v1/products"
    protected String ATTRIBUTES_URI = "/my_retail/attributes/v1/products"

    def setupSpec() {
        println("Starting wireMockServer.")
        wireMockServer.start()
    }

    def setup() {
        println("reset all wireMockServer.")
        wireMockServer.resetAll()
    }

    def cleanupSpec() {
        println("stop wireMockServer")
        wireMockServer.stop()
    }

    def cleanup(){
        mongoTemplate.remove(EMPTY_QUERY, COLLECTION)
        println("Cleaned up.")
    }

    def stub(String mockUrl, String mockMethod, String responseBody, int mockStatus = 200) {
        wireMock.stub {
            request {
                method mockMethod
                url mockUrl
            }
            response {
                status mockStatus
                body responseBody
                headers {
                    "Content-Type" "application/json"
                }
            }
        }
    }

    def getPricesUrl(Long productId) {
        return PRICES_URI + "/$productId?key=${partnerApisProperties.apiKey}"
    }

    def getAttributesUrl(Long productId, String excludes="") {
        return ATTRIBUTES_URI+ "/$productId?excludes=$excludes&key=${partnerApisProperties.apiKey}"
    }

    Integer getWireMockCallCount(String stubbedUrl, String mockedMethod) {
        return wireMock.count {
            method mockedMethod
            url stubbedUrl
        }
    }

    PricesDocument savePricesDocument(PricesDocument document) {
        return mongoTemplate.save(document, COLLECTION)
    }
}
