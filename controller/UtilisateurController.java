package com.securityspring.securityspring.controller;

import com.securityspring.securityspring.dto.AuthenticationDto;
import com.securityspring.securityspring.entity.UserService;
import com.securityspring.securityspring.service.JwtService;
import com.securityspring.securityspring.service.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class UtilisateurController {
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private UtilisateurService utilisateurService;

    @PostMapping(path = "inscription")
    public void inscription(@RequestBody UserService utilisateur){
        log.info("Inscription");
        this.utilisateurService.inscription(utilisateur);
    }
    @PostMapping(path = "activation")
    public void activation(@RequestBody Map<String, String> activationCode){
        this.utilisateurService.activation(activationCode);
    }
    @PostMapping(path = "connexion")
    public Map<String, String> connexion(@RequestBody AuthenticationDto authenticationDto){
        final Authentication authenticate= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationDto.username(), authenticationDto.password())
        );
        if(authenticate.isAuthenticated()){
            return this.jwtService.generate(authenticationDto.username());
        }
        log.info("resultat : {}", authenticate.isAuthenticated());
        return null;
    }
}
