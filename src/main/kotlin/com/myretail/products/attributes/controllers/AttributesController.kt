package com.myretail.products.attributes.controllers

import com.myretail.products.attributes.entities.AttributesDocument
import com.myretail.products.attributes.services.AttributesService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/attributes/v1"], produces = [MediaType.APPLICATION_JSON_VALUE])
class AttributesController(private val attributesService: AttributesService) {

    @GetMapping("/products/{productId}")
    fun getAttributesByProductId(
        @PathVariable productId: Long,
        @RequestParam("excludes", defaultValue = "") excludes: String
    ): AttributesDocument {
        return attributesService.process(productId, excludes)
    }
}
