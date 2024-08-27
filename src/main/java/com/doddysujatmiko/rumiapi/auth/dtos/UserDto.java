package com.doddysujatmiko.rumiapi.auth.dtos;

import com.doddysujatmiko.rumiapi.auth.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotEmpty(message = "shall not empty")
    private String username;

    @NotEmpty(message = "shall not empty")
    @Email
    private String email;

    private String picture;

    public static UserDto fromEntity(UserEntity userEntity) {
        return UserDto
                .builder()
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .picture(userEntity.getPicture())
                .build();
    }

    public static UserEntity toEntity(UserDto userDto) {
        return UserEntity
                .builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .picture(userDto.getPicture())
                .build();
    }
}
