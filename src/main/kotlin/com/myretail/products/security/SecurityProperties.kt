package com.myretail.products.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "myretail.security")
data class SecurityProperties(
    val validApiKeys: List<String>,
    val validAuthorizationToken: String
)
