package com.doddysujatmiko.rumiapi.auth.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginReqDto {
    @NotEmpty(message = "shall not empty")
    private String username;

    @NotEmpty(message = "shall not empty")
    private String password;
}
