package com.anodiam.core;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationRequest {
    private String recipientEmail;
    private String recipientFirstName;
    private String recipientLastName;
    private String validationToken;
    private String subject;
    private String attachment;
    private NotificationType notificationType;

    public static enum NotificationType {
        STUDENT_SIGNUP,
        TEACHER_SIGNUP,
        ADMIN_SIGNUP;
    }
}
