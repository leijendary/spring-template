package com.leijendary.spring.microservicetemplate.config;

import com.leijendary.spring.microservicetemplate.factory.mapper.SampleRequestV1ToSampleDataMap;
import org.modelmapper.ModelMapper;
import org.modelmapper.module.jsr310.Jsr310Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static org.modelmapper.Conditions.isNotNull;
import static org.modelmapper.convention.MatchingStrategies.STRICT;
import static org.modelmapper.module.jsr310.Jsr310ModuleConfig.builder;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        final var modelMapper = new ModelMapper();
        // Configuration setup
        modelMapper.getConfiguration()
                // Matching strategy set to "STRICT" to avoid mismatching of fields
                .setMatchingStrategy(STRICT)
                // Skip if the property is already null
                .setPropertyCondition(isNotNull());
        // Module registration
        modelMapper.registerModule(jsr310Module());
        // Mappings
        // SampleRequestV1 to SampleData mapping
        modelMapper.addMappings(new SampleRequestV1ToSampleDataMap());

        return modelMapper;
    }

    private Jsr310Module jsr310Module() {
        final var jsr310ModuleConfig = builder()
                .dateTimeFormatter(ISO_OFFSET_DATE_TIME)
                .build();

        return new Jsr310Module(jsr310ModuleConfig);
    }
}
