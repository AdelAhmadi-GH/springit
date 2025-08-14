package com.adelahmadi.springit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/foo")
    public String foo(Model model) {
        model.addAttribute("pageTitle", "Foo Page");
        return "foo";
    }
}
