package com.leijendary.view

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping
class HomeController {
    @GetMapping
    fun index(model: Model): String {
        model.addAttribute("message", "This is from the backend")
        return "home/index"
    }
}
