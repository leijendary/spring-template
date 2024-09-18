package com.leijendary.container

import org.testcontainers.ollama.OllamaContainer
import org.testcontainers.utility.DockerImageName

class OllamaContainerInitializer : OllamaContainer(image) {
    override fun start() {
        super.start()

        execInContainer("ollama", "pull", "all-minilm")

        System.setProperty("spring.ai.ollama.baseUrl", endpoint)
    }

    companion object {
        val INSTANCE: OllamaContainerInitializer by lazy { OllamaContainerInitializer() }

        private val image = DockerImageName.parse("ollama/ollama:0.3.11")
    }
}
