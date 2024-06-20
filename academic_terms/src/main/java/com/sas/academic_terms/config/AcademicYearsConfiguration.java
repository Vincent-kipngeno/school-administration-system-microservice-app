package com.sas.academic_terms.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AcademicYearsConfiguration {
    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.routing-keys.internal-grade-term-fees}")
    private String internalGradeTermFeesRoutingKey;

    @Value("${rabbitmq.routing-keys.internal-fees}")
    private String internalFeesRoutingKey;
}
