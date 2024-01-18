package com.securityspring.securityspring.service;

import com.securityspring.securityspring.entity.UserService;
import com.securityspring.securityspring.entity.Validation;
import com.securityspring.securityspring.repository.ValidationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@AllArgsConstructor
@Service
public class ValidationService {

    private ValidationRepository validationRepository;
    private NotificationService notificationService;

    public void enregistrer(UserService utilisateur){

        Validation validation = new Validation();

        // Parametrage de la validation:
        validation.setUtilisateur(utilisateur);
        Instant dateCreation = Instant.now();
        Instant dateExpiration = dateCreation.plus(10, ChronoUnit.MINUTES);
        Random random = new Random();
        int randomInt = random.nextInt(999999);
        String code = String.format("%06d", randomInt);
        validation.setDateCreation(dateCreation);
        validation.setDateExpiration(dateExpiration);
        validation.setCode(code);

        // Sauvegarde de la validation et envoie de la notification :
        this.validationRepository.save(validation);
        this.notificationService.envoyer(validation);
    }
    public Validation lireEnFontionDuCode(String code){
        return this.validationRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Votre code est invalide."));
    }
}
