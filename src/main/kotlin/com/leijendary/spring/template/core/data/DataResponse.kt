package com.leijendary.spring.template.core.data

import com.leijendary.spring.template.core.util.RequestContext.now
import com.leijendary.spring.template.core.util.RequestContext.uri
import io.opentelemetry.api.trace.Span
import org.apache.http.client.utils.URIBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.web.util.UriComponentsBuilder
import org.springframework.web.util.UriComponentsBuilder.fromUri
import java.net.URI

class DataResponse<T>(
    val data: T? = null,
    val meta: Map<String, Any> = emptyMap(),
    val links: Map<String, URI?>? = null
) : Response {
    companion object {
        fun <T> builder(): DataResponseBuilder<T> {
            return DataResponseBuilder<T>()
                .status(OK)
                .selfLink()
                .traceId()
        }
    }

    class DataResponseBuilder<T> {
        private var data: T? = null
        private val meta: MutableMap<String, Any> = HashMap()
        private val links: MutableMap<String, URI?> = HashMap()

        fun build(): DataResponse<T> {
            meta["timestamp"] = now

            return DataResponse(data, meta, links)
        }

        fun data(data: T): DataResponseBuilder<T> {
            this.data = data

            return this
        }

        fun meta(key: String, value: Any): DataResponseBuilder<T> {
            meta[key] = value

            return this
        }

        fun status(httpStatus: HttpStatus): DataResponseBuilder<T> {
            meta["status"] = httpStatus.value()

            return this
        }

        fun meta(page: Page<*>): DataResponseBuilder<T> {
            meta["page"] = PageMeta(page)

            return this
        }

        fun meta(seek: Seek<*>): DataResponseBuilder<T> {
            meta["seek"] = SeekMeta(seek)

            return this
        }

        fun traceId(): DataResponseBuilder<T> {
            meta["traceId"] = Span.current().spanContext.traceId

            return this
        }

        fun selfLink(): DataResponseBuilder<T> {
            links["self"] = uri

            return this
        }

        fun links(page: Page<*>): DataResponseBuilder<T> {
            val size = page.size
            val sort = page.sort

            links["self"] = createLink(page.pageable.pageNumber, size, sort)

            if (page.hasPrevious()) {
                val previousPageable = page.previousOrFirstPageable()

                links["previous"] = createLink(previousPageable.pageNumber, size, sort)
            }

            if (page.hasNext()) {
                val nextPageable = page.nextOrLastPageable()

                links["next"] = createLink(nextPageable.pageNumber, size, sort)
            }

            links["last"] = createLink(page.totalPages, size, sort)

            return this
        }

        fun links(seek: Seek<*>): DataResponseBuilder<T> {
            val nextToken = seek.nextToken
            val limit = seek.seekable.limit

            links["next"] = createLink(nextToken, limit)

            return this
        }

        private fun createLink(page: Int, size: Int, sort: Sort): URI? {
            val uri: URI = uri ?: return null
            val builder: UriComponentsBuilder = fromUri(uri)
                .replaceQueryParam("page", page)
                .replaceQueryParam("size", size)

            if (sort.isSorted) {
                builder.replaceQueryParam("sort", sort.toString())
            }

            return builder.build().toUri()
        }

        private fun createLink(nextToken: String?, limit: Int): URI? {
            val uri: URI = uri ?: return null

            return URIBuilder(uri)
                .removeQuery()
                .setCustomQuery("limit=$limit&nextToken=$nextToken")
                .build()
        }
    }
}