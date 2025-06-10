package com.animal.persona.service;

import com.animal.persona.dto.AnimalRequestDTO;
import com.animal.persona.dto.AnimalRequestDTO.TraitDTO;
import com.animal.persona.model.Animal;
import com.animal.persona.model.AnimalTrait;
import com.animal.persona.model.Category;
import com.animal.persona.model.Trait;
import com.animal.persona.repository.AnimalRepository;
import com.animal.persona.repository.AnimalTraitRepository;
import com.animal.persona.repository.CategoryRepository;
import com.animal.persona.repository.TraitRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final CategoryRepository categoryRepository;
    private final TraitRepository traitRepository;
    private final AnimalTraitRepository animalTraitRepository;

    public AnimalService(AnimalRepository animalRepository,
                         CategoryRepository categoryRepository,
                         TraitRepository traitRepository,
                         AnimalTraitRepository animalTraitRepository) {
        this.animalRepository = animalRepository;
        this.categoryRepository = categoryRepository;
        this.traitRepository = traitRepository;
        this.animalTraitRepository = animalTraitRepository;
    }

    @Transactional
    public Animal addAnimalWithTraits(AnimalRequestDTO dto) {
        // 1. Poišči ali ustvari kategorijo
        Category category = categoryRepository
                .findByNameIgnoreCase(dto.getCategoryName())
                .orElseGet(() -> {
                    Category newCat = new Category();
                    newCat.setName(dto.getCategoryName());
                    return categoryRepository.save(newCat);
                });

        // 2. Shrani žival
        Animal animal = new Animal(dto.getName(), dto.getImageUrl(), category);
        animal = animalRepository.save(animal);

        // 3. Obdelaj trait-e, če obstajajo
        if (dto.getTraits() != null) {
            for (TraitDTO traitDTO : dto.getTraits()) {
                // Najdi ali ustvari trait
                Trait trait = traitRepository
                        .findByDescriptionIgnoreCase(traitDTO.getDescription())
                        .orElseGet(() -> {
                            Trait t = new Trait();
                            t.setDescription(traitDTO.getDescription());
                            t.setPositive(traitDTO.isPositive());
                            return traitRepository.save(t);
                        });

                // Preveri, če povezava že obstaja
                boolean exists = animalTraitRepository.existsByAnimalIdAndTraitId(animal.getId(), trait.getId());
                if (!exists) {
                    // Poveži trait z živaljo
                    AnimalTrait animalTrait = new AnimalTrait(animal.getId(), trait.getId());
                    animalTraitRepository.save(animalTrait);
                }
            }
        }

        return animal;
    }
}
