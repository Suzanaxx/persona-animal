package com.animal.persona.dto;

import java.time.LocalDateTime;

public class HistoryResponseDTO {
    private String animalName;
    private String imageUrl;
    private LocalDateTime date;
    private String personaName;

    public HistoryResponseDTO(String animalName, String imageUrl, LocalDateTime date, String personaName) {
        this.animalName = animalName;
        this.imageUrl = imageUrl;
        this.date = date;
        this.personaName = personaName;
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

    public String getPersonaName() {
        return personaName;
    }

    public void setPersonaName(String personaName) {
        this.personaName = personaName;
    }
}
