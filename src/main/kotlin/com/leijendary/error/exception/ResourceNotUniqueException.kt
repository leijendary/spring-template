package com.leijendary.error.exception

class ResourceNotUniqueException(val value: Any, val pointer: String) :
    RuntimeException("Resource value $value is not unique. $pointer")
