package com.animal.persona.service;

import com.animal.persona.model.Users;
import com.animal.persona.repository.UsersRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    // ✅ 1. Anonimni uporabnik (session)
    public Users getCurrentUser(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        return usersRepository.findBySessionId(sessionId)
                .orElseGet(() -> {
                    Users newUser = new Users();
                    newUser.setName("Anonymous");
                    newUser.setSessionId(sessionId);
                    return usersRepository.save(newUser);
                });
    }

    // ✅ 2. Prijavljen uporabnik (Firebase JWT)
    public Users getOrCreateFirebaseUser(String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String firebaseUid = decodedToken.getUid();

            return usersRepository.findByFirebaseUid(firebaseUid)
                    .orElseGet(() -> {
                        try {
                            UserRecord firebaseUser = FirebaseAuth.getInstance().getUser(firebaseUid);

                            Users newUser = new Users();
                            newUser.setFirebaseUid(firebaseUid);
                            newUser.setUsername(firebaseUser.getEmail());
                            newUser.setName(firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "FirebaseUser");
                            newUser.setCreatedAt(LocalDateTime.now());

                            return usersRepository.save(newUser);
                        } catch (Exception e) {
                            throw new RuntimeException("Could not fetch user data from Firebase", e);
                        }
                    });

        } catch (Exception e) {
            throw new RuntimeException("Invalid Firebase token", e);
        }
    }
}
