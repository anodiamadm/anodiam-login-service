package com.anodiam.login.payload.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ValidationResult {
    private String objectName;
    private String fieldName;
    private String rejectedValue;
    private String validationMessage;
}
