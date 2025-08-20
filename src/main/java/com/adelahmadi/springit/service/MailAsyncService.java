package com.adelahmadi.springit.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adelahmadi.springit.domain.User;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailAsyncService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private static final String BASE_URL_GLOBAL = "baseURL";
    private static final String ENCODE = "UTF-8";
    private static final String NOREPLY_EMAIL = "noreply@springit.com";
    private final Logger logger = LoggerFactory.getLogger(MailAsyncService.class);

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultiPart, boolean isHtml) {
        if (to == null || to.isBlank()) {
            logger.warn("Skipping email: missing recipient address");
            return;
        }
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, isMultiPart, ENCODE);
            helper.setTo(to);
            helper.setFrom(NOREPLY_EMAIL);
            helper.setSubject(subject != null ? subject : "");
            helper.setText(content != null ? content : "", isHtml);
            javaMailSender.send(mimeMessage);
            logger.debug("Email sent to '{}'", to);
        } catch (Exception e) {
            logger.warn("Email could not be sent to '{}': {}", to, e.getMessage());
            logger.debug("Email send failure", e);
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String template, String subject, String baseUrl) {
        if (user == null || user.getEmail().isBlank()) {
            logger.warn("Skipping template email: user or user email is null/blank");
            return;
        }
        try {
            Locale locale = Locale.ENGLISH;
            Context ctx = new Context(locale);
            ctx.setVariable("user", user);
            ctx.setVariable(BASE_URL_GLOBAL, baseUrl);

            String content = templateEngine.process(template, ctx); // HTML body
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, ENCODE);
            helper.setTo(user.getEmail());
            helper.setFrom(NOREPLY_EMAIL);
            helper.setSubject(subject != null ? subject : "");
            helper.setText(content, true);
            javaMailSender.send(mimeMessage);

            logger.debug("Templated email '{}' sent to '{}'", template, user.getEmail());
        } catch (Exception e) {
            logger.warn("Template email '{}' could not be sent to '{}': {}", template, user.getEmail(), e.getMessage());
            logger.debug("Template email send failure", e);
        }
    }

    @Async
    public void sendActivationEmail(User user, String baseUrl) {
        if (user == null || user.getEmail().isBlank()) {
            logger.warn("Skipping activation email: user or user email is null/blank");
            return;
        }
        try {
            logger.debug("Sending activation email to '{}'", user.getEmail());
            Locale locale = Locale.ENGLISH;
            Context ctx = new Context(locale);
            ctx.setVariable("user", user);
            ctx.setVariable(BASE_URL_GLOBAL, baseUrl);

            String content = templateEngine.process("email/activation", ctx);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, ENCODE);
            helper.setTo(user.getEmail());
            helper.setFrom(NOREPLY_EMAIL);
            helper.setSubject("Springit User Activation");
            helper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            logger.warn("Activation email could not be sent to '{}': {}", user.getEmail(), e.getMessage());
            logger.debug("Activation email send failure", e);
        }
    }

    @Async
    public void sendWelcomeEmail(User user, String baseUrl) {
        if (user == null || user.getEmail().isBlank()) {
            logger.warn("Skipping welcome email: user or user email is null/blank");
            return;
        }
        try {
            logger.debug("Sending welcome email to '{}'", user.getEmail());
            Locale locale = Locale.ENGLISH;
            Context ctx = new Context(locale);
            ctx.setVariable("user", user);
            ctx.setVariable(BASE_URL_GLOBAL, baseUrl);

            String content = templateEngine.process("email/welcome", ctx);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, ENCODE);
            helper.setTo(user.getEmail());
            helper.setFrom(NOREPLY_EMAIL);
            helper.setSubject("Welcome new Springit User");
            helper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            logger.warn("Welcome email could not be sent to '{}': {}", user.getEmail(), e.getMessage());
            logger.debug("Welcome email send failure", e);
        }
    }
}
