package com.example.crudspringboot.controller.auth;

import com.example.crudspringboot.request.AuthenticationRequest;
import com.example.crudspringboot.request.RegisterRequest;
import com.example.crudspringboot.response.AuthenticationResponse;
import com.example.crudspringboot.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {
    @Autowired
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("/login")
    public ModelAndView authenticate(AuthenticationRequest request, HttpServletResponse response){
        AuthenticationResponse authRes= authenticationService.authenticate(request);
        if(authRes.getToken()!=null){
            Cookie cookie= new Cookie("accessToken", authRes.getToken());
            cookie.setMaxAge(3600);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
           return new ModelAndView("web/home");
        }
        return new ModelAndView("login");
    }
    @GetMapping("/login")
    public ModelAndView loginPage(Model model){
        model.addAttribute("formLogin", new AuthenticationRequest());
        return new ModelAndView("login");
    }
}
