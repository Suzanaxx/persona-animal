package com.animal.persona.dto;

import java.time.LocalDateTime;

public class HistoryResponseDTO {
    private String animalName;
    private String imageUrl;
    private LocalDateTime date;
    private String personName; // ← Popravljeno tukaj

    public HistoryResponseDTO(String animalName, String imageUrl, LocalDateTime date, String personName) {
        this.animalName = animalName;
        this.imageUrl = imageUrl;
        this.date = date;
        this.personName = personName;
    }

    public HistoryResponseDTO(String animalName, String imageUrl, LocalDateTime date) {
        this(animalName, imageUrl, date, "");
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
