package com.anodiam.login.security.jwt;

import com.anodiam.core.JwtToken;
import com.anodiam.login.payload.response.MessageCode;
import com.anodiam.login.payload.response.MessageResponse;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${jwt.app.secret}")
  private String jwtSecret;

  @Value("${jwt.app.expiration.ms}")
  private long jwtExpirationMs;

  public JwtToken generateJwtToken(String subject) {
    return generateJwtToken(subject, jwtExpirationMs);
  }

  public JwtToken generateJwtToken(String subject, long jwtExpirationMs) {
    final Date now = new Date();
    final Date expiresOn = new Date(now.getTime() + jwtExpirationMs);
    return JwtToken.builder()
            .token(Jwts.builder()
                    .setSubject(subject)
                    .setIssuedAt(now)
                    .setExpiration(expiresOn)
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact())
            .expiresOn(expiresOn)
            .build();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public MessageResponse validateJwtToken(String authToken) {
    MessageCode messageCode;
    String message = "OK";
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      messageCode = MessageCode.SUCCESS;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
      messageCode = MessageCode.BAD_REQUEST;
      message = "Invalid JWT signature: " +  e.getMessage();
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
      messageCode = MessageCode.BAD_REQUEST;
      message = "Invalid JWT token: " +  e.getMessage();
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
      messageCode = MessageCode.BAD_REQUEST;
      message = "JWT token is expired: " +  e.getMessage();
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
      messageCode = MessageCode.BAD_REQUEST;
      message = "JWT token is unsupported: " +  e.getMessage();
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
      messageCode = MessageCode.BAD_REQUEST;
      message = "JWT claims string is empty: " +  e.getMessage();
    }

    return new MessageResponse(messageCode, message, null);
  }
}
