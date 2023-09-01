package com.example.crudspringboot.controller.web;

import com.example.crudspringboot.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/hello")
public class testController {
    @GetMapping("")
    public ResponseEntity<AuthenticationResponse> hello(HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Authorization","Bearer "+request.getAttribute("token"));
        response.setHeader("RefreshToken","Bearer "+request.getAttribute("refreshToken"));
        return ResponseEntity.ok(AuthenticationResponse.builder()
                        .token((String) request.getAttribute("token"))
                        .refreshToken((String) request.getAttribute("refreshToken"))
                        .build());
    }
}
