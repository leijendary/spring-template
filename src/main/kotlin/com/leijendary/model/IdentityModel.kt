package com.leijendary.model

import org.springframework.aot.hint.annotation.RegisterReflection
import java.io.Serializable

@RegisterReflection
data class IdentityModel<T>(val id: T) : Serializable
