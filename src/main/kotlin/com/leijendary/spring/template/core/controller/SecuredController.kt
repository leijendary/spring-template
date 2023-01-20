package com.leijendary.spring.template.core.controller

import com.leijendary.spring.template.core.config.SECURITY_SCHEME_BEARER
import io.swagger.v3.oas.annotations.security.SecurityRequirement

@SecurityRequirement(name = SECURITY_SCHEME_BEARER)
open class SecuredController
