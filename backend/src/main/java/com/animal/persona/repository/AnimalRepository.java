package com.animal.persona.repository;

import com.animal.persona.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Integer> {
    List<Animal> findByCategory_NameIgnoreCase(String name);
    // NOVO: najdi po ID kategorije
    List<Animal> findByCategory_Id(Integer categoryId);
}
