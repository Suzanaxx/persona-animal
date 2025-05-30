package com.animal.persona.repository;

import com.animal.persona.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Integer> {
    // Lahko dodaš dodatne metode po potrebi, npr. findByName itd.
}