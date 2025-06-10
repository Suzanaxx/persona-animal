package com.animal.persona.service;

import com.animal.persona.model.Users;
import com.animal.persona.repository.UsersRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
        logger.info("UserService initialized");
    }

    /**
     * Vrni trenutnega uporabnika (Firebase ali anonimnega) na podlagi zahteve.
     */
    @Transactional(readOnly = true)
    public Users getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        logger.info("Received request with Authorization header: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String idToken = authHeader.substring(7);
            logger.info("Extracted ID token for processing");
            try {
                return getOrCreateFirebaseUser(idToken);
            } catch (Exception e) {
                logger.error("Invalid Firebase token or user creation failed: {}", e.getMessage(), e);
                return createOrGetAnonymousUser(request);
            }
        }

        logger.info("No Authorization header, using anonymous user");
        return createOrGetAnonymousUser(request);
    }

    /**
     * Ustvari ali vrni Firebase uporabnika na podlagi JWT tokena.
     */
    @Transactional
    public Users getOrCreateFirebaseUser(String idToken) {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                logger.warn("FirebaseApp not initialized, but this should be handled by FirebaseConfig");
                throw new IllegalStateException("FirebaseApp not initialized");
            }
            logger.info("Verifying Firebase token");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String firebaseUid = decodedToken.getUid();
            logger.info("Verified Firebase UID: {}", firebaseUid);

            Optional<Users> existingUser = usersRepository.findByFirebaseUid(firebaseUid);
            if (existingUser.isPresent()) {
                logger.info("Found existing user with firebaseUid: {}", firebaseUid);
                return existingUser.get();
            }

            logger.info("Creating new user with firebaseUid: {}", firebaseUid);
            Users newUser = new Users();
            newUser.setFirebaseUid(firebaseUid);
            newUser.setUsername(decodedToken.getEmail() != null ? decodedToken.getEmail() : "no-email@" + firebaseUid);
            newUser.setName(decodedToken.getName() != null ? decodedToken.getName() : "FirebaseUser_" + firebaseUid.substring(0, 8));
            Users savedUser = usersRepository.save(newUser);
            logger.info("User saved with id: {} and firebaseUid: {}", savedUser.getId(), savedUser.getFirebaseUid());
            return savedUser;

        } catch (Exception e) {
            logger.error("Firebase authentication or user creation failed: {}", e.getMessage(), e);
            throw new RuntimeException("Firebase authentication or user creation failed", e);
        }
    }

    /**
     * Ustvari ali vrni anonimnega uporabnika na podlagi session ID-ja.
     */
    @Transactional
    public Users createOrGetAnonymousUser(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        logger.info("Processing anonymous user with sessionId: {}", sessionId);

        Optional<Users> existingUser = usersRepository.findBySessionId(sessionId);
        if (existingUser.isPresent()) {
            logger.info("Found existing anonymous user with sessionId: {}", sessionId);
            return existingUser.get();
        }

        logger.info("Creating new anonymous user with sessionId: {}", sessionId);
        Users newUser = new Users();
        newUser.setName("Anonymous_" + sessionId.substring(0, 8));
        newUser.setSessionId(sessionId);
        try {
            Users savedUser = usersRepository.save(newUser);
            logger.info("Anonymous user saved with id: {} and sessionId: {}", savedUser.getId(), savedUser.getSessionId());
            return savedUser;
        } catch (Exception e) {
            logger.error("Failed to save anonymous user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save anonymous user", e);
        }
    }

    /**
     * Posodobi obstoječega uporabnika (npr. po registraciji).
     */
    @Transactional
    public Users updateUser(Users user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null for update");
        }
        logger.info("Updating user with id: {}", user.getId());
        return usersRepository.save(user);
    }
}