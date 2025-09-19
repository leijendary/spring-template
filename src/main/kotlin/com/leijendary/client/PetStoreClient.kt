package com.leijendary.client

import feign.RequestInterceptor
import org.openapi.petstore.v2.model.Pet
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@FeignClient("petStore", configuration = [PetStoreFeignConfiguration::class])
interface PetStoreClient {
    @GetMapping("v2/pet/findByStatus")
    fun getPets(@RequestParam status: Pet.Status): List<Pet>

    @GetMapping("v2/store/inventory")
    fun getStoreInventory(): Map<String, Int>

    @GetMapping("v2/user/login")
    fun login(@RequestParam username: String, @RequestParam password: String): Any
}

class PetStoreFeignConfiguration {
    @Bean
    fun requestInterceptor() = RequestInterceptor {
        it.header(AUTHORIZATION, "Bearer ${UUID.randomUUID()}")
    }
}
