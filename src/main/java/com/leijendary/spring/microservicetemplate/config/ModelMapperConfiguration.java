package com.leijendary.spring.microservicetemplate.config;

import com.leijendary.spring.microservicetemplate.factory.mapper.SampleRequestV1ToSampleTableMap;
import org.modelmapper.ModelMapper;
import org.modelmapper.module.jsr310.Jsr310Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static org.modelmapper.convention.MatchingStrategies.STRICT;
import static org.modelmapper.module.jsr310.Jsr310ModuleConfig.builder;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        final var config = builder()
                .dateTimeFormatter(ISO_OFFSET_DATE_TIME)
                .build();
        final var modelMapper = new ModelMapper();
        modelMapper.registerModule(new Jsr310Module(config));
        // Matching strategy set to "STRICT" to avoid mismatching of fields
        modelMapper.getConfiguration().setMatchingStrategy(STRICT);
        modelMapper.addMappings(new SampleRequestV1ToSampleTableMap());

        return modelMapper;
    }
}
