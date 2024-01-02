package com.leijendary.error.exception

import com.leijendary.model.ErrorSource

class ResourceNotUniqueException(val value: Any, val source: ErrorSource) : RuntimeException()
