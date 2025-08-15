package com.adelahmadi.springit.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.adelahmadi.springit.domain.Link;
import com.adelahmadi.springit.repository.LinkRepository;
import jakarta.validation.Valid;

// @RequestMapping is used to map web requests to specific handler methods in a controller.
// In this case, it maps all requests starting with "/links" to the methods in this class.
// This means that any HTTP request to "/links" will be handled by the methods in this controller.
@Controller
// @RequestMapping("/links")
public class LinkController {
    private static final Logger logger = LoggerFactory.getLogger(LinkController.class);

    private final LinkRepository linkRepository;
    // Constant for the success attribute key
    private static final String ATTR_SUCCESS = "success";

    public LinkController(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
        logger.info("LinkController initialized with LinkRepository");
    }

    // The @GetMapping annotation is used to handle HTTP GET requests.
    // In this case, it maps the method getAllLinks() to the HTTP GET request at the
    // URL "/links/".
    // This method is typically used to retrieve a list of resources, such as all
    // links in this case.
    // The method currently returns a string indicating the view name to be
    // rendered.
    // It does not return any data directly; instead, it relies on a view resolver
    // to render the appropriate view.
    @GetMapping("/")
    public String getAllLinks(Model model) {
        logger.info("Fetching all links");
        model.addAttribute("links", linkRepository.findAll());

        return "link/list"; // Assuming you have a Thymeleaf template named "list.html" to display links
    }

    // The @GetMapping annotation is used to handle HTTP GET requests.
    // In this case, it maps the method read() to the HTTP GET request at the URL
    @GetMapping("/link/{id}")
    public String read(@PathVariable("id") Long linkId, Model model) {
        logger.info("Fetching link with id: {}", linkId);
        Optional<Link> link = linkRepository.findById(linkId);
        if (link.isEmpty()) {
            logger.error("Link not found with id: {}", linkId);
            return "redirect:/"; // Redirect to the list of links if not found
        }
        model.addAttribute("link", link.get());
        logger.info("Link found: {}", link.get().getTitle());

        return "link/view";
    }

    @GetMapping("/link/submit")
    public String newLinkForm(Model model) {
        model.addAttribute("link", new Link());
        return "link/submit";
    }

    // Create
    @PostMapping("/link/submit")
    public String createLink(@Valid Link link, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            logger.info("Validation errors were found while submitting a new link.");
            model.addAttribute("link", link);
            return "link/submit";
        }
        // save our link
        linkRepository.save(link);
        logger.info("New link was saved successfully");
        redirectAttributes
                .addAttribute("id", link.getLinkId())
                .addFlashAttribute(ATTR_SUCCESS, true);
        return "redirect:/link/{id}";

    }

    // // ************ CRUD operations for Link entity as Examples ************//

    // @GetMapping("/all")
    // // The @GetMapping annotation is used to handle HTTP GET requests.
    // // In this case, it maps the method getAllLinks() to the HTTP GET request at
    // the
    // // URL "/links/all".
    // // This method is typically used to retrieve a list of resources, such as all
    // // links in this case.
    // // The method currently returns an empty list, indicating that it does not
    // yet
    // // implement the logic to retrieve links.
    // public List<Link> getAllLinks() {
    // return linkRepository.findAll();
    // // This will return a list of all Link entities from the database.
    // // If there are no links, it will return an empty list.
    // // The linkRepository is used to interact with the database and perform CRUD
    // // operations on Link entities.
    // }

    // @PutMapping("/create")
    // // The @PutMapping annotation is used to handle HTTP PUT requests.
    // // In this case, it maps the method createLink() to the HTTP PUT request at
    // the
    // // URL "/links/create".
    // // This method is typically used to create a new resource, such as a new link
    // in
    // // this case.
    // // The method currently returns null, indicating that it does not yet
    // implement
    // // the logic to create a link.
    // public Link createLink(@ModelAttribute Link link) {
    // return linkRepository.save(link);
    // }

    // @GetMapping("/{id}")
    // public Optional<Link> read(@PathVariable("id") Long linkId) {
    // logger.info("Fetching link with id: {}", linkId);
    // // The @GetMapping annotation is used to handle HTTP GET requests.
    // // In this case, it maps the method read() to the HTTP GET request at the URL
    // return linkRepository.findById(linkId);
    // // This method retrieves a specific Link entity by its ID.
    // // The @PathVariable annotation is used to extract the value of the "id" path
    // // variable from the URL.
    // // If the link with the specified ID is found, it returns an Optional
    // containing
    // // the Link entity.
    // // If the link is not found, it returns an empty Optional.
    // // You can handle the empty Optional case in your application logic as
    // needed.
    // // This method retrieves a specific Link entity by its ID.
    // // If the link is not found, it throws a RuntimeException with an appropriate
    // // message.
    // // The @PathVariable annotation is used to extract the value of the "id" path
    // // variable from the URL.
    // }

    // @PutMapping("/{id}")
    // // The @PutMapping annotation is used to handle HTTP PUT requests.
    // // In this case, it maps the method updateLink() to the HTTP PUT request at
    // the
    // public Link updateLink(@PathVariable("id") Long linkId, @ModelAttribute Link
    // link) {
    // // URL "/links/{id}".
    // // This method is typically used to update an existing resource, such as an
    // // existing link in this case.
    // // The method currently returns null, indicating that it does not yet
    // implement
    // // the logic to update a link.
    // Link existingLink = linkRepository.findById(linkId)
    // .orElseThrow(() -> new RuntimeException("Link not found with id: " +
    // linkId));
    // existingLink.setTitle(link.getTitle());
    // existingLink.setUrl(link.getUrl());
    // logger.info("Link updated successfully with id: {}", linkId);
    // // This method retrieves an existing Link entity by its ID, updates its title
    // // and URL, and then saves the updated link back to the database.
    // // If the link with the specified ID is not found, it throws a
    // RuntimeException
    // // with an appropriate message.
    // return linkRepository.save(existingLink);
    // }

    // @DeleteMapping("/{id}")
    // // The @DeleteMapping annotation is used to handle HTTP DELETE requests.
    // // In this case, it maps the method deleteLink() to the HTTP DELETE request
    // at
    // // the URL "/links/delete/{id}".
    // public void deleteLink(@PathVariable("id") Long linkId) {
    // // This method is typically used to delete an existing resource, such as an
    // // existing link in this case.
    // Link existingLink = linkRepository.findById(linkId)
    // .orElseThrow(() -> new RuntimeException("Link not found with id: " +
    // linkId));
    // linkRepository.delete(existingLink);
    // logger.info("Link deleted successfully");
    // }
}
