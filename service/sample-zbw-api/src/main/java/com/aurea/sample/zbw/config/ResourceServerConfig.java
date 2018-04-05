package com.aurea.sample.zbw.config;

import lombok.extern.java.Log;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Log
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public void configure(HttpSecurity http) {
        try {
            http.authorizeRequests()
                    .antMatchers("/swagger-ui.html").permitAll()
                    .antMatchers("/api/data/browser/**").permitAll()
                    .antMatchers("/api/users/forgot-password").permitAll()
                    .antMatchers("/api/users/reset-password").permitAll()
                    .antMatchers("/oauth/check-reset-token").permitAll()
                    .antMatchers("/oauth/**").authenticated()
                    .antMatchers("/api/**").authenticated();
        } catch (Exception e) {
            log.severe("There is a problem configuring resources access");
        }
    }
}
