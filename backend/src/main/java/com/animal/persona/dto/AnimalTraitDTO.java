package com.animal.persona.dto;

public class AnimalTraitDTO {
    private Integer traitId;
    private String description;
    private boolean isPositive;

    public AnimalTraitDTO(Integer traitId, String description, boolean isPositive) {
        this.traitId = traitId;
        this.description = description;
        this.isPositive = isPositive;
    }

    // Getters & Setters
    public Integer getTraitId() { return traitId; }
    public void setTraitId(Integer traitId) { this.traitId = traitId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isPositive() { return isPositive; }
    public void setPositive(boolean isPositive) { this.isPositive = isPositive; }
}
