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

import com.adelahmadi.springit.domain.Comment;
import com.adelahmadi.springit.domain.Link;
import com.adelahmadi.springit.repository.CommentRepository;
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
    private final CommentRepository commentRepository;
    // Constant for the success attribute key
    private static final String ATTR_SUCCESS = "success";

    public LinkController(LinkRepository linkRepository, CommentRepository commentRepository) {
        this.linkRepository = linkRepository;
        this.commentRepository = commentRepository;
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

    // The @GetMapping annotation is used to handle HTTP GET requests.
    // In this case, it maps the method read() to the HTTP GET request at the URL
    @GetMapping("/link/{id}")
    public String read(@PathVariable("id") Long linkId, Model model) {
        Optional<Link> link = linkRepository.findById(linkId);
        if (link.isEmpty())
            return "redirect:/";
        Link currentLink = link.get();
        Comment comment = new Comment();
        comment.setLink(currentLink); // <-- مهم

        model.addAttribute("comment", comment);
        model.addAttribute("link", currentLink);
        model.addAttribute(ATTR_SUCCESS, model.containsAttribute(ATTR_SUCCESS));
        return "link/link-comments";
    }

    @PostMapping("/link/comments")
    public String addComment(@Valid Comment comment, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("There was problem adding a new comment.");
        } else {
            commentRepository.save(comment);
            logger.info("New Comment was saved successfully!");
        }
        return "redirect:/link/" + comment.getLink().getLinkId();
    }

}
