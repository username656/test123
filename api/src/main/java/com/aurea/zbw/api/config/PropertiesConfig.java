package com.aurea.zbw.api.config;

import com.aurea.zbw.api.ApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApiProperties.class)
public class PropertiesConfig {
}
