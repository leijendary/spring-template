package com.leijendary.spring.boot.template.core.data

import com.leijendary.spring.boot.template.core.util.RequestContext.now
import com.leijendary.spring.boot.template.core.util.RequestContext.uri
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import java.io.Serializable
import java.net.URI

class ErrorResponse(
    val errors: List<ErrorData>? = null,
    val meta: Map<String, Any> = emptyMap(),
    val links: Map<String, URI?>? = null
) : Serializable {

    companion object {
        fun builder(): ErrorResponseBuilder {
            return ErrorResponseBuilder()
                .status(INTERNAL_SERVER_ERROR)
                .selfLink()
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

        fun selfLink(): ErrorResponseBuilder {
            links["self"] = uri

            return this
        }
    }
}