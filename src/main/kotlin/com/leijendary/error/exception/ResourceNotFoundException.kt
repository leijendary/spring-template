package com.leijendary.error.exception

import com.leijendary.model.ErrorSource

class ResourceNotFoundException(
    val id: Any,
    val entity: String,
    val source: ErrorSource
) : RuntimeException("Resource $entity $id not found. $source")
