package com.leijendary.spring.core.exception

import com.leijendary.spring.core.model.ErrorSource

class ResourceNotUniqueException(val value: Any, val source: ErrorSource) : RuntimeException()
