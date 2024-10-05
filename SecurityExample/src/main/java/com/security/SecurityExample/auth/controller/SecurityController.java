package com.security.SecurityExample.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @GetMapping("/")
    public String test(HttpServletRequest request){
        return "Welcome to the Spring Security "+request.getSession().getId();
    }
}
