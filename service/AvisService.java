package com.securityspring.securityspring.service;

import com.securityspring.securityspring.entity.Avis;
import com.securityspring.securityspring.repository.AvisRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class AvisService {
    private final AvisRepository avisRepository;

    public void creer(Avis avis){
        this.avisRepository.save(avis);
    }
}
