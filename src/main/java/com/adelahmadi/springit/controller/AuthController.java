package com.adelahmadi.springit.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.adelahmadi.springit.domain.User;
import com.adelahmadi.springit.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.core.model.Model;
import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

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
    public String register() {
        return "auth/register";
    }

    // @PostMapping("/register")
    // public String registerNewUser(@Valid User user, BindingResult bindingResult,
    // Model model,
    // RedirectAttributes redirectAttributes) {
    // if (bindingResult.hasErrors()) {
    // // show validation errors
    // logger.info("Validation errors were found while registering a new user");
    // model.addAttribute("user", user);
    // model.addAttribute("validationErrors", bindingResult.getAllErrors());
    // return "auth/register";
    // } else {
    // // Register new user
    // User newUser = userService.register(user);
    // redirectAttributes
    // .addAttribute("id", newUser.getId())
    // .addFlashAttribute("success", true);
    // return "redirect:/register";
    // }
    // }

    // @GetMapping("/activate/{email}/{activationCode}")
    // public String activate(@PathVariable String email, @PathVariable String
    // activationCode) {
    // Optional<User> user = userService.findByEmailAndActivationCode(email,
    // activationCode);
    // if (user.isPresent()) {
    // User newUser = user.get();
    // newUser.setEnabled(true);
    // newUser.setConfirmPassword(newUser.getPassword());
    // userService.save(newUser);
    // userService.sendWelcomeEmail(newUser);
    // return "auth/activated";
    // }
    // return "redirect:/";
    // }

}
