package com.animal.persona.repository;

import com.animal.persona.model.Trait;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraitRepository extends JpaRepository<Trait, Integer> {
    Optional<Trait> findByDescriptionIgnoreCase(String description);
}