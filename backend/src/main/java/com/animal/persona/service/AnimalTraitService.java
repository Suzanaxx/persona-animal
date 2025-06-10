package com.animal.persona.service;

import com.animal.persona.model.AnimalTrait;
import com.animal.persona.model.AnimalTrait.AnimalTraitId;
import com.animal.persona.repository.AnimalTraitRepository;
import org.springframework.stereotype.Service;

@Service
public class AnimalTraitService {

    private final AnimalTraitRepository animalTraitRepository;

    public AnimalTraitService(AnimalTraitRepository animalTraitRepository) {
        this.animalTraitRepository = animalTraitRepository;
    }

    public boolean addTraitToAnimal(Integer animalId, Integer traitId) {
        if (animalTraitRepository.existsByAnimalIdAndTraitId(animalId, traitId)) {
            return false;
        }
        AnimalTrait at = new AnimalTrait(animalId, traitId);
        animalTraitRepository.save(at);
        return true;
    }
}
