package com.anodiam.StudentSignup.serviceRepository.Role;

import com.anodiam.StudentSignup.model.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByRoleName(String roleName);
}
