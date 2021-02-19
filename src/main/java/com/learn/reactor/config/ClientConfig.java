package com.learn.reactor.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class ClientConfig {
    @Value("${EventTime}")
    private int eventTime;
}
