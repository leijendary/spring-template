package com.leijendary.error

/**
 * Error codes
 */
const val CODE_BAD_REQUEST = "error.badRequest"
const val CODE_SERVER_ERROR = "error.serverError"
const val CODE_DATA_VERSION_CONFLICT = "error.data.version.conflict"
const val CODE_DATA_REFERENCED = "error.data.referenced"
const val CODE_RESOURCE_NOT_FOUND = "error.resource.notFound"
const val CODE_DATA_INTEGRITY = "error.data.integrity"
const val CODE_FORMAT_INVALID = "error.format.invalid"
const val CODE_FORMAT_INCOMPATIBLE = "error.format.incompatible"

/**
 * Access codes
 */
const val CODE_SESSION_NOT_FOUND = "access.session.notFound"

/**
 * Validation codes
 */
const val CODE_ALREADY_EXISTS = "validation.alreadyExists"
const val CODE_REQUIRED = "validation.required"
const val CODE_BINDING_INVALID_VALUE = "validation.binding.invalidValue"
const val CODE_IMAGE_MEDIA_TYPE = "validation.image.mediaType"
const val CODE_SIZE_RANGE = "validation.size.range"
const val CODE_DECIMAL_MIN = "validation.decimal.min"
const val CODE_DECIMAL_MAX = "validation.decimal.max"
const val CODE_MIN = "validation.min"
const val CODE_DUPLICATE_VALUE = "validation.duplicateValue"
const val CODE_SIZE_SAME = "validation.size.same"
const val CODE_IMAGE_NAME = "validation.image.name"

const val DETAIL_STILL_REFERENCED = "is still referenced"

const val PROPERTY_ERRORS = "errors"
const val PROPERTY_PROBLEM_DETAIL = "problemDetail"

const val POINTER_SERVER_INTERNAL = "#/server/internal"
