package com.animal.persona.service;

import com.animal.persona.model.Trait;
import com.animal.persona.repository.TraitRepository;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class TraitServiceTest {

    @Mock private TraitRepository traitRepository;
    @InjectMocks private TraitService traitService;

    @Test
    void findOrCreateTrait_shouldReturnExistingTrait() {
        Trait trait = new Trait("pogumen", true);
        when(traitRepository.findByDescriptionIgnoreCase("pogumen"))
                .thenReturn(Optional.of(trait));

        Trait result = traitService.findOrCreateTrait("pogumen", true);
        assertEquals("pogumen", result.getDescription());
        verify(traitRepository, never()).save(any());
    }

    @Test
    void findOrCreateTrait_shouldCreateNewTraitIfNotExist() {
        when(traitRepository.findByDescriptionIgnoreCase("nov")).thenReturn(Optional.empty());

        Trait saved = new Trait("nov", false);
        when(traitRepository.save(any())).thenReturn(saved);

        Trait result = traitService.findOrCreateTrait("nov", false);

        assertEquals("nov", result.getDescription());
        assertFalse(result.isPositive());
        verify(traitRepository).save(any());
    }
}
