package com.adelahmadi.springit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.adelahmadi.springit.domain.Link;
import com.adelahmadi.springit.repository.LinkRepository;

@Service
public class LinkService {

    private final LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public List<Link> findAll() {
        return linkRepository.findAll();
    }

    public Optional<Link> findById(Long linkId) {
        return linkRepository.findById(linkId);
    }

    public Link save(Link link) {
        return linkRepository.save(link);
    }

}
