package com.security.SecurityExample.auth.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonResponse<T> {
    private T message;
    private T code;
    private T response;
}
