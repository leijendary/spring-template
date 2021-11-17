package com.leijendary.spring.boot.template.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

@Configuration
@ConditionalOnClass(KafkaStreamsConfiguration.class)
@EnableKafka
public class KafkaAutoConfiguration {
}
