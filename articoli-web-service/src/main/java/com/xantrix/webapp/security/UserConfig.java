package com.xantrix.webapp.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("guestuser")
@Data
public class UserConfig {
    private String url;
    private String user;
    private String password;
}
