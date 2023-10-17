package com.Applemango_Backend.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GlobalResDto {

    private String msg;
    private int statusCode;

    public GlobalResDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}