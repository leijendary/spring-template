package com.leijendary.error.exception

class ResourceNotFoundException(val id: Any, val entity: String, val pointer: String) :
    RuntimeException("Resource $entity $id not found. $pointer")
