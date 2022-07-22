package com.anodiam.StudentSignup.serviceRepository.User;

import com.anodiam.StudentSignup.model.User;
import com.anodiam.StudentSignup.model.common.MessageResponse;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    MessageResponse save(User user);
}
