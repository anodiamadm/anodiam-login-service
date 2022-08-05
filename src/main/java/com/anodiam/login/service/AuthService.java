package com.anodiam.login.service;

import com.anodiam.login.exception.EmailInUseException;
import com.anodiam.login.exception.InvalidTokenException;
import com.anodiam.login.exception.RoleNotFoundException;
import com.anodiam.login.exception.UserNotFoundException;
import com.anodiam.login.models.AnodiamRole;
import com.anodiam.login.models.Role;
import com.anodiam.login.models.SignupUser;
import com.anodiam.login.models.User;
import com.anodiam.login.payload.request.LoginRequest;
import com.anodiam.core.NotificationRequest;
import com.anodiam.login.payload.request.SignupRequest;
import com.anodiam.login.payload.response.JwtResponse;
import com.anodiam.login.payload.response.MessageResponse;
import com.anodiam.login.repository.RoleRepository;
import com.anodiam.login.repository.SignupUserRepository;
import com.anodiam.login.repository.UserRepository;
import com.anodiam.login.security.jwt.JwtUtils;
import com.anodiam.login.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final SignupUserRepository signupUserRepository;

    private final RoleRepository roleRepository;

    private final KafkaTemplate<String, NotificationRequest> kafkaTemplate;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    @Value("${anodiam.login.notification.topic.name}")
    private String loginNotificationTopicName;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, SignupUserRepository signupUserRepository, RoleRepository roleRepository, KafkaTemplate<String, NotificationRequest> kafkaTemplate, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.signupUserRepository = signupUserRepository;
        this.roleRepository = roleRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    public MessageResponse registerUser(final SignupRequest signUpRequest, final AnodiamRole role) throws EmailInUseException {
        if (userRepository.existsByEmail(signUpRequest.getEmail()) || signupUserRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EmailInUseException("Error: Email is already in use!");
        }

        String jwt = jwtUtils.generateJwtToken(signUpRequest.getEmail());

        // Create new user's account
        SignupUser user = SignupUser.builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .refererEmail(signUpRequest.getRefererEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .role(role)
                .validationToken(jwt)
                .build();

        signupUserRepository.save(user);

        kafkaTemplate.send(loginNotificationTopicName,
                NotificationRequest.builder()
                        .recipientEmail(signUpRequest.getEmail())
                        .recipientFirstName(signUpRequest.getFirstName())
                        .recipientLastName(signUpRequest.getLastName())
                        .subject("Anodiam Registration Confirmation")
                        .validationToken(new String(Base64.getEncoder().withoutPadding().encode(jwt.getBytes(StandardCharsets.UTF_8))))
                        .notificationType(NotificationRequest.NotificationType.STUDENT_SIGNUP)
                        .build());
        return new MessageResponse("User registered successfully!");
    }

    public MessageResponse validateUser(final String token) {
        String jwt = new String(Base64.getDecoder().decode(token.getBytes(StandardCharsets.UTF_8)));
        if(!jwtUtils.validateJwtToken(jwt)) {
            throw new InvalidTokenException("Error: Invalid Token");
        }
        String email = jwtUtils.getUserNameFromJwtToken(jwt);
        SignupUser signupUser = signupUserRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Error: Registration is not found."));
        if(!jwt.equals(signupUser.getValidationToken())) {
            throw new InvalidTokenException("Error: Invalid Token");
        }
        User user = User.builder()
                .firstName(signupUser.getFirstName())
                .lastName(signupUser.getLastName())
                .email(signupUser.getEmail())
                .refererEmail(signupUser.getRefererEmail())
                .password(signupUser.getPassword())
                .build();

        Set<Role> roles = new HashSet<>();

        switch (signupUser.getRole().name().toLowerCase()) {
            case "admin":
                Role adminRole = roleRepository.findByName(AnodiamRole.ADMIN)
                        .orElseThrow(() -> new RoleNotFoundException("Error: Role is not found."));
                roles.add(adminRole);

                break;
            case "teacher":
                Role modRole = roleRepository.findByName(AnodiamRole.TEACHER)
                        .orElseThrow(() -> new RoleNotFoundException("Error: Role is not found."));
                roles.add(modRole);

                break;
            default:
                Role userRole = roleRepository.findByName(AnodiamRole.STUDENT)
                        .orElseThrow(() -> new RoleNotFoundException("Error: Role is not found."));
                roles.add(userRole);
        }

        user.setRoles(roles);
        userRepository.save(user);
        signupUserRepository.deleteById(signupUser.getId());
        return new MessageResponse("User validated successfully!");
    }

    public JwtResponse authenticateUser(final LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return JwtResponse.builder()
                .type(JwtResponse.TOKEN_TYPE)
                .token(jwt)
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(roles)
                .build();
    }
}
