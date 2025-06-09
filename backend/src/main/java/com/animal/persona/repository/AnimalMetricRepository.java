package com.animal.persona.repository;

import com.animal.persona.model.AnimalMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalMetricRepository extends JpaRepository<AnimalMetric, Long> {
    List<AnimalMetric> findByAnimalId(Integer animalId);
}