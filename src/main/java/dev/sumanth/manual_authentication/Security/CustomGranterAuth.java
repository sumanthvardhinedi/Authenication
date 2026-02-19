package dev.sumanth.manual_authentication.Security;

import dev.sumanth.manual_authentication.models.Role;
//import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public class CustomGranterAuth implements GrantedAuthority {
    private Role role;
    public CustomGranterAuth(Role role) {
        this.role = role;
    }
    @Override
    public String getAuthority() {
        return role.getRole();
    }
}
