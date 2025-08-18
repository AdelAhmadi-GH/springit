package com.adelahmadi.springit.service;

import org.springframework.stereotype.Service;

import com.adelahmadi.springit.domain.Vote;
import com.adelahmadi.springit.repository.VoteRepository;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }

}
