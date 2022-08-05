package com.anodiam.login.payload.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SignupRequest {

  @NotBlank
  @Size(max = 30)
  private String firstName;

  @NotBlank
  @Size(max = 30)
  private String lastName;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 50)
  @Email
  private String refererEmail;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;
}
