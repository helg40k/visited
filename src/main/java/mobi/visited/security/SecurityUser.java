package mobi.visited.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class SecurityUser implements UserDetails {

    private String username;
    private String password;
    private Collection<GrantedAuthority> authorities;

    public SecurityUser(String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.setRoles(roles);
    }

    public void setRoles(String roles) {
        this.authorities = new HashSet<>();
        for (String role : roles.split(",")) {
            if (role != null && !"".equals(role.trim())) {
                GrantedAuthority grandAuthority = new SimpleGrantedAuthority(role.trim());
                this.authorities.add(grandAuthority);
            }
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }
}
