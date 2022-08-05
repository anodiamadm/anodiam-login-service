package com.anodiam.login.payload.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
