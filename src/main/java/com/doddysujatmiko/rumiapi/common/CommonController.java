package com.doddysujatmiko.rumiapi.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
public class CommonController {
    @GetMapping("/hello")
    String getHello() {
        return "Hello";
    }

    @GetMapping("/error")
    String getError() {
        throw new Error("You ask for this!!");
    }
}
