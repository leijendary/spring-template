package com.leijendary.spring.boot.template.core.exception

class ResourceNotUniqueException(val source: List<String>, val value: String) : RuntimeException()