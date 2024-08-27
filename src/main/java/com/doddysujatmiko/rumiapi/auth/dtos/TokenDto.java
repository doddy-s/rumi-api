package com.doddysujatmiko.rumiapi.auth.dtos;

import lombok.Data;

@Data
public class TokenDto {
    private Long userId;
    private String accessToken;
    private String refreshToken;
}
