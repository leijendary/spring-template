package com.leijendary.spring.template.core.exception

class ResourceNotFoundException(val source: List<Any>, val identifier: Any) : RuntimeException()
