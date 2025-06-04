package com.animal.persona.repository;

import com.animal.persona.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findBySessionId(String sessionId);
    Optional<Users> findByFirebaseUid(String firebaseUid);
}