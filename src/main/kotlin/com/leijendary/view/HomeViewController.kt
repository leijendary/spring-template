package com.leijendary.view

import com.leijendary.domain.sample.SampleRequest
import com.leijendary.extension.withProblemDetail
import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping
class HomeViewController {
    @GetMapping
    fun index(model: Model): String {
        model.addAttribute("message", "This is from the backend")

        return "page/index"
    }

    @PostMapping
    fun submit(@Valid @ModelAttribute request: SampleRequest, bindingResult: BindingResult, model: Model): String {
        if (bindingResult.hasErrors()) {
            model.withProblemDetail(bindingResult)
        }

        model.addAttribute("message", "This is from the backend")

        return "page/index"
    }
}
