package com.animal.persona.service;

import com.animal.persona.model.History;
import com.animal.persona.model.Users;
import com.animal.persona.repository.HistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoryServiceTest {

    @Mock HistoryRepository historyRepository;
    @InjectMocks HistoryService historyService;

    Users dummyUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        // a minimal user stub
        dummyUser = new Users();
        dummyUser.setId(UUID.randomUUID());
        dummyUser.setName("Anon");
        dummyUser.setSessionId("sess");
    }

    @Test
    void saveSelfAssessment_persistsAndReturns() {
        // arrange
        History toSave = new History();
        toSave.setUser(dummyUser);
        toSave.setAnimalId(42);
        toSave.setName("MyTest");
        toSave.setCreatedAt(LocalDateTime.now());

        when(historyRepository.save(any(History.class)))
                .thenAnswer(inv -> {
                    History h = inv.getArgument(0);
                    h.setId(123L);
                    return h;
                });

        // act
        History saved = historyService.saveSelfAssessment(dummyUser, 42, "MyTest");

        // assert
        assertNotNull(saved.getId());
        assertEquals(dummyUser, saved.getUser());
        assertEquals(42, saved.getAnimalId());
        verify(historyRepository).save(saved);
    }

    @Test
    void getUserHistory_returnsList() {
        // arrange
        UUID uid = dummyUser.getId();
        History h1 = new History(); h1.setId(1L); h1.setUser(dummyUser);
        History h2 = new History(); h2.setId(2L); h2.setUser(dummyUser);
        when(historyRepository.findByUser_Id(uid))
                .thenReturn(List.of(h1, h2));

        // act
        List<History> list = historyService.getUserHistory(uid);

        // assert
        assertEquals(2, list.size());
        assertTrue(list.containsAll(List.of(h1, h2)));
    }

    @Test
    void deleteHistory_foundThenDeleted() {
        // arrange
        when(historyRepository.existsById(5L)).thenReturn(true);

        // act
        boolean result = historyService.deleteHistory(5L);

        // assert
        assertTrue(result);
        verify(historyRepository).deleteById(5L);
    }

    @Test
    void deleteHistory_notFound() {
        when(historyRepository.existsById(7L)).thenReturn(false);
        boolean result = historyService.deleteHistory(7L);
        assertFalse(result);
        verify(historyRepository, never()).deleteById(any());
    }
}
