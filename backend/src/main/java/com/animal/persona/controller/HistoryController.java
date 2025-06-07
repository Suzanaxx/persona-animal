package com.animal.persona.controller;

import com.animal.persona.dto.HistoryRequestOtherDTO;
import com.animal.persona.dto.HistoryResponseDTO;
import com.animal.persona.model.Animal;
import com.animal.persona.model.History;
import com.animal.persona.model.Users;
import com.animal.persona.repository.AnimalRepository;
import com.animal.persona.repository.HistoryRepository;
import com.animal.persona.service.HistoryService;
import com.animal.persona.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class HistoryController {

    private final HistoryService historyService;
    private final UserService userService;
    private final AnimalRepository animalRepository;
    private final HistoryRepository historyRepository;

    public HistoryController(
            HistoryService historyService,
            UserService userService,
            AnimalRepository animalRepository,
            HistoryRepository historyRepository
    ) {
        this.historyService = historyService;
        this.userService = userService;
        this.animalRepository = animalRepository;
        this.historyRepository = historyRepository;
    }

    @PostMapping("/self-assessment")
    public ResponseEntity<History> saveSelfAssessment(
            @RequestBody SelfAssessmentRequest request,
            HttpServletRequest httpRequest) {

        Users user = userService.getCurrentUser(httpRequest);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        History history = historyService.saveSelfAssessment(
                user,
                request.getAnimalId(),
                "Samoocenitev " + System.currentTimeMillis()
        );

        return ResponseEntity.ok(history);
    }

    /**
     * Novi endpoint: vrne seznam zapisov v obliki HistoryResponseDTO
     * – pridobimo userja, poiščemo v history, nato (za vsak history vnos)
     * naredimo join na tabelo animals, da dobimo ime in sliko.
     */
    @GetMapping("/self-assessments")
    public ResponseEntity<List<HistoryResponseDTO>> getSelfAssessments(HttpServletRequest httpRequest) {
        Users user = userService.getCurrentUser(httpRequest);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        UUID userId = user.getId();

        // Pridobimo samo tiste zapise, kjer je personName null ali prazen (torej samoocenitve)
        List<History> historyList = historyService.getUserHistory(userId).stream()
                .filter(h -> h.getPersonName() == null || h.getPersonName().isBlank())
                .collect(Collectors.toList());

        List<HistoryResponseDTO> dtoList = historyList.stream().map(history -> {
            Animal animal = animalRepository.findById(history.getAnimalId())
                    .orElse(null);

            if (animal == null) {
                return new HistoryResponseDTO(
                        "NEZNANA ŽIVAL",
                        "",
                        history.getCreatedAt()
                );
            }

            return new HistoryResponseDTO(
                    animal.getName(),
                    animal.getImageUrl(),
                    history.getCreatedAt()
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<History> getHistoryById(@PathVariable Long id) {
        return historyService.getHistoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistory(@PathVariable Long id) {
        if (historyService.deleteHistory(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // DTO za POST request
    public static class SelfAssessmentRequest {
        private Integer animalId;

        public Integer getAnimalId() {
            return animalId;
        }

        public void setAnimalId(Integer animalId) {
            this.animalId = animalId;
        }
    }

    //od tu naprej je za ocene drugih oseb

    public static class OtherAssessmentRequest {
        private Integer animalId;
        private String personName;

        public Integer getAnimalId() {
            return animalId;
        }

        public void setAnimalId(Integer animalId) {
            this.animalId = animalId;
        }

        public String getPersonName() {
            return personName;
        }

        public void setPersonName(String personName) {
            this.personName = personName;
        }
    }

    @PostMapping("/other-assessment")
    public ResponseEntity<Void> saveOtherAssessment(@RequestBody HistoryRequestOtherDTO request,
                                                    HttpServletRequest httpRequest) {
        Users user = userService.getCurrentUser(httpRequest);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        System.out.println("Shranjujem oceno za: " + request.getPersonName());

        History history = new History();
        history.setUser(user);
        history.setAnimalId(request.getAnimalId());
        history.setCreatedAt(LocalDateTime.now());
        history.setPersonName(request.getPersonName());
        history.setName(request.getPersonName());// ← pomembno: tukaj gre ime druge osebe

        historyRepository.save(history);
        return ResponseEntity.ok().build();


    }

    @GetMapping("/other-assessments")
    public ResponseEntity<List<HistoryResponseDTO>> getOtherAssessments(HttpServletRequest httpRequest) {
        Users user = userService.getCurrentUser(httpRequest);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        UUID userId = user.getId();
        List<History> historyList = historyService.getUserHistory(userId);

        List<HistoryResponseDTO> dtoList = historyList.stream()
                .filter(h -> !h.getPersonName().startsWith("Samoocenitev"))
                .map(history -> {
                    Animal animal = animalRepository.findById(history.getAnimalId())
                            .orElse(null);
                    if (animal == null) {
                        return new HistoryResponseDTO("NEZNANA ŽIVAL", "", history.getCreatedAt(), history.getPersonName());
                    }
                    return new HistoryResponseDTO(
                            animal.getName(),
                            animal.getImageUrl(),
                            history.getCreatedAt(),
                            history.getPersonName()
                    );
                }).collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }


}
