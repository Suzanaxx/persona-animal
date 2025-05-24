package com.animal.persona.controller;

import com.animal.persona.model.Animal;
import com.animal.persona.repository.AnimalRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {

    private final AnimalRepository animalRepository;

    public AnimalController(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    // GET /api/animals - pridobi vse živali
    @GetMapping(produces = "application/json")
    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    // GET /api/animals/{id} - pridobi žival po id-ju
    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable Integer id) {
        Optional<Animal> animal = animalRepository.findById(id);
        if (animal.isPresent()) {
            return ResponseEntity.ok(animal.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/animals - ustvari novo žival
    @PostMapping
    public Animal createAnimal(@RequestBody Animal animal) {
        return animalRepository.save(animal);
    }

    // PUT /api/animals/{id} - posodobi žival
    @PutMapping("/{id}")
    public ResponseEntity<Animal> updateAnimal(@PathVariable Integer id, @RequestBody Animal updatedAnimal) {
        return animalRepository.findById(id)
                .map(animal -> {
                    animal.setName(updatedAnimal.getName());
                    animal.setImageUrl(updatedAnimal.getImageUrl());
                    animalRepository.save(animal);
                    return ResponseEntity.ok(animal);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/animals/{id} - izbriši žival
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Integer id) {
        if (animalRepository.existsById(id)) {
            animalRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
