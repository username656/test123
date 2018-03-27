package com.aurea.zbw.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "com.aurea.zbw.api.controllers" })
public class WebConfig {
}
