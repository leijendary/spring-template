package com.leijendary.spring.template.core.data

import com.leijendary.spring.template.core.util.RequestContext.now
import com.leijendary.spring.template.core.util.RequestContext.uri
import io.opentelemetry.api.trace.Span
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import java.net.URI

class ErrorResponse(
    val errors: List<ErrorData>? = null,
    val meta: Map<String, Any> = emptyMap(),
    val links: Map<String, URI?>? = null
) : Response {
    companion object {
        fun builder(): ErrorResponseBuilder {
            return ErrorResponseBuilder()
                .status(INTERNAL_SERVER_ERROR)
                .selfLink()
                .traceId()
        }
    }

    class ErrorResponseBuilder {
        private val errors: MutableList<ErrorData> = ArrayList()
        private val meta: MutableMap<String, Any> = HashMap()
        private val links: MutableMap<String, URI?> = HashMap()

        fun build(): ErrorResponse {
            meta["timestamp"] = now

            return ErrorResponse(errors, meta, links)
        }

        fun addError(source: List<Any>, code: String, message: String?): ErrorResponseBuilder {
            errors.add(ErrorData(source, code, message))

            return this
        }

        fun meta(key: String, value: Any): ErrorResponseBuilder {
            meta[key] = value

            return this
        }

        fun status(httpStatus: HttpStatus): ErrorResponseBuilder {
            meta["status"] = httpStatus.value()

            return this
        }

        fun traceId(): ErrorResponseBuilder {
            meta["traceId"] = Span.current().spanContext.traceId

            return this
        }

        fun selfLink(): ErrorResponseBuilder {
            links["self"] = uri

            return this
        }
    }
}