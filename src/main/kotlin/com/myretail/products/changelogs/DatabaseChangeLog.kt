package com.myretail.products.changelogs

import com.github.cloudyrock.mongock.ChangeLog
import com.github.cloudyrock.mongock.ChangeSet
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.CreateCollectionOptions
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.ValidationAction
import com.mongodb.client.model.ValidationLevel
import com.mongodb.client.model.ValidationOptions
import org.bson.Document
import org.springframework.core.io.ClassPathResource
import org.springframework.util.FileCopyUtils

@ChangeLog
class DatabaseChangeLog {
    @ChangeSet(order = "001", id = "initialPricesCollection", author = "SV")
    fun initialPricesCollection(db: MongoDatabase) {
        val validatorJson = readJsonFile(COLLECTION_JSON)
        val validationOptions = ValidationOptions()
            .validator(validatorJson)
            .validationLevel(ValidationLevel.STRICT)
            .validationAction(ValidationAction.WARN)
        val collectionOptions = CreateCollectionOptions().validationOptions(validationOptions)
        db.createCollection(COLLECTION, collectionOptions)
    }

    @ChangeSet(order = "002", id = "initialProductIdIndex", author = "SV")
    fun initialProductIdIndex(db: MongoDatabase) {
        val inventoryRemovalIdIndex = Document(mapOf("productId" to 1))
        val collection = db.getCollection(COLLECTION)
        collection.createIndex(inventoryRemovalIdIndex, IndexOptions().unique(true))
    }

    companion object {

        const val COLLECTION = "prices"
        const val COLLECTION_JSON = "prices-v1.json"

        fun readJsonFile(fileName: String): Document {
            val jsonFileStream = ClassPathResource("db/$fileName").inputStream
            val jsonFileContents = FileCopyUtils.copyToString(jsonFileStream.reader(Charsets.UTF_8))
            return Document.parse(jsonFileContents)
        }
    }
}
