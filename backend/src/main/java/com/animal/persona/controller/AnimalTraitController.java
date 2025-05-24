package com.animal.persona.controller;

import com.animal.persona.dto.AnimalTraitDTO;
import com.animal.persona.model.AnimalTrait;
import com.animal.persona.model.AnimalTrait.AnimalTraitId;
import com.animal.persona.repository.AnimalTraitRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animal_traits")
@CrossOrigin(origins = "http://localhost:5173")
public class AnimalTraitController {

    private final AnimalTraitRepository animalTraitRepository;

    public AnimalTraitController(AnimalTraitRepository animalTraitRepository) {
        this.animalTraitRepository = animalTraitRepository;
    }

    @GetMapping
    public List<AnimalTrait> getAllAnimalTraits() {
        return animalTraitRepository.findAll();
    }

    @GetMapping("/{animalId}/{traitId}")
    public ResponseEntity<AnimalTrait> getAnimalTrait(
            @PathVariable Integer animalId,
            @PathVariable Integer traitId) {

        AnimalTraitId id = new AnimalTraitId(animalId, traitId);
        return animalTraitRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AnimalTrait createAnimalTrait(@RequestBody AnimalTrait animalTrait) {
        return animalTraitRepository.save(animalTrait);
    }

    @DeleteMapping("/{animalId}/{traitId}")
    public ResponseEntity<Void> deleteAnimalTrait(
            @PathVariable Integer animalId,
            @PathVariable Integer traitId) {

        AnimalTraitId id = new AnimalTraitId(animalId, traitId);
        if (animalTraitRepository.existsById(id)) {
            animalTraitRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/traits/{animalId}")
    public ResponseEntity<List<AnimalTraitDTO>> getTraitsByAnimalId(@PathVariable Integer animalId) {
        List<AnimalTraitDTO> traits = animalTraitRepository.findTraitsByAnimalId(animalId);
        if (traits.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(traits);
    }


}
