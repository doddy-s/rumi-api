package com.doddysujatmiko.rumiapi.common.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Responser {
    public ResponseEntity<?> response(HttpStatus status, String message, Object data){
        Map<Object, Object> map = new HashMap<>();
        map.put("statusCode", status.value());
        map.put("message", message);
        map.put("data", data);
        return new ResponseEntity<>(map, status);
    }

    public ResponseEntity<?> response(HttpStatus status, String message){
        Map<Object, Object> map = new HashMap<>();
        map.put("statusCode", status.value());
        map.put("message", message);
        return new ResponseEntity<>(map, status);
    }
}
