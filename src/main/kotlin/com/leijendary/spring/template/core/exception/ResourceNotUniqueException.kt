package com.leijendary.spring.template.core.exception

class ResourceNotUniqueException(val source: List<String>, val value: String) : RuntimeException()