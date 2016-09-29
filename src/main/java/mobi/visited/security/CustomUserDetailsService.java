package mobi.visited.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import mobi.visited.service.UserService;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userManager;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userManager.getUserForSecurity(username);
    }
}
