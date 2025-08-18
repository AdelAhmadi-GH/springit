package com.adelahmadi.springit.config;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import com.adelahmadi.springit.controller.VoteController;
import com.adelahmadi.springit.domain.User;

public class AuditorAwareImpl implements AuditorAware<String> {
    private static final Logger logger = LoggerFactory.getLogger(VoteController.class);

    @Override
    @org.springframework.lang.NonNull
    public Optional<String> getCurrentAuditor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of("master@gmail.com"); // Default auditor if no user is authenticated
        }
        return Optional.ofNullable(((User) authentication.getPrincipal()).getEmail());
    }
}
