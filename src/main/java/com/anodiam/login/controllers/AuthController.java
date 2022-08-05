package com.anodiam.login.controllers;

import com.anodiam.login.exception.EmailInUseException;
import com.anodiam.login.models.AnodiamRole;
import com.anodiam.login.payload.request.LoginRequest;
import com.anodiam.login.payload.request.SignupRequest;
import com.anodiam.login.payload.response.MessageResponse;
import com.anodiam.login.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @PutMapping("/signup/{role}")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, @Valid @NotNull @PathVariable("role") AnodiamRole role) {
        try{
            return ResponseEntity.ok(authService.registerUser(signUpRequest, role));
        } catch(EmailInUseException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(ex.getMessage()));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateUser(@RequestParam("_t") final String token) {
        return ResponseEntity.ok(authService.validateUser(token));
    }
}
