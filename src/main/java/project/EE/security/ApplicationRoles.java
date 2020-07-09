package project.EE.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

public enum ApplicationRoles {
    ADMIN(Sets.newHashSet(new SimpleGrantedAuthority("ROLE_ADMIN"))),
    SUPER_OPERATOR(Sets.newHashSet(new SimpleGrantedAuthority("ROLE_SUPER_OPERATOR"))),
    OPERATOR(Sets.newHashSet(new SimpleGrantedAuthority("ROLE_OPERATOR")));

    private final Set<SimpleGrantedAuthority> roles;

    ApplicationRoles(Set<SimpleGrantedAuthority> roles) {
        this.roles = roles;
    }

    public Set<SimpleGrantedAuthority> getRoles() {
        return roles;
    }
}
