package com.animal.persona.controller;

import com.animal.persona.controller.HistoryController;
import com.animal.persona.dto.HistoryResponseDTO;
import com.animal.persona.model.Animal;
import com.animal.persona.model.History;
import com.animal.persona.model.Users;
import com.animal.persona.repository.AnimalRepository;
import com.animal.persona.service.HistoryService;
import com.animal.persona.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HistoryControllerTest {

    @Mock private HistoryService historyService;
    @Mock private UserService userService;
    @Mock private AnimalRepository animalRepository;
    @InjectMocks private HistoryController controller;            // ← in tukaj mora biti kontroler

    private MockMvc mockMvc;
    private Users anon;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        anon = new Users("Anonymous", "sess");
        anon.setId(UUID.randomUUID());
    }

    @Test
    void getSelfAssessments_returnsDtos() throws Exception {
        // 1) userService vrne anonimca
        when(userService.getCurrentUser(any())).thenReturn(anon);

        // 2) historyService vrne en zapis samoocenitev
        History h = new History();
        h.setAnimalId(5);
        h.setCreatedAt(LocalDateTime.of(2025,1,1,12,0));
        when(historyService.getUserHistory(anon.getId()))
                .thenReturn(List.of(h));

        // 3) animalRepository vrne dummy žival
        Animal a = new Animal();
        a.setId(5);
        a.setName("TestAnimal");
        a.setImageUrl("/img/test.png");
        when(animalRepository.findById(5)).thenReturn(Optional.of(a));

        // 4) Poženi GET in preveri JSON
        mockMvc.perform(get("/api/history/self-assessments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].animalName").value("TestAnimal"))
                .andExpect(jsonPath("$[0].imageUrl").value("/img/test.png"))
                .andExpect(jsonPath("$[0].date").exists())
                .andExpect(jsonPath("$[0].personName").value(""));

        // 5) Preveri, da so se klici zgodili
        verify(historyService).getUserHistory(anon.getId());
        verify(animalRepository).findById(5);
    }
}
