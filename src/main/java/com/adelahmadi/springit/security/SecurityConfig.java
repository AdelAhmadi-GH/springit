// src\main\java\com\adelahmadi\springit\security\SecurityConfig.java
package com.adelahmadi.springit.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.adelahmadi.springit.bootstrap.InitialRoles;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = false, prePostEnabled = false, jsr250Enabled = false)
public class SecurityConfig {

        // SecurityConfig.java
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // DEV-ONLY: allow H2 console frames and skip CSRF for it (remove after tests)
                                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))

                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(EndpointRequest.to("info")).permitAll()
                                                .requestMatchers(EndpointRequest.toAnyEndpoint())
                                                .hasRole(InitialRoles.ADMIN)
                                                .requestMatchers("/actuator/").hasRole(InitialRoles.ADMIN)
                                                .requestMatchers("/", "/h2-console/**").permitAll() // DEV-ONLY: open H2
                                                                                                    // console
                                                .requestMatchers("/link/submit")
                                                .hasAnyRole(InitialRoles.USER, InitialRoles.ADMIN)
                                                .anyRequest().permitAll())

                                // Use default generated login page
                                // Configure form login
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                                                .defaultSuccessUrl("/", true)
                                                .failureUrl("/login?error"))

                                // Use default logout configuration
                                // Configure logout
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/")
                                                .permitAll())

                                .rememberMe(remember -> remember
                                                .key("uniqueAndSecretKey123") // Identifier for the remember-me token
                                                .tokenValiditySeconds(14 * 24 * 60 * 60) // 14 days
                                                .rememberMeParameter("remember-me") // parameter name in the login form
                                                .rememberMeCookieName("remember-me-cookie") // cookie name
                                )

                                // Logout config
                                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").permitAll());

                return http.build();
        }

        // DelegatingPasswordEncoder supports {bcrypt}, {noop}, ...
        // DelegatingPasswordEncoder supports {bcrypt}, {noop}, ...
        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }
}
