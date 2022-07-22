package com.anodiam.StudentSignup.controller;

import com.anodiam.StudentSignup.model.common.MessageResponse;
import com.anodiam.StudentSignup.model.common.ResponseCode;
import com.anodiam.StudentSignup.model.User;
import com.anodiam.StudentSignup.serviceRepository.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/public")
@CrossOrigin
public class PublicRestApiController {

    @Autowired
    private UserService userService;

//  Register New user with username & password
    @PostMapping(value = "student-signup")
    public ResponseEntity<?> studentSignup(@RequestBody User student) throws Exception {
        User studentToSignUp = new User();
        try
        {
            MessageResponse messageResponse = userService.save(student);
            return ResponseEntity.ok(new MessageResponse(studentToSignUp.getMessageResponse().getCode(),
                    studentToSignUp.getMessageResponse().getMessage()));
        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseEntity.ok(new MessageResponse(ResponseCode.FAILURE.getID(),
                    exception.getMessage()));
        }
    }
}
