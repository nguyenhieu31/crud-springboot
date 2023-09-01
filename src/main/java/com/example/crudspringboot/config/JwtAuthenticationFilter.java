package com.example.crudspringboot.config;
import com.example.crudspringboot.response.AuthenticationResponse;
import com.example.crudspringboot.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeaderToken= request.getHeader("Authorization");
        final String authHeaderRefreshToken= request.getHeader("RefreshToken");
        final String token;
        final String refreshToken;
        final String userEmail;
        if(authHeaderToken==null || !authHeaderToken.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return ;
        }
        token= authHeaderToken.substring(7);
        refreshToken= authHeaderRefreshToken.substring(7);
        if(!jwtService.isTokenInBlackList(token)){
            try{
                boolean checkExpiredToken= jwtService.isTokenExpiration(token);
                if(!checkExpiredToken){
                    userEmail= jwtService.extractUserName(token);
                    if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                        UserDetails userDetails= this.userDetailsService.loadUserByUsername(userEmail);
                        if(jwtService.isTokenValid(token,userDetails)){
                            UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    }
                }
            }catch (ExpiredJwtException ex){
                Claims claims= jwtService.decodedToken(refreshToken);
                UserDetails userDetails= this.userDetailsService.loadUserByUsername(claims.getSubject());
                String newToken= jwtService.getRefreshToken(token,userDetails);
                if(jwtService.isTokenValid(newToken,userDetails)){
                    request.setAttribute("token", newToken);
                    request.setAttribute("refreshToken", refreshToken);
                    UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }else {
            throw new JwtException("token isn't valid");
        }
        filterChain.doFilter(request,response);
    }
}