package com.anodiam.login.payload.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String username;
  private List<String> roles;
}
