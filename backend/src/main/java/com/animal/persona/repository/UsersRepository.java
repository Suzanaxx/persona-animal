package com.animal.persona.repository;

import com.animal.persona.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    // Lahko dodaš metode po potrebi, npr. findBySessionId itd.
}