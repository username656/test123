package com.aurea.boot.autoconfigure.api.user.config;

import com.aurea.boot.autoconfigure.api.user.config.props.ApiUserProps;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApiUserProps.class)
@ComponentScan(basePackages = "com.aurea.boot.autoconfigure.api.user")
public class ApiUserAutoConfiguration {

}
