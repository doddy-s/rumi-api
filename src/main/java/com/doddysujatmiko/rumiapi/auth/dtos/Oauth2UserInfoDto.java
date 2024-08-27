package com.doddysujatmiko.rumiapi.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Oauth2UserInfoDto {
    @NotEmpty(message = "shall not empty")
    private String username;

    @NotEmpty(message = "shall not empty")
    @Email
    private String email;

    private String picture;
}
