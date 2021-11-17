package com.leijendary.spring.boot.template.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Pageable")
public class AppPageable {

    @Schema(description = "Number of records per page", type = "number", format = "int", example = "20")
    private int size = 20;

    @Schema(description = "Results page you want to retrieve (1..N)", type = "number", format = "int", example = "1")
    private int page = 1;

    @Schema(
            description = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. " +
                    "Multiple sort criteria are supported.", type = "string", example = "id,asc"
    )
    private String sort = "asc";
}
