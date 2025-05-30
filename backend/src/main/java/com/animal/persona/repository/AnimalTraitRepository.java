package com.animal.persona.repository;

import com.animal.persona.dto.AnimalTraitDTO;
import com.animal.persona.model.AnimalTrait;
import com.animal.persona.model.AnimalTrait.AnimalTraitId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnimalTraitRepository extends JpaRepository<AnimalTrait, AnimalTraitId> {

    @Query("""
        select new com.animal.persona.dto.AnimalTraitDTO(
            t.id, t.description, t.isPositive)
        from AnimalTrait at
        join Trait t on at.traitId = t.id
        where at.animalId = :animalId
    """)
    List<AnimalTraitDTO> findTraitsByAnimalId(@Param("animalId") Integer animalId);
}