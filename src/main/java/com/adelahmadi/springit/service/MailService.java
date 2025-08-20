package com.adelahmadi.springit.service;

import org.springframework.stereotype.Service;

import com.adelahmadi.springit.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

    private static final String BASE_URL = "http://localhost:8080";
    private final MailAsyncService mailAsync;

    public void sendEmailFromTemplate(User user, String template, String subject) {
        mailAsync.sendEmailFromTemplate(user, template, subject, BASE_URL);
    }

    public void sendActivationEmail(User u) {
        mailAsync.sendActivationEmail(u, BASE_URL);
    }

    public void sendWelcomeEmail(User u) {
        mailAsync.sendWelcomeEmail(u, BASE_URL);
    }
}
