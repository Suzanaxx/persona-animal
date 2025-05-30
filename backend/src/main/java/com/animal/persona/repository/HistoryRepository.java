package com.animal.persona.repository;

import com.animal.persona.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByUser_Id(UUID userId);
}