package com.animal.persona.service;

import com.animal.persona.model.History;
import com.animal.persona.model.Users;
import com.animal.persona.repository.HistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public History saveSelfAssessment(Users user, Integer animalId, String name) {
        History history = new History();
        history.setUser(user);
        history.setAnimalId(animalId);
        history.setName(name);
        return historyRepository.save(history);
    }

    public List<History> getUserHistory(UUID userId) {
        return historyRepository.findByUser_Id(userId);
    }

    public Optional<History> getHistoryById(Long id) {
        return historyRepository.findById(id);
    }

    public boolean deleteHistory(Long id) {
        if (historyRepository.existsById(id)) {
            historyRepository.deleteById(id);
            return true;
        }
        return false;
    }
}