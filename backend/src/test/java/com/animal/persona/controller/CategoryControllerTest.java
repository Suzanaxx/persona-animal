package com.animal.persona.controller;

import com.animal.persona.model.Category;
import com.animal.persona.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    @Mock private CategoryService catService;
    @InjectMocks private CategoryController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void prep() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllCategories_returnsList() throws Exception {
        when(catService.findAll()).thenReturn(List.of(new Category("zivali","/img")));
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("zivali"));
    }

    @Test
    void getCategoryById_found() throws Exception {
        Category c = new Category("x","/i");
        c.setId(3);
        when(catService.findById(3)).thenReturn(Optional.of(c));

        mockMvc.perform(get("/api/categories/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("x"));
    }
}
