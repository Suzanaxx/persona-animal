package com.animal.persona.controller;

import com.animal.persona.dto.AnimalRequestDTO;
import com.animal.persona.dto.AnimalResponseDTO;
import com.animal.persona.model.Animal;
import com.animal.persona.model.Category;
import com.animal.persona.repository.AnimalRepository;
import com.animal.persona.repository.CategoryRepository;
import com.animal.persona.service.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {

    private final AnimalRepository animalRepository;
    private final CategoryRepository categoryRepository;
    private final AnimalService animalService;

    public AnimalController(
            AnimalRepository animalRepository,
            CategoryRepository categoryRepository,
            AnimalService animalService
    ) {
        this.animalRepository = animalRepository;
        this.categoryRepository = categoryRepository;
        this.animalService = animalService;
    }

    // DTO za osnovni CREATE/UPDATE
    public static class AnimalDto {
        private String name;
        private String imageUrl;
        private Integer categoryId;

        public AnimalDto() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

        public Integer getCategoryId() { return categoryId; }
        public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    }

    /**
     * GET /api/animals
     * filtriranje po categoryId in mapiranje na DTO
     */
    @GetMapping
    public ResponseEntity<List<AnimalResponseDTO>> getAnimals(
            @RequestParam(value = "categoryId", required = false) Integer categoryId
    ) {
        List<Animal> animals = (categoryId != null)
                ? animalRepository.findByCategory_Id(categoryId)
                : animalRepository.findAll();

        List<AnimalResponseDTO> dtos = animals.stream()
                .map(a -> new AnimalResponseDTO(a.getId(), a.getName(), a.getImageUrl()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/animals/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponseDTO> getAnimalById(@PathVariable Integer id) {
        return animalRepository.findById(id)
                .map(a -> new AnimalResponseDTO(a.getId(), a.getName(), a.getImageUrl()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * POST /api/animals
     * Osnovno ustvarjanje brez traitov
     */
    @PostMapping
    public ResponseEntity<?> createAnimal(@RequestBody AnimalDto dto) {
        Optional<Category> catOpt = categoryRepository.findById(dto.getCategoryId());
        if (catOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Category not found");
        }
        Animal entity = new Animal(dto.getName(), dto.getImageUrl(), catOpt.get());
        Animal saved = animalRepository.save(entity);

        AnimalResponseDTO responseDto = new AnimalResponseDTO(
                saved.getId(), saved.getName(), saved.getImageUrl()
        );
        URI location = URI.create("/api/animals/" + saved.getId());
        return ResponseEntity.created(location).body(responseDto);
    }

    /**
     * PUT /api/animals/{id}
     * Osnovno posodabljanje brez traitov
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnimal(
            @PathVariable Integer id,
            @RequestBody AnimalDto dto
    ) {
        Optional<Category> catOpt = categoryRepository.findById(dto.getCategoryId());
        if (catOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Category not found");
        }

        return animalRepository.findById(id)
                .map(animal -> {
                    animal.setName(dto.getName());
                    animal.setImageUrl(dto.getImageUrl());
                    animal.setCategory(catOpt.get());
                    Animal saved = animalRepository.save(animal);
                    AnimalResponseDTO responseDto = new AnimalResponseDTO(
                            saved.getId(), saved.getName(), saved.getImageUrl()
                    );
                    return ResponseEntity.ok(responseDto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/animals/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Integer id) {
        if (animalRepository.existsById(id)) {
            animalRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * POST /api/animals/with-traits
     * Ustvari žival + traits – tukaj uporabljamo AnimalRequestDTO
     */
    @PostMapping("/with-traits")
    public ResponseEntity<AnimalResponseDTO> addAnimalWithTraits(
            @RequestBody AnimalRequestDTO request
    ) {
        Animal created = animalService.addAnimalWithTraits(request);
        AnimalResponseDTO responseDto = new AnimalResponseDTO(
                created.getId(), created.getName(), created.getImageUrl()
        );
        URI location = URI.create("/api/animals/" + created.getId());
        return ResponseEntity.created(location).body(responseDto);
    }
}
