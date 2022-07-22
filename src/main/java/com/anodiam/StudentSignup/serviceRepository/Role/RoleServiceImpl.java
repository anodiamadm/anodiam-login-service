package com.anodiam.StudentSignup.serviceRepository.Role;

import com.anodiam.StudentSignup.model.Role;

import java.util.Optional;

abstract class RoleServiceImpl implements RoleService {

    @Override
    public Optional<Role> findByRoleName(String roleName) {
        return new RoleServiceDal().findByRoleName(roleName);
    }
}
