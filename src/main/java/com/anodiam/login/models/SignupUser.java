package com.anodiam.login.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "signup_users",
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "email")
    })
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SignupUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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
  @Size(max = 120)
  private String password;

  @NotNull
  @Enumerated(EnumType.STRING)
  private AnodiamRole role;

  @NotBlank
  @Size(max = 1000)
  private String validationToken;

}
