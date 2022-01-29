package com.leijendary.spring.boot.template.core.data

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Pageable")
data class AppPageable(
    @Schema(
        description = "Number of records per page",
        type = "number",
        format = "int",
        example = "20"
    )
    var size: Int = 20,

    @Schema(
        description = "Results page you want to retrieve (1..N)",
        type = "number",
        format = "int",
        example = "1"
    )
    var page: Int = 1,

    @Schema(
        description = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. " +
                "Multiple sort criteria are supported.",
        type = "string",
        example = "id,asc"
    )
    var sort: String = "asc"
)