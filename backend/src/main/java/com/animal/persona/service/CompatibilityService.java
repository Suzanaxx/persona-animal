package com.animal.persona.service;

import com.animal.persona.dto.CompatibilityResultDTO;
import com.animal.persona.model.Animal;
import com.animal.persona.model.AnimalTrait;
import com.animal.persona.model.MatchCategory;
import com.animal.persona.repository.AnimalRepository;
import com.animal.persona.repository.AnimalTraitRepository;
import com.animal.persona.repository.MatchCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompatibilityService {

    private final AnimalRepository animalRepository;
    private final AnimalTraitRepository animalTraitRepository;
    private final MatchCategoryRepository matchCategoryRepository;

    public CompatibilityService(AnimalRepository animalRepository,
                                AnimalTraitRepository animalTraitRepository,
                                MatchCategoryRepository matchCategoryRepository) {
        this.animalRepository = animalRepository;
        this.animalTraitRepository = animalTraitRepository;
        this.matchCategoryRepository = matchCategoryRepository;
    }

    /**
     * Primerjate dve živali (animal1 in animal2) in vrnete CompatibilityResultDTO,
     * kjer vsebuje % ujemanja, kategorijo in opis te kategorije iz tabele match_categories.
     */
    public CompatibilityResultDTO compareAnimals(Integer animal1Id, Integer animal2Id) {
        // 1) Najprej prenesemo obe živali iz baze (če ne obstajata, vrnemo kakšno default vrednost ali exception).
        Animal a1 = animalRepository.findById(animal1Id)
                .orElseThrow(() -> new IllegalArgumentException("Žival z ID=" + animal1Id + " ne obstaja"));
        Animal a2 = animalRepository.findById(animal2Id)
                .orElseThrow(() -> new IllegalArgumentException("Žival z ID=" + animal2Id + " ne obstaja"));

        // 2) Nato pridobimo trait-e za vsako žival, npr. AnimalTraitDTO (imamo findTraitsByAnimalId).
        List<com.animal.persona.dto.AnimalTraitDTO> traits1 = animalTraitRepository.findTraitsByAnimalId(animal1Id);
        List<com.animal.persona.dto.AnimalTraitDTO> traits2 = animalTraitRepository.findTraitsByAnimalId(animal2Id);

        // 3) Poenostavljen izračun “compatibilityPercent”:
        //    (primer: seštejemo število enakih “pozitivnih” in “negativnih” trait-ov in delimo s skupnim številom trait-ov)
        int totalTraits = Math.max(traits1.size(), traits2.size());
        if (totalTraits == 0) totalTraits = 1; // da ne delimo z 0

        int matchCount = 0;
        for (com.animal.persona.dto.AnimalTraitDTO t1 : traits1) {
            for (com.animal.persona.dto.AnimalTraitDTO t2 : traits2) {
                if (t1.getTraitId().equals(t2.getTraitId())
                        && t1.isPositive() == t2.isPositive()) {
                    matchCount++;
                }
            }
        }
        double compatibilityPercent = (double) matchCount / totalTraits * 100.0;

        // 4) Na podlagi compatibilityPercent določimo “categoryId”.
        //    Tukaj lahko implementirate poljubna pravila po zahtevah.
        //    Recimo:
        Integer chosenCategoryId;
        if (compatibilityPercent >= 90.0) {
            chosenCategoryId = 1; // Maksimalno ujemanje
        } else if (compatibilityPercent >= 70.0) {
            chosenCategoryId = 2; // Majhne razlike
        } else if (compatibilityPercent >= 50.0) {
            chosenCategoryId = 3; // Srednje razlike
        } else if (compatibilityPercent >= 30.0) {
            chosenCategoryId = 4; // Velike razlike
        } else {
            chosenCategoryId = 5; // Neujemanje
        }

        // 5) Preberemo iz tabele match_categories ime in opis za izbrano kategorijo
        Optional<MatchCategory> maybeCat = matchCategoryRepository.findById(chosenCategoryId);
        if (maybeCat.isEmpty()) {
            // Če ni najdena, lahko pobrskamo po default (npr. id=5) ali pa vrnemo prazne vrednosti
            chosenCategoryId = 5;
            maybeCat = matchCategoryRepository.findById(5);
        }
        MatchCategory category = maybeCat.get();

        // 6) Končno sestavimo DTO in ga vrnemo
        return new CompatibilityResultDTO(
                compatibilityPercent,
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}