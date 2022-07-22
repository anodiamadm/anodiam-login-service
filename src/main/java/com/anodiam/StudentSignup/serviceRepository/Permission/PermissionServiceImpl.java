package com.anodiam.StudentSignup.serviceRepository.Permission;

import com.anodiam.StudentSignup.model.Permission;

import java.util.Optional;

abstract class PermissionServiceImpl implements PermissionService {

    @Override
    public Optional<Permission> findByPermissionName(String permissionName) {
        return new PermissionServiceDal().findByPermissionName(permissionName);
    }
}
