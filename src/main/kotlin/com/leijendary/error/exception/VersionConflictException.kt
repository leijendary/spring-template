package com.leijendary.error.exception

class VersionConflictException(val id: Any, val entity: String, val version: Int) : RuntimeException()
