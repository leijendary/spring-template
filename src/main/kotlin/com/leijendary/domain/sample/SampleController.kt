package com.leijendary.domain.sample

import com.leijendary.client.PetStoreClient
import com.leijendary.model.QueryRequest
import com.leijendary.model.Seek
import com.leijendary.model.SeekRequest
import com.leijendary.util.requestAttribute
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.openapi.petstore.v2.model.Pet
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * This is an example of a controller that will be created in microservices.
 *
 * There are 3 parts of the [RequestMapping] url that we need to take note of:
 *      1. The api prefix ("api")
 *      2. The version ("v1")
 *      3. The parent path of this API ("/samples") which can be anything that this specific controller should be doing.
 *
 * The url paths should be in kebab-case except for the query parameters, body,
 * and other URL parts in which they should be in camelCase.
 *
 * For headers, I would recommend that the Header keys should be in
 * Pascal-Kebab-Case
 */
@RestController
@RequestMapping("/api/v1/samples")
@Tag(name = "Sample")
class SampleController(private val petStoreClient: PetStoreClient, private val sampleService: SampleService) {
    @GetMapping
    @Operation(
        summary = """
           List of all sample records in a cursor-based result. Use createdAt and id in the next request to get 
           the next page. This is a faster way to do pagination since it does not use limit offset, but rather
           uses the index to get the next page of the result.
        """
    )
    fun seek(queryRequest: QueryRequest, seekRequest: SeekRequest): Seek<SampleList> {
        return sampleService.seek(queryRequest, seekRequest)
    }

    @GetMapping("{id}")
    @Operation(summary = "Retrieves the translated sample record from the database.")
    fun get(@PathVariable id: Long): SampleDetail {
        return sampleService.get(id, true)
    }

    @GetMapping("open-api/pets/all")
    fun openApiPets(): List<Pet> {
        return petStoreClient.getPets(Pet.Status.AVAILABLE)
    }

    @GetMapping("open-api/store/inventory")
    fun openApiStoreInventory(): Map<String, Int> {
        return petStoreClient.getStoreInventory()
    }

    @GetMapping("open-api/user/login")
    fun openApiUserLogin(): Any {
        return petStoreClient.login("", "")
    }

    @GetMapping("request-scoped")
    fun requestScoped(@RequestParam value: UUID): Pair<Map<String, UUID>, Map<String, UUID>> {
        val simpleName = UUID::class.qualifiedName!!
        val a = requestAttribute(simpleName) { mapOf("value" to value) }
        val b = requestAttribute(simpleName) { mapOf("value" to value) }

        assert(value == a["value"])
        assert(a == b)

        return a to b
    }
}
