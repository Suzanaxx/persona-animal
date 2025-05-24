package com.animal.persona.controller;

import com.animal.persona.model.Animal;
import com.animal.persona.service.SelfAssessmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/self-assessment")
public class SelfAssessmentController {

    private final SelfAssessmentService service;

    public SelfAssessmentController(SelfAssessmentService service) {
        this.service = service;
    }

    @PostMapping("/start")
    public void startSession() {
        service.resetSession();
    }

    @GetMapping("/next")
    public List<Animal> getNextPair() {
        Optional<List<Animal>> next = service.getNextPair();
        return next.orElse(List.of()); // prazno -> konec ocenjevanja
    }
}
