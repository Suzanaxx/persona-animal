package com.animal.persona.controller;

import com.animal.persona.dto.CompatibilityResultDTO;
import com.animal.persona.service.CompatibilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compatibility")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CompatibilityController {

    private final CompatibilityService compatibilityService;

    public CompatibilityController(CompatibilityService compatibilityService) {
        this.compatibilityService = compatibilityService;
    }


    @GetMapping
    public ResponseEntity<CompatibilityResultDTO> compareTwo(
            @RequestParam("animal1") Integer animal1,
            @RequestParam("animal2") Integer animal2) {

        CompatibilityResultDTO result = compatibilityService.compareAnimals(animal1, animal2);
        return ResponseEntity.ok(result);
    }
}