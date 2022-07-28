package com.anodiam.login.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "signup_users",
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "email")
    })
public class SignupUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  @NotNull
  @Enumerated(EnumType.STRING)
  private AnodiamRole role;

  @NotBlank
  @Size(max = 1000)
  private String validationToken;

  public SignupUser() {
  }

  public SignupUser(String email, String password, String validationToken, AnodiamRole role) {
    this.email = email;
    this.password = password;
    this.validationToken = validationToken;
    this.role = role;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public AnodiamRole getRole() {
    return role;
  }

  public void setRole(AnodiamRole role) {
    this.role = role;
  }

  public String getValidationToken() {
    return validationToken;
  }

  public void setValidationToken(String validationToken) {
    this.validationToken = validationToken;
  }
}
