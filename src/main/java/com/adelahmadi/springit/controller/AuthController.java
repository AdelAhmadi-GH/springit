// src\main\java\com\adelahmadi\springit\controller\AuthController.java
package com.adelahmadi.springit.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.adelahmadi.springit.domain.User;
import com.adelahmadi.springit.service.UserService;
import com.adelahmadi.springit.web.dto.RegisterRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private static final String RGISTER_PATH = "auth/register";

    @GetMapping("/login")
    public String login() {
        // Return the login view
        return "auth/login"; // Ensure you have a login.html in your templates directory
    }

    @GetMapping("/profile")
    public String profile() {
        return "auth/profile";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("form", new RegisterRequest());
        logger.info("GET /register success? {}", model.asMap().get("success"));
        return RGISTER_PATH;
    }

    @PostMapping("/register")
    public String doRegister(
            @Valid @ModelAttribute("form") RegisterRequest form,
            BindingResult br,
            Model model) {
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            br.rejectValue("confirmPassword", "password.mismatch",
                    "Password & Password Confirmation do not match.");
        }

        if (br.hasErrors()) {
            return RGISTER_PATH;
        }

        try {
            userService.register(form);
            model.addAttribute("success", true);
            return RGISTER_PATH;
        } catch (IllegalArgumentException ex) {
            br.rejectValue("email", "email.exists", ex.getMessage());
            return RGISTER_PATH;
        }

    }

    @GetMapping("/activate/{email}/{activationCode}")
    public String activate(@PathVariable String email, @PathVariable String activationCode) {
        Optional<User> user = userService.findByEmailAndActivationCode(email, activationCode);
        if (user.isPresent()) {
            User newUser = user.get();
            newUser.setEnabled(true);
            userService.saveUser(newUser);
            userService.sendWelcomeEmail(newUser);
            return "auth/activated";
        }
        return "redirect:/";
    }

}
