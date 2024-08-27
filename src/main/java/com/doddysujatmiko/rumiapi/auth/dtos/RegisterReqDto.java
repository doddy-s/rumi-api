package com.doddysujatmiko.rumiapi.auth.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterReqDto extends UserDto {
    @NotEmpty(message = "shall not empty")
    private String password;
}
