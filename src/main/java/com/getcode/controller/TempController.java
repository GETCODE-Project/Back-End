package com.getcode.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {
    @GetMapping("/login/oauth2/code/api/oauth2/sign-up")
    public String oauthTest() {
        return "aa";
    }
}
