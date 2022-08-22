package com.anodiam.login.payload.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MessageResponse<T> {
  private MessageCode messageCode;
  private String message;
  private T data;

  public boolean isOk() {
    return messageCode.equals(MessageCode.SUCCESS);
  }
}
