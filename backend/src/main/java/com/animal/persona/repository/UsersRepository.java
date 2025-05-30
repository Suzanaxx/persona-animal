package com.animal.persona.repository;

import com.animal.persona.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {
    // Tako bo Spring Data JPA sam kreiral poizvedbo SELECT * FROM users WHERE session_id = :sessionId
    Optional<Users> findBySessionId(String sessionId);
}
