package com.anodiam.login.payload.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ApiResponse<T> {
  private ResponseCode responseCode;
  private String message;
  private T data;

  public boolean isOk() {
    return responseCode.equals(ResponseCode.SUCCESS);
  }
}
