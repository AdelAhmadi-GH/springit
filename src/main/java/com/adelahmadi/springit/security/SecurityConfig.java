package com.adelahmadi.springit.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.adelahmadi.springit.bootstrap.InitialRoles;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = false, prePostEnabled = false, jsr250Enabled = false)
public class SecurityConfig {

    private UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // SecurityConfig.java
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // DEV-ONLY: allow H2 console frames and skip CSRF for it (remove after tests)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(EndpointRequest.to("info")).permitAll()
                        .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole(InitialRoles.ADMIN)
                        .requestMatchers("/actuator/").hasRole(InitialRoles.ADMIN)
                        .requestMatchers("/", "/h2-console/**").permitAll() // DEV-ONLY: open H2 console
                        .requestMatchers("/link/submit").hasAnyRole(InitialRoles.USER, InitialRoles.ADMIN)
                        .anyRequest().permitAll())

                // Use default generated login page
                // Configure form login
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll().usernameParameter("email") // Use email as username
                        .passwordParameter("password")
                // Uncomment the next line if you want to redirect to home after login
                // .defaultSuccessUrl("/", true) // if needed, redirect to home after login
                )

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

    // Supports {bcrypt}, {noop}, etc.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // This method is overridden to ensure the authentication manager is configured
        // when the application starts.
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
