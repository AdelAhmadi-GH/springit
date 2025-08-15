// Role.java
package com.adelahmadi.springit.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

/**
 * Role entity as an authority dictionary.
 * No association to User here because User uses a String-based role model.
 * This avoids mappedBy mismatches at runtime.
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @NonNull
    @Size(min = 3, max = 32, message = "Role name must be between 3 and 32 characters")
    @Column(nullable = false, unique = true, length = 32)
    private String name; // e.g., "ADMIN" or "ROLE_ADMIN"

    @Override
    public String getAuthority() {
        // Normalize to Spring Security convention
        return name.startsWith("ROLE_") ? name : "ROLE_" + name;
    }
}
