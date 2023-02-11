package com.leijendary.spring.template.core.controller

import com.leijendary.spring.template.core.config.HEADER_USER_ID
import io.swagger.v3.oas.annotations.security.SecurityRequirement

@SecurityRequirement(name = HEADER_USER_ID)
open class SecuredController
