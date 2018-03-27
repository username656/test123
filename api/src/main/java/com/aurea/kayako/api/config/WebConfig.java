package com.aurea.kayako.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "com.aurea.kayako.api.controllers" })
public class WebConfig {
}
