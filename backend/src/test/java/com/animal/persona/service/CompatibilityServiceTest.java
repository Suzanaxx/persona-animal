package com.animal.persona.service;

import com.animal.persona.dto.AnimalTraitDTO;
import com.animal.persona.dto.CompatibilityResultDTO;
import com.animal.persona.model.Animal;
import com.animal.persona.model.MatchCategory;
import com.animal.persona.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class CompatibilityServiceTest {

    @Mock private AnimalRepository animalRepository;
    @Mock private AnimalTraitRepository animalTraitRepository;
    @Mock private MatchCategoryRepository matchCategoryRepository;

    @InjectMocks
    private CompatibilityService compatibilityService;

    @Test
    void compareAnimals_shouldReturnCorrectCompatibility() {
        // Arrange
        Animal a1 = new Animal(); a1.setId(1); a1.setName("Lion");
        Animal a2 = new Animal(); a2.setId(2); a2.setName("Tiger");

        when(animalRepository.findById(1)).thenReturn(Optional.of(a1));
        when(animalRepository.findById(2)).thenReturn(Optional.of(a2));

        AnimalTraitDTO trait1 = new AnimalTraitDTO(1, "hraber", true);
        AnimalTraitDTO trait2 = new AnimalTraitDTO(1, "hraber", true);

        when(animalTraitRepository.findTraitsByAnimalId(1)).thenReturn(List.of(trait1));
        when(animalTraitRepository.findTraitsByAnimalId(2)).thenReturn(List.of(trait2));

        MatchCategory category = new MatchCategory();
        category.setId(1); category.setName("Popolna ujemanje");
        category.setDescription("Popolnoma enaka.");

        when(matchCategoryRepository.findById(1)).thenReturn(Optional.of(category));

        // Act
        CompatibilityResultDTO result = compatibilityService.compareAnimals(1, 2);

        // Assert
        assertEquals(100.0, result.getCompatibilityPercent());
        assertEquals("Popolna ujemanje", result.getCategoryName());
    }
}
