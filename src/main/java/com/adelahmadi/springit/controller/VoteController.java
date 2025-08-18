package com.adelahmadi.springit.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.adelahmadi.springit.domain.Link;
import com.adelahmadi.springit.domain.Vote;
import com.adelahmadi.springit.repository.LinkRepository;
import com.adelahmadi.springit.repository.VoteRepository;

import jakarta.annotation.security.PermitAll;

import org.springframework.security.core.Authentication;

// We use @RestController to indicate that this class is a REST controller
// This means it will handle HTTP requests and return JSON responses
// Becaus wie need to implement methods for handling votes, we will define them later
// For now, we can leave this class empty or add some basic methods to handle votes
@RestController
public class VoteController {

    private static final Logger logger = LoggerFactory.getLogger(VoteController.class);
    private static final String ALLOWED_VOTER_EMAIL = "user@gmail.com";

    private final VoteRepository voteRepository;
    private final LinkRepository linkRepository;

    public VoteController(VoteRepository voteRepository, LinkRepository linkRepository) {
        this.voteRepository = voteRepository;
        this.linkRepository = linkRepository;
    }

    // Explane:
    // http://localhost:8080/votes/link/{linkId}/voteType/{voteType}/votecount/{votecount}
    // This method will return the total vote count for a specific link
    // The linkId is the ID of the link for which we want to get the vote count
    // The voteType is either -1 for downvote or 1 for upvote
    // The votecount is the total number of votes for that link
    // We will implement this method to calculate the total vote count for a link
    // @Secured(value = { "ROLE_USER" })
    // @GetMapping("/votes/link/{linkId}/voteType/{voteType}/votecount/{votecount}")
    // // This method will return the total vote count for a specific link
    // // The linkId is the ID of the link for which we want to get the vote count
    // public int getVoteCount(@PathVariable Long linkId,
    // @PathVariable short voteType,
    // @PathVariable int votecount) {
    // Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    // if (auth != null) {
    // logger.info("Current user: {} | Roles: {}",
    // auth.getName(),
    // auth.getAuthorities());
    // } else {
    // logger.info("Authentication is Null!");
    // }
    // Optional<Link> linkOptional = linkRepository.findById(linkId);
    // if (linkOptional.isPresent()) {
    // Link link = linkOptional.get();
    // Vote vote = new Vote(link);
    // vote.setVoteType(voteType);
    // voteRepository.save(vote);

    // int updatedVoteCount = votecount + voteType;
    // link.setVoteCount(updatedVoteCount);
    // linkRepository.save(link);
    // return updatedVoteCount;
    // }
    // return votecount;
    // }

    @PermitAll
    @GetMapping(value = "/votes/link/{linkId}/voteType/{voteType}/votecount/{votecount}", produces = "application/json")
    public ResponseEntity<Integer> getVoteCount(@PathVariable Long linkId,
            @PathVariable short voteType,
            @PathVariable int votecount,
            Authentication auth) {
        // Step 1: Get the current vote count from the database (fallback to client
        // value if not found)
        int currentCount = linkRepository.findById(linkId)
                .map(Link::getVoteCount)
                .orElse(votecount);

        // Step 2: Block unauthenticated or anonymous users (return the current count
        // without changes)
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            logger.info("Vote blocked: anonymous. Returning currentCount={}", currentCount);
            return ResponseEntity.ok(Integer.valueOf(currentCount));
        }

        // Step 3: Allow only a specific email with ROLE_USER to vote
        String email = auth.getName();
        boolean hasUserRole = auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_USER".equals(a.getAuthority()));

        if (!ALLOWED_VOTER_EMAIL.equalsIgnoreCase(email) || !hasUserRole) {
            logger.info("Vote blocked: email={} roles={} Returning currentCount={}",
                    email, auth.getAuthorities(), currentCount);
            return ResponseEntity.ok(Integer.valueOf(currentCount));
        }

        // Step 4: Apply vote for authorized user and update database
        return linkRepository.findById(linkId)
                .map(link -> {
                    Vote vote = new Vote(link);
                    vote.setVoteType(voteType);
                    voteRepository.save(vote);

                    int updated = currentCount + voteType;
                    link.setVoteCount(updated);
                    linkRepository.save(link);

                    logger.info("Vote applied: email={} newCount={}", email, updated);
                    return ResponseEntity.ok(Integer.valueOf(updated));
                })
                .orElseGet(() -> {
                    logger.warn("Link not found: linkId={}", linkId);
                    return ResponseEntity.ok(Integer.valueOf(currentCount));
                });
    }
}
