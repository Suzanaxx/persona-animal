package com.animal.persona.repository;

import com.animal.persona.model.MatchCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchCategoryRepository extends JpaRepository<MatchCategory, Integer> {

}