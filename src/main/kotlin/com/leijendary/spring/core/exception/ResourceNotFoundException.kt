package com.leijendary.spring.core.exception

import com.leijendary.spring.core.model.ErrorSource

class ResourceNotFoundException(val id: Any, val source: ErrorSource) : RuntimeException()
