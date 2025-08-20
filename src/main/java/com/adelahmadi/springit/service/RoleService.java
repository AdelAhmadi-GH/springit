package com.adelahmadi.springit.service;

import org.springframework.stereotype.Service;

import com.adelahmadi.springit.domain.Role;
import com.adelahmadi.springit.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Role save(Role role) {
        Role existing = roleRepository.findByName(role.getName());
        return (existing != null) ? existing : roleRepository.save(role);
    }

    public long count() {
        return roleRepository.count();
    }

}
