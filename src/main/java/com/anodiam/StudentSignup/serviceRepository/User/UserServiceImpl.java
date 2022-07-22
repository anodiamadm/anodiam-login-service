package com.anodiam.StudentSignup.serviceRepository.User;

import com.anodiam.StudentSignup.model.User;
import com.anodiam.StudentSignup.model.common.MessageResponse;

import java.util.Optional;

abstract class UserServiceImpl implements UserService {

    @Override
    public Optional<User> findByUsername(String username) {
        return new UserServiceDal().findByUsername(username);
    }

    @Override
    public MessageResponse save(User user) {
        return new UserServiceDal().save(user);
    }
}
