package com.xantrix.webapp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.core.userdetails.User.UserBuilder;

import java.net.URI;
import java.net.URISyntaxException;

// it's a service, but it's strictly related to security!!!
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserConfig userConfig;

    @Override
    public UserDetails loadUserByUsername(String userID) throws UsernameNotFoundException {
        if (userID == null || userID.isEmpty()) {
            String error = "Invalid username.";
            logger.warn(error);
            throw new UsernameNotFoundException(error);
        }

        AuthUser authUser = this.getHttpValue(userID);
        if (authUser == null) {
            String error = "Username not found!";
            logger.warn(error);
            throw new UsernameNotFoundException(error);
        }

        UserBuilder builder = User.withUsername(authUser.getUserId());
        builder.disabled(authUser.getActive().equals("Yes") ? false : true);
        builder.password(authUser.getPassword());
        String[] profiles = authUser.getRoles().stream().map(role -> "ROLE_" + role).toArray(String[]::new);
        builder.authorities(profiles);

        return builder.build();
    }


    private AuthUser getHttpValue(String userID) {

        URI url = null;

        try {
            String userServiceUrl = userConfig.getUrl();
            url = new URI(userServiceUrl + userID);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors()
                .add(new BasicAuthenticationInterceptor(userConfig.getUser(), userConfig.getPassword()));

        AuthUser authUser = null;

        try {
            authUser = restTemplate.getForObject(url, AuthUser.class);
        } catch (RestClientException e) {
            String error = String.format("Connection to authentication service failed: %s", e.getMessage());
            logger.error(error);
        }

        return authUser;
    }
}
