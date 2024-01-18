package com.securityspring.securityspring.service;

import com.securityspring.securityspring.TypeDeRole;
import com.securityspring.securityspring.entity.Role;
import com.securityspring.securityspring.entity.UserService;
import com.securityspring.securityspring.entity.Validation;
import com.securityspring.securityspring.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UtilisateurService implements UserDetailsService {

    private UtilisateurRepository utilisateurRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private ValidationService validationService;

    public void inscription(UserService utilisateur){

        //quelques vérifications sur le mail :
        if(!utilisateur.getUsername().contains("@")){
            throw new RuntimeException("Votre email n'est pas valide.");
        }
        if(!utilisateur.getUsername().contains(".")){
            throw new RuntimeException("Votre email n'est pas valide.");
        }

        //Vérifier que l'utilisateur n'est pas déjà dans la base de données :
        Optional<UserService> utilisateurOptional = this.utilisateurRepository.findByEmail(utilisateur.getUsername());
        if(utilisateurOptional.isPresent()){
            throw new RuntimeException("Votre email est déjà utilisé.");
        }

        // Encodage du mot de passe :
        String hashedPassword = this.passwordEncoder.encode(utilisateur.getPassword());
        utilisateur.setMdp(hashedPassword);

        //Définition d'un role pour l'utilisateur :
        Role roleUtilisateur = new Role();
        roleUtilisateur.setLibelle(TypeDeRole.UTILISATEUR);
        utilisateur.setRole(roleUtilisateur);

        //Sauvegarder l'utilisateur et assigner l'utilisateur pour la validation :
        utilisateur = this.utilisateurRepository.save(utilisateur);
        this.validationService.enregistrer(utilisateur);
    }

    public void activation(Map<String, String> activationCode) {
        Validation validation = this.validationService.lireEnFontionDuCode(activationCode.get("code"));
        if(Instant.now().isAfter(validation.getDateExpiration())){
            throw new RuntimeException("Votre code a expiré.");
        }
        UserService utilisateurActiver = this.utilisateurRepository.findById(validation.getUtilisateur().getId()).orElseThrow(() -> new RuntimeException("Utilisateur inconnu"));
        utilisateurActiver.setActif(true);

        utilisateurRepository.save(utilisateurActiver);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return  this.utilisateurRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur ne correspond à cet email."));

    }
}
