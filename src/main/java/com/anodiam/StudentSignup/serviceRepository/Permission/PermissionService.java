package com.anodiam.StudentSignup.serviceRepository.Permission;

import com.anodiam.StudentSignup.model.Permission;

import java.util.Optional;

public interface PermissionService {
    Optional<Permission> findByPermissionName(String permissionName);
}
