package com.adelahmadi.springit.service;

import org.springframework.stereotype.Service;

import com.adelahmadi.springit.domain.Comment;
import com.adelahmadi.springit.repository.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
}
