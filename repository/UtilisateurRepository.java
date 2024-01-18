package com.securityspring.securityspring.repository;

import com.securityspring.securityspring.entity.UserService;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UtilisateurRepository extends CrudRepository<UserService, Integer> {
    Optional<UserService> findByEmail(String email);
}
