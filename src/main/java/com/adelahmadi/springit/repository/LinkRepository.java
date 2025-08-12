package com.adelahmadi.springit.repository;

import com.adelahmadi.springit.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Long> {
}
