package com.myretail.products.attributes.repositories

import com.myretail.products.attributes.entities.Attributes
import com.myretail.products.attributes.entities.AttributesDocument
import org.springframework.stereotype.Repository

@Repository
class AttributesRepository {
    fun getAttributesDocumentByProductId(productId: Long): AttributesDocument {
        return AttributesDocument(productId, Attributes("Acme Glue", "Home", "pounds"))
    }
}
