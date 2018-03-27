package com.aurea.zbw.api;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("zbw")  // If you change the prefix remember to update the application.properties
@Data
public final class ApiProperties {

    private Api api = new Api();

    @Data
    public static final class Api {

        private String title;
        private String license;
        private String version;

        private Swagger swagger;
        private Security security;

        @Data
        public static final class Swagger {
            private String uiPath;
            private String resourcesPath1;
            private String resourcesPath2;
        }

        @Data
        public static final class Security {
            private Cors cors;
            private Authentication authentication;
            private Jwt jwt;

            @Data
            public static final class Cors {
                private List<String> allowedOrigins;
                private List<String> allowedMethods;
                private List<String> allowedHeaders;
            }

            @Data
            public static final class Authentication {
                private String type;
                private String loginPath;
                private String responseHeader;
            }

            @Data
            public static final class Jwt {
                private String secret;
                private Long expiration;
            }
        }
    }

}
