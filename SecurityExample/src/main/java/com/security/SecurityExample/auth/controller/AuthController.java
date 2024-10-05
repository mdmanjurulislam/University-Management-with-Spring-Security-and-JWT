package com.security.SecurityExample.auth.controller;

import com.security.SecurityExample.auth.service.MyUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private MyUserDetailsService.AuthService authService;

    @GetMapping("/get-csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        System.out.println("function called"+authService.getCsrfToken(request));
        return authService.getCsrfToken(request);
    }

}
