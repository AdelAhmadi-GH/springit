package com.adelahmadi.springit.repository;

import com.adelahmadi.springit.domain.Link;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Long> {

    // Custom query methods can be defined here
    // This method finds a link by its title.
    // The method name follows Spring Data JPA's naming conventions to automatically
    // generate the query.
    Link findByTitle(String title);

    // // This method finds links by their title using a "like" query and orders
    // them
    // // by
    // // their creation date in descending order.
    // // The "Like" keyword allows for partial matching of the title.
    // // The "OrderBy" keyword specifies the ordering of the results.
    // // The method name is constructed to follow Spring Data JPA's conventions.
    // List<Link> findALinksByTitleLikeOrderLinksByCreatedDesc(String title);
}
