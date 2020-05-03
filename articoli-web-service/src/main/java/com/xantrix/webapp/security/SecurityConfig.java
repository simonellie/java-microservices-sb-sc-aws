package com.xantrix.webapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] USER_MATCHER = {"/api/item/search/**"};
    private static final String[] ADMIN_MATCHER = {"/api/item/add/**", "/api/item/edit/**", "/api/item/delete/**"};
    private static final String REALM = "REAME";

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserBuilder users = User.builder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        // users creation
        manager.createUser(users
                .username("ema")
                .password(new BCryptPasswordEncoder().encode("ema"))
                .roles("USER")
                .build());

        manager.createUser(users
                .username("admin")
                .password(new BCryptPasswordEncoder().encode("admin"))
                .roles("USER", "ADMIN")
                .build());

        return manager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(USER_MATCHER).hasAnyRole("USER")
                .antMatchers(ADMIN_MATCHER).hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public AuthEntryPoint getBasicAuthEntryPoint() {
        return new AuthEntryPoint();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
