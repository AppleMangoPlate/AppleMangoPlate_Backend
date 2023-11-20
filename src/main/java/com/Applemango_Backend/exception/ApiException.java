package com.Applemango_Backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiException extends RuntimeException {
    private ApiResponseStatus status; //BaseResponseStatus 객체에 매핑
}
