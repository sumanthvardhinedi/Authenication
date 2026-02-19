package dev.sumanth.manual_authentication.Security;

import dev.sumanth.manual_authentication.models.User;
import dev.sumanth.manual_authentication.repos.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.Optional;

@Service
public class CustomUserService implements UserDetailsService {

    private UserRepo userRepo;
    public CustomUserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       Optional<User> optionalUser= userRepo.findByEmail(email);
       if(optionalUser.isEmpty()){
           throw new UsernameNotFoundException("User not found with email: " + email);
       }
        return new CustomUserDetails(optionalUser.get());
    }
}
