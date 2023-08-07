package com.myretail.products

import com.github.cloudyrock.spring.v5.EnableMongock
import com.myretail.products.configs.PartnerApisProperties
import com.myretail.products.security.SecurityProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableMongock
@SpringBootApplication
@EnableConfigurationProperties(SecurityProperties::class, PartnerApisProperties::class)
class MyRetailApplication

fun main(args: Array<String>) {
    try {
        runApplication<MyRetailApplication>(*args)
    } catch (t: Throwable) {
        println(t.message)
        t.printStackTrace()
    }
}
