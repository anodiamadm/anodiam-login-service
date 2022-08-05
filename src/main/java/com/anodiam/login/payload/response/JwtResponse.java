package com.anodiam.login.payload.response;

import com.anodiam.core.JwtToken;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JwtResponse {
  public static final String TOKEN_TYPE = "Bearer";
  private JwtToken token;
  private String type = TOKEN_TYPE;
  private Long id;
  private String username;
  private List<String> roles;
}
