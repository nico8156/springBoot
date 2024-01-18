package com.securityspring.securityspring.controller;

import com.securityspring.securityspring.entity.Avis;
import com.securityspring.securityspring.entity.UserService;
import com.securityspring.securityspring.service.AvisService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RequestMapping("avis")
@RestController
public class AvisController {

    private final AvisService avisService;
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public void creer(@RequestBody Avis avis){
        UserService utilisateur = (UserService) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        avis.setUtilisateur(utilisateur);
        this.avisService.creer(avis);
    }
}
