// src\main\java\com\adelahmadi\springit\service\UserService.java
package com.adelahmadi.springit.service;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.adelahmadi.springit.domain.User;
import com.adelahmadi.springit.repository.UserRepository;
import com.adelahmadi.springit.web.dto.RegisterRequest;

import jakarta.transaction.Transactional;

@Service
@lombok.RequiredArgsConstructor // generates constructor for all final fields
public class UserService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserService.class);

    // repositories & services
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final MailService mailService; // keep it final so Lombok injects it

    @Transactional
    public User register(RegisterRequest req) {
        String email = req.getEmail().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setEmail(email);
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setAlias(req.getAlias());

        user.setPassword(passwordEncoder.encode(req.getPassword()));

        user.setEnabled(false);
        user.setActivationCode(UUID.randomUUID().toString());

        user.addRole(roleService.findByName("ROLE_USER").getName());

        User saved = userRepository.save(user);

        sendActivationEmail(saved);

        return saved;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void saveUsers(User... users) {
        for (User user : users) {
            logger.info("Saving User: {}", user.getEmail());
            userRepository.save(user);
        }
    }

    public void sendActivationEmail(User user) {
        mailService.sendActivationEmail(user);
    }

    public void sendWelcomeEmail(User user) {
        mailService.sendWelcomeEmail(user);
    }

    public long count() {
        return userRepository.count();
    }

    public Optional<User> findByEmailAndActivationCode(String email, String activationCode) {
        return userRepository.findByEmailAndActivationCode(email, activationCode);
    }

    public boolean existsByEmail(String userEmail) {
        return userRepository.existsByEmail(userEmail.trim().toLowerCase(Locale.ROOT));
    }

    public Optional<User> findByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail.toLowerCase(Locale.ROOT));
    }

}
