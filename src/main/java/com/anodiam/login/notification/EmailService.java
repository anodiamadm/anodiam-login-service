package com.anodiam.login.notification;

public interface EmailService {
    // To send a simple email
    void sendSimpleMail(EmailDetails details);

    // To send an email with attachment
    void sendMailWithAttachment(EmailDetails details);
}
