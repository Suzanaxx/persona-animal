package com.animal.persona.service;

import com.animal.persona.model.Users;
import com.animal.persona.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Users getCurrentUser(HttpServletRequest request) {
        // Pridobi session ID iz cookie-ja
        String sessionId = request.getSession().getId();

        // Poišči uporabnika po session ID
        return usersRepository.findBySessionId(sessionId)
                .orElseGet(() -> {
                    // Če uporabnik ne obstaja, ga ustvarimo
                    Users newUser = new Users();
                    newUser.setName("Anonymous");
                    newUser.setSessionId(sessionId);
                    return usersRepository.save(newUser);
                });
    }
}