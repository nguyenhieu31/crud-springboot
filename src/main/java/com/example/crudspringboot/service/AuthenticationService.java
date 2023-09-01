package com.example.crudspringboot.service;

import com.example.crudspringboot.entity.RoleEntity;
import com.example.crudspringboot.entity.UserEntity;
import com.example.crudspringboot.entity.WhitelistEntity;
import com.example.crudspringboot.repository.RoleRepository;
import com.example.crudspringboot.repository.UserRepository;
import com.example.crudspringboot.repository.WhitelistRepository;
import com.example.crudspringboot.request.AuthenticationRequest;
import com.example.crudspringboot.request.RegisterRequest;
import com.example.crudspringboot.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;
    private final WhitelistRepository whitelistRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public String register(RegisterRequest request){
        List<String> nameRole= new ArrayList<>();
        request.getRoles().forEach(role->nameRole.add(role.getName()));
        Set<RoleEntity> roles= new HashSet<>();
        nameRole.forEach(name->{
            Optional<RoleEntity> role= roleRepository.findByName(name);
            role.ifPresent(roles::add);
        });
        var user= UserEntity.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();
        userRepository.save(user);
        return "register is success";
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        System.out.println(authentication);
        var user= userRepository.findUserEntitiesByEmail(request.getEmail())
                .orElseThrow();
        var token= jwtService.generatorAccessToken(user);
        var refreshToken= jwtService.generatorRefreshToken(user);
        var saveTokenInWhitelist= WhitelistEntity.builder()
                .token(refreshToken)
                .build();
        whitelistRepository.save(saveTokenInWhitelist);
        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }
}
