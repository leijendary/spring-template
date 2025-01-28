package com.leijendary.domain.ai.chat

import com.leijendary.config.ToolContainer
import com.leijendary.extension.logger
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component

@Component
class AiChatFunction : ToolContainer {
    private val log = logger()

    @Tool(description = "Get the user's account number")
    fun getAccountNumber(userId: String): String {
        log.info("Get account number of user ID {}", userId)

        return "8717823912"
    }

    companion object {
        const val USER_ID_KEY = "userId"
    }
}
