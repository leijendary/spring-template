package com.leijendary.model

import java.io.Serializable

data class IdentityModel<T>(val id: T) : Serializable
