package com.animal.persona.service;

import com.animal.persona.model.Trait;
import com.animal.persona.repository.TraitRepository;
import org.springframework.stereotype.Service;

@Service
public class TraitService {

    private final TraitRepository traitRepository;

    public TraitService(TraitRepository traitRepository) {
        this.traitRepository = traitRepository;
    }

    public Trait findOrCreateTrait(String description, boolean isPositive) {
        return traitRepository.findByDescriptionIgnoreCase(description)
                .orElseGet(() -> {
                    Trait newTrait = new Trait(description, isPositive);
                    return traitRepository.save(newTrait);
                });
    }
}
