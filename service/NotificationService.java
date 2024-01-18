package com.securityspring.securityspring.service;

import com.securityspring.securityspring.entity.Validation;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {
    JavaMailSender javaMailSender;
    public void envoyer(Validation validation){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("bibi-verif@verif.com");
        mailMessage.setTo(validation.getUtilisateur().getUsername());
        mailMessage.setSubject("Votre code d'activation");
        String message = String.format("Bonjour %s, votre code d'activation est le suivant : %s .Nous vous remercions et vous aouhaitons une bonne journée. A bientôt.",
                validation.getUtilisateur().getNom(),
                validation.getCode()
        );
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);

    }
}
