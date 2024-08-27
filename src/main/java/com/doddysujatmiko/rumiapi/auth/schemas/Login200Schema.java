package com.doddysujatmiko.rumiapi.auth.schemas;

import com.doddysujatmiko.rumiapi.auth.dtos.TokenDto;
import com.doddysujatmiko.rumiapi.common.BaseResDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Login200Schema extends BaseResDto {
    private TokenDto data;
}
