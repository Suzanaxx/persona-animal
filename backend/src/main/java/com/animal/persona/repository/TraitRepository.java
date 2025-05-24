package com.animal.persona.repository;

import com.animal.persona.model.Trait;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface TraitRepository extends JpaRepository<Trait, Integer> {
    List<Trait> findByIdIn(Collection<Integer> ids);
}
