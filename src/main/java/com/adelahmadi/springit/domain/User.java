// src/main/java/com/adelahmadi/springit/domain/User.java
package com.adelahmadi.springit.domain;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * User entity implementing Spring Security's UserDetails.
 * We use email as the authentication username (principal).
 * Roles are stored as simple strings via ElementCollection (String-based
 * model).
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = { "password", "roles" }) // Avoid logging sensitive fields
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String alias;

    @Column(nullable = false)
    private boolean enabled = false;

    private String activationCode;

    /** Store roles as simple strings in a separate collection table. */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false, length = 50)
    private Set<String> roles = new HashSet<>();

    /** Utility: full name. */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /** Normalize email on set (optional but helps consistency). */
    public void setEmail(String email) {
        this.email = (email == null) ? null : email.trim().toLowerCase(Locale.ROOT);
    }

    /* ========================= UserDetails ========================= */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null || roles.isEmpty()) {
            return Set.of();
        }
        return roles.stream()
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public String getUsername() {
        // We use email as the username
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // customize if you add fields for expiration
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // customize if you add fields for locking
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // customize if you add fields for credential expiration
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /* ========================= Role helpers ========================= */

    /** Add a role name directly, e.g., "ROLE_USER" or "USER". */
    public void addRole(String roleName) {
        if (roleName == null || roleName.isBlank())
            return;
        roles.add(roleName);
    }

    /** Add multiple role names at once. */
    public void addRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty())
            return;
        roles.addAll(roleNames);
    }

    /** Remove a role name. */
    public void removeRole(String roleName) {
        if (roleName == null || roleName.isBlank())
            return;
        roles.remove(roleName);
    }
}
