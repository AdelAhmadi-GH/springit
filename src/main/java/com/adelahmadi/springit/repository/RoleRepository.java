package com.adelahmadi.springit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adelahmadi.springit.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
