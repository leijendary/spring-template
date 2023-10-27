package com.leijendary.spring.core.exception

class VersionConflictException(val id: Any, val entity: String) : RuntimeException()
