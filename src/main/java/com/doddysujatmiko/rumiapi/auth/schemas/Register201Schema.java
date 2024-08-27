package com.doddysujatmiko.rumiapi.auth.schemas;

import com.doddysujatmiko.rumiapi.auth.dtos.UserDto;
import com.doddysujatmiko.rumiapi.common.BaseResDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Register201Schema extends BaseResDto {
    private UserDto data;
}
