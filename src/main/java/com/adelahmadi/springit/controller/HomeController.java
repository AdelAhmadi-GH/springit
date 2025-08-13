package com.adelahmadi.springit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// @RestController is a convenience annotation that combines @Controller and
// @ResponseBody, indicating that the class is a controller where every method
// returns a domain object instead of a view.
// This means that the methods in this class will return data directly to the
// client, typically in JSON format.
// @RestController

/*********************************************/
// @Controller is a specialized version of the @Component annotation,
// indicating that the class is a Spring MVC controller.
// It is used to define a controller that handles web requests and returns views
// or data.
// In this case, the HomeController class is annotated with @Controller,
// indicating
// that it will handle web requests related to the home page of the application.
@Controller
public class HomeController {

    @RequestMapping("/")
    // The @RequestMapping annotation is used to map HTTP requests to handler
    // methods of MVC and REST controllers.
    // In this case, it maps the root URL ("/") to the home() method.
    public String home() {

        return "index"; // This will return the view named "index" to be rendered.
        // The view resolver will look for a template named "index" in the configured
        // template directory (e.g., src/main/resources/templates).
        // If you want to return a simple string instead of a view, you can use
        // @ResponseBody annotation.

        /**********************************************************/
        // Just for testing purposes, we return a simple string by using the
        // @RequestMapping annotation.
        // In a real application, you would typically return a view name or a model
        // return "Welcome to Springit!";
    }
}
