package com.anodiam.StudentSignup.serviceRepository.Permission;

import com.anodiam.StudentSignup.model.Permission;
import com.anodiam.StudentSignup.model.common.MessageResponse;
import com.anodiam.StudentSignup.model.common.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class PermissionServiceDal extends PermissionServiceImpl {

    @Autowired
    private PermissionRepository permissionRepository;

    public PermissionServiceDal(){}

    @Override
    public Optional<Permission> findByPermissionName(String permissionName) {
        Permission permissionReturned = new Permission();
        try {
            Optional<Permission> optionalPermission =
                    permissionRepository.findByPermissionName(permissionName);
            if(optionalPermission.isPresent()){
                permissionReturned = optionalPermission.get();
                permissionReturned.setMessageResponse(new
                        MessageResponse(ResponseCode.PERMISSION_NAME_EXISTS.getID(),
                        ResponseCode.PERMISSION_NAME_EXISTS.getMessage()
                                + permissionReturned.getPermissionName()));
            } else {
                permissionReturned.setMessageResponse(new
                        MessageResponse(ResponseCode.PERMISSION_NAME_INVALID.getID(),
                        ResponseCode.PERMISSION_NAME_INVALID.getMessage() + permissionName));
            }
        } catch(Exception exception) {
            exception.printStackTrace();
            permissionReturned.setMessageResponse(new MessageResponse(ResponseCode.FAILURE.getID(),
                    ResponseCode.FAILURE.getMessage() + exception.getMessage()));
        }
        return Optional.of(permissionReturned);
    }
}
