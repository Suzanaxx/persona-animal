package com.animal.persona.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // ali IDENTITY, a ker je UUID, AUTO je priporočeno
    private UUID id;

    @Column(name="firebase_uid", nullable = true, unique = true)
    private String firebaseUid;

    @Column(nullable = true, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(name = "session_id", length = 255, unique = true)
    private String sessionId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Users() { }

    public Users(String name, String sessionId) {
        this.name = name;
        this.sessionId = sessionId;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters & setters ...

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
