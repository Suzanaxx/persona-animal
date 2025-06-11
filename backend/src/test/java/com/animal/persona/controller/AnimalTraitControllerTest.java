package com.animal.persona.controller;

import com.animal.persona.dto.AnimalTraitDTO;
import com.animal.persona.model.AnimalTrait;
import com.animal.persona.model.AnimalTrait.AnimalTraitId;
import com.animal.persona.repository.AnimalTraitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AnimalTraitControllerTest {

    @Mock private AnimalTraitRepository traitRepo;
    @InjectMocks private AnimalTraitController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getTraitsByAnimalId_returnsDtoList() throws Exception {
        AnimalTraitDTO dto = new AnimalTraitDTO(5, "desc", true);
        when(traitRepo.findTraitsByAnimalId(7)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/animal_traits/traits/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].traitId").value(5))
                .andExpect(jsonPath("$[0].description").value("desc"));
    }
}
