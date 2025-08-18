package com.adelahmadi.springit.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.adelahmadi.springit.domain.User;
import com.adelahmadi.springit.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleService roleService;
    private MailService mailService;

    public UserService(UserRepository userRepository, RoleService roleService, MailService mailService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.mailService = mailService;
        encoder = new BCryptPasswordEncoder();
    }

    public User register(User user) {
        // take the password from the form and encode
        String secret = "{bcrypt}" + encoder.encode(user.getPassword());
        user.setPassword(secret);
        // confirm password
        // user.setConfirmPassword(secret);

        // assign a role to this user
        user.addRole(roleService.findByName("ROLE_USER"));

        // set an activation code
        // user.setActivationCode(UUID.randomUUID().toString());

        // disable the user
        user.setEnabled(false);
        // save user
        save(user);

        // send the activation email
        // sendActivationEmail(user);

        // return the user
        return user;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void saveUsers(User... users) {
        for (User user : users) {
            logger.info("Saving User: {}", user.getEmail());
            userRepository.save(user);
        }
    }

    // public void sendActivationEmail(User user) {
    // mailService.sendActivationEmail(user);
    // }

    // public void sendWelcomeEmail(User user) {
    // mailService.sendWelcomeEmail(user);
    // }

    // public Optional<User> findByEmailAndActivationCode(String email, String
    // activationCode) {
    // return userRepository.findByEmailAndActivationCode(email, activationCode);
    // }

}
