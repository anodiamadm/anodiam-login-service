package com.anodiam.StudentSignup.serviceRepository.User;

import com.anodiam.StudentSignup.model.Permission;
import com.anodiam.StudentSignup.model.Role;
import com.anodiam.StudentSignup.model.User;
import com.anodiam.StudentSignup.model.common.MessageResponse;
import com.anodiam.StudentSignup.model.common.ResponseCode;
import com.anodiam.StudentSignup.serviceRepository.Permission.PermissionRepository;
import com.anodiam.StudentSignup.serviceRepository.Role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
class UserServiceDal extends UserServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceDal(){}

    @Override
    public Optional<User> findByUsername(String username) {
        User userReturned = new User();
        try {
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if(optionalUser.isPresent()) {
                userReturned = optionalUser.get();
                userReturned.setMessageResponse(new
                        MessageResponse(ResponseCode.USER_ALREADY_EXISTS.getID(),
                        ResponseCode.USER_ALREADY_EXISTS.getMessage()
                                + userReturned.getUsername()));
            } else {
                userReturned.setMessageResponse(new
                        MessageResponse(ResponseCode.USER_NOT_REGISTERED.getID(),
                        ResponseCode.USER_NOT_REGISTERED.getMessage() + username));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            userReturned.setMessageResponse(new MessageResponse(ResponseCode.FAILURE.getID(),
                    ResponseCode.FAILURE.getMessage() + exception.getMessage()));
        }
        return Optional.of(userReturned);
    }

    @Override
    public MessageResponse save(User student)
    {
        MessageResponse messageResponse = new MessageResponse();
        try
        {
            if (!isValidEmail(student.getUsername())) {
                messageResponse.setCode(ResponseCode.EMAIL_FORMAT_INVALID.getID());
                messageResponse.setMessage(ResponseCode.EMAIL_FORMAT_INVALID.getMessage()
                        + student.getUsername());
            } else if (userRepository.findByUsername(student.getUsername()).isPresent()) {
                messageResponse.setCode(ResponseCode.USER_ALREADY_EXISTS.getID());
                messageResponse.setMessage(ResponseCode.USER_ALREADY_EXISTS.getMessage()
                        + student.getUsername());
            } else if(student.getPassword().length() < 6) {
                messageResponse.setCode(ResponseCode.PASSWORD_SHORT.getID());
                messageResponse.setMessage(ResponseCode.PASSWORD_SHORT.getMessage()
                        + student.getPassword());
            } else if (!isValidPassword(student.getPassword())) {
                messageResponse.setCode(ResponseCode.PASSWORD_INVALID.getID());
                messageResponse.setMessage(ResponseCode.PASSWORD_INVALID.getMessage()
                        + student.getPassword());
            } else {
                String encodedPassword = passwordEncoder.encode(student.getPassword());
                User studentToSave = new User(student.getUsername(), encodedPassword);
                Role role_user = roleRepository.findByRoleName("USER").get();
                Permission permission_user = permissionRepository.findByPermissionName("STUDENT_ACCESS").get();
                studentToSave.getRoleList().add(role_user);
                studentToSave.getPermissionList().add(permission_user);
                role_user.getUserList().add(studentToSave);
                permission_user.getUserList().add(studentToSave);
                userRepository.save(studentToSave);
                messageResponse.setCode(ResponseCode.SUCCESS.getID());
                messageResponse.setMessage(ResponseCode.SUCCESS.getMessage() + student.getUsername());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            messageResponse.setCode(ResponseCode.FAILURE.getID());
            messageResponse.setMessage(ResponseCode.FAILURE.getMessage() + student.getUsername());
        }
        return messageResponse;
    }

    private  static boolean isValidPassword(String password)
    {

        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{5,100}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if (password == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);

        // Return if the password
        // matched the ReGex
        return m.matches();
    }

    private static boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{1,9}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}
