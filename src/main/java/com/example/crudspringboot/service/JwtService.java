package com.example.crudspringboot.service;

import com.example.crudspringboot.entity.BlacklistEntity;
import com.example.crudspringboot.entity.WhitelistEntity;
import com.example.crudspringboot.repository.BlacklistRepository;
import com.example.crudspringboot.repository.WhitelistRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final String SECRET_KEY="cDJjRClp0QutqNa5TxgCVYUr71EeAu8FSJsBYX5E5gPHkf9tCqroBG3EJezB94H6";
    private final long ACCESS_TOKEN= 60*1000;
    private final long REFRESH_TOKEN= 60*24*7*1000;
    private final WhitelistRepository whitelistRepository;
    private final BlacklistRepository blacklistRepository;
    private final UserDetailsService userDetailsService;
    private Key getSignInKey(){
        byte[] keys= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keys);
    }
    private Claims extractClaimsAll(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims= extractClaimsAll(token);
        return claimsResolver.apply(claims);
    }
    public String extractUserName(String token){
        return extractClaim(token,Claims::getSubject);
    }
    public Claims decodedToken(String token){
        return extractClaimsAll(token);
    }
    private String generatorToken(Map<String, List<String>> extractClaims, UserDetails userDetails, long expiration){
        Collection<? extends GrantedAuthority> userRoles= userDetails.getAuthorities();
        List<String> roles = userRoles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        roles.forEach(role-> System.out.println(role));
        extractClaims.put("roles",  roles);
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generatorAccessToken(UserDetails userDetails){
        return generatorToken(new HashMap<>(), userDetails, ACCESS_TOKEN);
    }
    public String generatorRefreshToken(UserDetails userDetails){
        return generatorToken(new HashMap<>(), userDetails, REFRESH_TOKEN);
    }
    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
    public boolean isTokenExpiration(String token){
        return extractExpiration(token).before(new Date());
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName= extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpiration(token);
    }
    public boolean isTokenInBlackList(String token){
        Optional<BlacklistEntity> isToken= blacklistRepository.findByToken(token).isPresent()?blacklistRepository.findByToken(token):null;
        if(isToken==null){
            return false;
        }
        return true;
    }
    public String getRefreshToken(String token, UserDetails userDetails){
        List<WhitelistEntity> whitelist= whitelistRepository.findAll();
        final String[] newToken = new String[1];
        whitelist.forEach(item->{
            final String userName= extractUserName(item.getToken());
            if(userName.equals(userDetails.getUsername())){
                UserDetails user= this.userDetailsService.loadUserByUsername(userName);
                newToken[0] = this.generatorAccessToken(user);
            }
        });
        var tokenBlacklist= BlacklistEntity.builder()
                .token(token)
                .build();
        blacklistRepository.save(tokenBlacklist);
        return newToken[0];
    }
}
