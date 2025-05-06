package com.leijendary.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("dashboard")
class DashboardViewController {
    @GetMapping
    fun index(): String {
        return "page/dashboard/index"
    }
}
