package com.anodiam.core;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JwtToken {
    private String token;
    private Date expiresOn;
}
