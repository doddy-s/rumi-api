package com.doddysujatmiko.rumiapi.common;

import lombok.Data;

@Data
public abstract class BaseResDto {
    private int statusCode;
    private String message;
}
