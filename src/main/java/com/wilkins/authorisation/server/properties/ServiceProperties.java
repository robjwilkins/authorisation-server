package com.wilkins.authorisation.server.properties;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "service")
@Data
@NoArgsConstructor
public class ServiceProperties {
    private String signingKey;
    private String securityRealm;
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String resourceId;
    private int accessTokenValidSeconds;
    private int refreshTokenValidSeconds;
}
