package com.anodiam.login.notification;

import lombok.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MessageContentBuilder {
    private String baseUrl;
    private String validationToken;

    public String getMessageContent() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<p>");
        stringBuilder.append("<div>Congratulations!!! Your Registration was successful.</div>");
        stringBuilder.append("<div>Please confirm your registration by visiting <a href=\"");
        stringBuilder.append(baseUrl);
        stringBuilder.append("/api/auth/validate?_t=");
        stringBuilder.append(new String(Base64.getEncoder().withoutPadding().encode(validationToken.getBytes(StandardCharsets.UTF_8))));
        stringBuilder.append("\">this</a> link.</div>");
        stringBuilder.append("<p>&nbsp;</p>");
        stringBuilder.append("<div>Warm Regards,</div>");
        stringBuilder.append("<div>Anodiam Admin</div>");
        stringBuilder.append("</p>");
        return stringBuilder.toString();
    }
}
