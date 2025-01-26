package com.leijendary.domain.ai.chat

import com.leijendary.extension.logger
import org.springframework.ai.model.function.FunctionCallback
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AiChatFunction {
    private val log = logger()

    data class Request(val userId: String)
    data class Response(val accountNumber: String)

    @Bean
    fun getAccountNumber(): FunctionCallback {
        return FunctionCallback.builder()
            .function("getAccountNumber") { input: Request, toolContext ->
                log.info("Get account number of user ID {}", input.userId)

                Response("8717823912")
            }
            .inputType(Request::class.java)
            .description("This function will return the user's account number")
            .build()
    }

    companion object {
        const val USER_ID_KEY = "userId"
    }
}
