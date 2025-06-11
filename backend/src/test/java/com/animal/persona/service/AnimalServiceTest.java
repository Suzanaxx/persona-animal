package com.animal.persona.service;

import com.animal.persona.dto.AnimalRequestDTO;
import com.animal.persona.model.*;
import com.animal.persona.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class AnimalServiceTest {

    @Mock private AnimalRepository animalRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private TraitRepository traitRepository;
    @Mock private AnimalTraitRepository animalTraitRepository;

    @InjectMocks
    private AnimalService animalService;

    @Test
    void addAnimalWithTraits_shouldCreateAnimalAndLinkTraits() {
        // Arrange
        AnimalRequestDTO dto = new AnimalRequestDTO();
        dto.setName("Tiger");
        dto.setImageUrl("tiger.png");
        dto.setCategoryName("zivali");

        var traitDto = new AnimalRequestDTO.TraitDTO();
        traitDto.setDescription("hraber");
        traitDto.setPositive(true);
        dto.setTraits(List.of(traitDto));

        Category category = new Category("zivali", null);
        when(categoryRepository.findByNameIgnoreCase("zivali"))
                .thenReturn(Optional.of(category));

        Animal savedAnimal = new Animal("Tiger", "tiger.png", category);
        savedAnimal.setId(1);
        when(animalRepository.save(any())).thenReturn(savedAnimal);

        Trait trait = new Trait("hraber", true);
        trait.setId(10);
        when(traitRepository.findByDescriptionIgnoreCase("hraber"))
                .thenReturn(Optional.of(trait));
        when(animalTraitRepository.existsByAnimalIdAndTraitId(1, 10)).thenReturn(false);

        // Act
        Animal result = animalService.addAnimalWithTraits(dto);

        // Assert
        assertEquals("Tiger", result.getName());
        verify(animalRepository, times(1)).save(any());
        verify(animalTraitRepository, times(1)).save(any());
    }
}
