package com.animal.persona.controller;

import com.animal.persona.dto.AnimalResponseDTO;
import com.animal.persona.model.Animal;
import com.animal.persona.model.Category;
import com.animal.persona.repository.AnimalRepository;
import com.animal.persona.repository.CategoryRepository;
import com.animal.persona.service.AnimalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AnimalControllerTest {

    @Mock private AnimalRepository animalRepo;
    @Mock private CategoryRepository categoryRepo;
    @Mock private AnimalService animalService;

    @InjectMocks private AnimalController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAnimals_withoutCategory_returnsAll() throws Exception {
        Animal a = new Animal("Ime", "/img.png", new Category("zivali", null));
        a.setId(1);
        when(animalRepo.findAll()).thenReturn(List.of(a));

        mockMvc.perform(get("/api/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Ime"));

        verify(animalRepo).findAll();
    }

    @Test
    void getAnimalById_found_returnsDto() throws Exception {
        Animal a = new Animal("X", "/i", new Category("zivali",null));
        a.setId(42);
        when(animalRepo.findById(42)).thenReturn(Optional.of(a));

        mockMvc.perform(get("/api/animals/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.name").value("X"));
    }
}
