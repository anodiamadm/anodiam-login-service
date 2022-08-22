package com.anodiam.login.controllers;

import com.anodiam.login.models.AnodiamRole;
import com.anodiam.login.payload.request.LoginRequest;
import com.anodiam.login.payload.request.SignupRequest;
import com.anodiam.login.payload.response.ResponseCode;
import com.anodiam.login.payload.response.ApiResponse;
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
        return buildResponseEntity(authService.authenticateUser(loginRequest));
    }

    @PutMapping("/signup/{role}")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, @Valid @NotNull @PathVariable("role") AnodiamRole role) {
        return buildResponseEntity(authService.registerUser(signUpRequest, role));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateUser(@RequestParam("_t") final String token) {
        return buildResponseEntity(authService.validateUser(token));
    }

    private ResponseEntity<?> buildResponseEntity(final ApiResponse<?> apiResponse) {
        if(apiResponse.getResponseCode().equals(ResponseCode.SUCCESS)) {
            return ResponseEntity.ok(apiResponse);
        } else if(apiResponse.getResponseCode().equals(ResponseCode.BAD_REQUEST)) {
            return ResponseEntity
                    .badRequest()
                    .body(apiResponse);
        } else if(apiResponse.getResponseCode().equals(ResponseCode.SERVER_ERROR)) {
            return ResponseEntity
                    .internalServerError()
                    .body(apiResponse);
        } else {
            return ResponseEntity
                    .unprocessableEntity()
                    .body(apiResponse);
        }
    }
}
