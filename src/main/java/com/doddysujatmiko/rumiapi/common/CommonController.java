package com.doddysujatmiko.rumiapi.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.BiFunction;

@RestController
@RequestMapping("/")
public class CommonController {
    @Value("${RUMI_API_HOST}")
    private String rumiApiHost;

    @GetMapping("/")
    String root() {
        BiFunction<String, String, String> createAnchor = (href, text) -> "<a href=\""+href+"\"> "+text+" </a>";
        return "<!DOCTYPE html> Welcome to RumiApi. Click " +
                createAnchor.apply(rumiApiHost+"/swagger-ui/index.html", "this link") +
                " to open documentation. Or " +
                createAnchor.apply("http://localhost:8080/swagger-ui/index.html", "this link") +
                "if you run this api in localhost.";
    }

    @GetMapping("/hello")
    String getHello() {
        return "Hello";
    }

    @GetMapping("/error")
    String getError() {
        throw new Error("You ask for this!!");
    }
}
