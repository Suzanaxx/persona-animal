package com.animal.persona.dto;

import java.time.LocalDateTime;

public class SelfFullDTO {
    private Long historyId;
    private Integer animalId;
    private String imageUrl;
    private LocalDateTime date;

    public SelfFullDTO(Long historyId, Integer animalId, String imageUrl, LocalDateTime date) {
        this.historyId = historyId;
        this.animalId = animalId;
        this.imageUrl = imageUrl;
        this.date = date;
    }

    public Long getHistoryId() { return historyId; }
    public void setHistoryId(Long historyId) { this.historyId = historyId; }

    public Integer getAnimalId() { return animalId; }
    public void setAnimalId(Integer animalId) { this.animalId = animalId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
