package com.animal.persona.service;

import com.animal.persona.model.History;
import com.animal.persona.model.Users;
import com.animal.persona.repository.HistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HistoryService {
    private static final Logger logger = LoggerFactory.getLogger(HistoryService.class);
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public History saveSelfAssessment(Users user, Integer animalId, String name) {
        logger.info("Saving self-assessment for user: {}, animalId: {}, name: {}", user.getId(), animalId, name);
        if (user == null || animalId == null) {
            throw new IllegalArgumentException("User and animalId must not be null");
        }

        History history = new History();
        history.setUser(user);
        history.setAnimalId(animalId);
        history.setName(name);
        history.setCreatedAt(LocalDateTime.now()); // Dodano za konsistentnost
        History savedHistory = historyRepository.save(history);
        logger.info("Self-assessment saved successfully with id: {}", savedHistory.getId());
        return savedHistory;
    }

    public List<History> getUserHistory(UUID userId) {
        logger.info("Fetching history for userId: {}", userId);
        return historyRepository.findByUser_Id(userId);
    }

    public Optional<History> getHistoryById(Long id) {
        logger.info("Fetching history by id: {}", id);
        return historyRepository.findById(id);
    }

    public boolean deleteHistory(Long id) {
        logger.info("Deleting history by id: {}", id);
        if (historyRepository.existsById(id)) {
            historyRepository.deleteById(id);
            logger.info("History deleted successfully");
            return true;
        }
        logger.warn("History not found for id: {}", id);
        return false;
    }
}