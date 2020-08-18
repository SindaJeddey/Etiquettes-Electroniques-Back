package project.EE.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.EE.models.authentication.ApplicationUser;
import project.EE.models.authentication.User;
import project.EE.repositories.UserRepository;

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
