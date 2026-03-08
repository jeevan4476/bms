package com.bms.config;

import org.h2.server.web.JakartaWebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/h2-console/**").permitAll()
                    .anyRequest().permitAll()
            );

        return http.build();
    }

    @Bean
    ServletRegistrationBean<JakartaWebServlet> h2Console() {

        ServletRegistrationBean<JakartaWebServlet> registration =
                new ServletRegistrationBean<>(new JakartaWebServlet(), "/h2-console/*");

        registration.addInitParameter("-webAllowOthers", "true");
        registration.setLoadOnStartup(1);

        return registration;
    }
}