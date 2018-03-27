package com.aurea.kayako.api.config;

import com.aurea.kayako.api.ApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApiProperties.class)
public class PropertiesConfig {
}
