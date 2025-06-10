package com.animal.persona.controller;

import com.animal.persona.model.Trait;
import com.animal.persona.repository.TraitRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/traits")
@CrossOrigin(origins = "http://localhost:5173") // CORS za frontend
public class TraitController {

    private final TraitRepository traitRepository;

    public TraitController(TraitRepository traitRepository) {
        this.traitRepository = traitRepository;
    }

    @GetMapping
    public List<Trait> getAllTraits() {
        return traitRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trait> getTraitById(@PathVariable Integer id) {
        return traitRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createTrait(@RequestBody Trait trait) {
        if (traitRepository.findAll().stream()
                .anyMatch(t -> t.getDescription().equalsIgnoreCase(trait.getDescription()))) {
            return ResponseEntity.badRequest().body("Trait with this description already exists");
        }
        Trait saved = traitRepository.save(trait);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trait> updateTrait(@PathVariable Integer id, @RequestBody Trait updatedTrait) {
        return traitRepository.findById(id)
                .map(trait -> {
                    trait.setDescription(updatedTrait.getDescription());
                    trait.setPositive(updatedTrait.isPositive());
                    traitRepository.save(trait);
                    return ResponseEntity.ok(trait);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrait(@PathVariable Integer id) {
        if (traitRepository.existsById(id)) {
            traitRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}