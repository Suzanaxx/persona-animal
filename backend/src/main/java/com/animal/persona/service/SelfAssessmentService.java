package com.animal.persona.service;

import com.animal.persona.model.Animal;
import com.animal.persona.repository.AnimalRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SelfAssessmentService {

    private final AnimalRepository animalRepository;
    private final Set<Integer> usedAnimalIds = new HashSet<>();

    public SelfAssessmentService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Optional<List<Animal>> getNextPair() {
        List<Animal> allAnimals = animalRepository.findAll();
        List<Animal> available = allAnimals.stream()
                .filter(a -> !usedAnimalIds.contains(a.getId()))
                .toList();

        if (available.size() < 2) return Optional.empty();

        Collections.shuffle(available);
        Animal a1 = available.get(0);
        Animal a2 = available.get(1);

        usedAnimalIds.add(a1.getId());
        usedAnimalIds.add(a2.getId());

        return Optional.of(List.of(a1, a2));
    }

    public void resetSession() {
        usedAnimalIds.clear();
    }
}
