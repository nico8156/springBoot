package com.securityspring.securityspring.service;

import com.securityspring.securityspring.entity.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
@Service
public class JwtService {
    private final String ENCRIPTION_KEY = "azCdnMnFUg4GwbowwN9fi6rqG2JCQGPjbCAa4GV64SfD3MU3Evh8zSfj4aUlqVkS";
    private UtilisateurService utilisateurService;

    public Map<String, String> generate(String username){
        UserService utilisateur = (UserService) this.utilisateurService.loadUserByUsername(username);
        return this.generateJwt(utilisateur);
    }

    public String getUsernameFromToken(String token){
        return this.getClaim(token, Claims::getSubject);
    }
    public Boolean isTokenExpired(String token){
        Date expirationDate = this.getClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }
    private <T> T getClaim(String token, Function<Claims, T> function){
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }
    private Claims getAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Map<String ,String> generateJwt(UserService utilisateur){
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 30 * 60 * 1000;
        final Map<String, Object> claims =
                Map.of(
                        "nom", utilisateur.getNom(),
                        Claims.EXPIRATION, new Date(expirationTime),
                        Claims.SUBJECT, utilisateur.getEmail()
                );
        final String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(utilisateur.getEmail())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of("bearer", bearer);
    }
    private Key getKey(){
        final byte[] decodeur = Decoders.BASE64.decode(ENCRIPTION_KEY);
        return Keys.hmacShaKeyFor(decodeur);
    }

}
