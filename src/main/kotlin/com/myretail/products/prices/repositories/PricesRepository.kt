package com.myretail.products.prices.repositories

import com.myretail.products.prices.entities.PricesDocument
import com.myretail.products.prices.entities.PricesDocumentFields
import com.myretail.products.prices.entities.PricesDocumentFieldsAndFailureCode
import com.myretail.products.prices.exceptions.PricesDatabaseException
import com.myretail.products.prices.exceptions.PricesNotFoundException
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PricesRepository : MongoRepository<PricesDocument, ObjectId>, PricesUpdateRepository {
    fun findByProductId(productId: Long): PricesDocument?
}

interface PricesUpdateRepository {
    fun updateSectionsInPricesDocument(
        productId: Long,
        updateMap: Map<String, Any?>,
        pricesDocumentFieldsAndFailureCode: PricesDocumentFieldsAndFailureCode
    ): PricesDocument?
}

class PricesUpdateRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : PricesUpdateRepository {
    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun updateSectionsInPricesDocument(productId: Long, updateMap: Map<String, Any?>, pricesDocumentFieldsAndFailureCode: PricesDocumentFieldsAndFailureCode): PricesDocument? {
        val query = Query()
        query.addCriteria(Criteria.where(PricesDocumentFields.PRODUCT_ID.fieldName).isEqualTo(productId))
        if (0L == mongoTemplate.count(query, DOCUMENT_CLASS)) {
            throw PricesNotFoundException("Prices not found for product: $productId, for event: ${pricesDocumentFieldsAndFailureCode.event}")
        }
        val update = Update()
        updateMap.forEach {
            update.set(it.key, it.value)
        }
        return try {
            mongoTemplate.updateFirst(query, update, DOCUMENT_CLASS)
            mongoTemplate.findOne(query, DOCUMENT_CLASS)
        } catch (ex: Exception) {
            logger.error(
                "Error:{}, Query:{}, Update:{}, exception:{}",
                pricesDocumentFieldsAndFailureCode.failureCode,
                query.toString(),
                update.toString(),
                ex
            )
            throw PricesDatabaseException(
                message = pricesDocumentFieldsAndFailureCode.failureCode,
                cause = ex
            )
        }
    }

    companion object {
        val DOCUMENT_CLASS = PricesDocument::class.java
    }
}
