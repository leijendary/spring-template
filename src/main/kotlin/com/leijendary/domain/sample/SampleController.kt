package com.leijendary.domain.sample

import com.leijendary.client.PetStoreClient
import com.leijendary.context.RequestContext.getAttributeOrDefault
import com.leijendary.extension.transactional
import com.leijendary.model.Cursorable
import com.leijendary.model.CursoredModel
import com.leijendary.model.QueryRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.openapi.petstore.v2.model.Pet
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.*

private const val CACHE_KEY_COUNT = "sample::count"
private const val CACHE_KEY_TIMESTAMP = "sample::timestamp"

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
@RequestMapping("api/v1/samples")
@Tag(name = "Sample")
class SampleController(
    private val petStoreClient: PetStoreClient,
    private val redisTemplate: StringRedisTemplate,
    private val sampleService: SampleService
) {
    @GetMapping
    @Operation(
        summary = """
           List of all sample records in a cursor-based result. Use createdAt and id in the next request to get 
           the next page. This is a faster way to do pagination since it does not use limit offset, but rather
           uses the index to get the next page of the result.
        """
    )
    fun cursor(queryRequest: QueryRequest, cursorable: Cursorable): CursoredModel<SampleResponse> {
        return sampleService.cursor(queryRequest, cursorable)
    }

    @GetMapping("{id}")
    @Operation(summary = "Retrieves the translated sample record from the database.")
    fun get(@PathVariable id: String): SampleDetailResponse {
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
    fun requestScoped(@RequestParam value: UUID): Pair<Map<String, UUID>?, Map<String, UUID>?> {
        val simpleName = UUID::class.qualifiedName!!
        val a = getAttributeOrDefault(simpleName) { mapOf("value" to value) }
        val b = getAttributeOrDefault(simpleName) { mapOf("differentValue" to UUID.randomUUID()) }

        assert(value == a["value"])
        assert(a == b)

        return a to b
    }

    @GetMapping("counter")
    fun counter(): Map<String, Any?> {
        val timestamp = Instant.now()
        val (counter, previousTimestamp) = redisTemplate.transactional {
            it.multi()

            val ops = it.opsForValue()
            ops.increment(CACHE_KEY_COUNT)
            ops.getAndSet(CACHE_KEY_TIMESTAMP, timestamp.toString())

            val result = it.exec()

            result[0] as Long to (result[1] as? String)?.let(Instant::parse)
        }

        return mapOf(
            "counter" to counter,
            "previousTimestamp" to previousTimestamp,
            "timestamp" to timestamp
        )
    }
}
