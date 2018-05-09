package com.aurea.boot.autoconfigure.api.user.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "api.user")
public class ApiUserProps {

    private String uiUrl = "http://localhost:4000";
}
