package com.anodiam.StudentSignup.security;

import com.anodiam.StudentSignup.serviceRepository.User.UserRepository;
import com.anodiam.StudentSignup.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailService implements UserDetailsService {

    private UserRepository userRepository;

    public UserPrincipalDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username).get();
        UserPrincipal userPrincipal = new UserPrincipal(user);
        return userPrincipal;
    }
}
