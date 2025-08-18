package com.adelahmadi.springit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.adelahmadi.springit.domain.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {

}
