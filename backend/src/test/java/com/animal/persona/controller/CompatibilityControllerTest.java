package com.animal.persona.controller;

import com.animal.persona.dto.CompatibilityResultDTO;
import com.animal.persona.service.CompatibilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CompatibilityControllerTest {

    @Mock private CompatibilityService compService;
    @InjectMocks private CompatibilityController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void standUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void compareTwo_returnsDto() throws Exception {
        CompatibilityResultDTO dto = new CompatibilityResultDTO(80.0, 2, "cat", "desc");
        when(compService.compareAnimals(1,2)).thenReturn(dto);

        mockMvc.perform(get("/api/compatibility")
                        .param("animal1","1")
                        .param("animal2","2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.compatibilityPercent").value(80.0))
                .andExpect(jsonPath("$.categoryName").value("cat"));
    }
}
