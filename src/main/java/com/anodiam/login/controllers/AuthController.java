package com.anodiam.login.controllers;

import com.anodiam.login.exception.InvalidTokenException;
import com.anodiam.login.exception.RoleNotFoundException;
import com.anodiam.login.exception.UserNotFoundException;
import com.anodiam.login.models.*;
import com.anodiam.login.notification.EmailDetails;
import com.anodiam.login.notification.EmailService;
import com.anodiam.login.notification.MessageContentBuilder;
import com.anodiam.login.payload.request.LoginRequest;
import com.anodiam.login.payload.request.SignupRequest;
import com.anodiam.login.payload.response.JwtResponse;
import com.anodiam.login.payload.response.MessageResponse;
import com.anodiam.login.repository.RoleRepository;
import com.anodiam.login.repository.SignupUserRepository;
import com.anodiam.login.repository.UserRepository;
import com.anodiam.login.security.jwt.JwtUtils;
import com.anodiam.login.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SignupUserRepository signupUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${anodiam.mail.base-url}")
    private String baseUrl;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @PostMapping("/signup/{role}")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, @Valid @NotNull @PathVariable("role") AnodiamRole role) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }



        String jwt = jwtUtils.generateJwtToken(signUpRequest.getEmail());

        // Create new user's account
        SignupUser user = new SignupUser(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                jwt,
                role);

        signupUserRepository.save(user);

        emailService.sendSimpleMail(
                EmailDetails.builder()
                        .recipient(signUpRequest.getEmail())
                        .subject("Anodiam Registration Confirmation")
                        .msgBody(MessageContentBuilder.builder().baseUrl(baseUrl).validationToken(jwt).build().getMessageContent())
                        .build());
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateUser(@RequestParam("_t") final String token) {
        String jwt = new String(Base64.getDecoder().decode(token.getBytes(StandardCharsets.UTF_8)));
        if(!jwtUtils.validateJwtToken(jwt)) {
            throw new InvalidTokenException("Error: Invalid Token");
        }
        String email = jwtUtils.getUserNameFromJwtToken(jwt);
        SignupUser signupUser = signupUserRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Error: Registration is not found."));
        if(!jwt.equals(signupUser.getValidationToken())) {
            throw new InvalidTokenException("Error: Invalid Token");
        }
        User user = new User(signupUser.getEmail(), signupUser.getPassword());

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
        return ResponseEntity.ok(new MessageResponse("User validated successfully!"));
    }
}
