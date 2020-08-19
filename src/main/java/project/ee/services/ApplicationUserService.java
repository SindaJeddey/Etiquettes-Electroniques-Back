package project.ee.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.ee.models.authentication.ApplicationUser;
import project.ee.models.authentication.User;
import project.ee.repositories.UserRepository;

@Service
public class ApplicationUserService implements UserDetailsService {

    //Testing ...
    private final UserRepository userRepository;

    public ApplicationUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException("Username "+s+" not found"));
        ApplicationUser applicationUser = new ApplicationUser(user);
        return applicationUser;
    }
}