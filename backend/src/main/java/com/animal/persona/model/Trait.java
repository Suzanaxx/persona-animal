package com.animal.persona.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "traits")
public class Trait {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String description;

    @Column(name = "is_positive", nullable = false)
    @JsonProperty("is_positive")   // s tem poveš, da se v JSON uporablja 'is_positive'
    private boolean isPositive;

    // Konstruktorji
    public Trait() {}

    public Trait(String description, boolean isPositive) {
        this.description = description;
        this.isPositive = isPositive;
    }

    // Get/set
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPositive() {
        return isPositive;
    }

    public void setPositive(boolean positive) {
        isPositive = positive;
    }
}