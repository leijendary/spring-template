package com.leijendary.view

import com.leijendary.domain.sample.SampleRequest
import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping
class HomeViewController {
    @GetMapping
    fun index(model: Model): String {
        model.addAttribute("message", "This is from the backend")

        return "page/home/index"
    }

    @PostMapping
    fun submit(model: Model, @Valid @RequestBody request: SampleRequest): String {
        model.addAttribute("message", "This is from the backend")

        return "page/home/index"
    }
}
