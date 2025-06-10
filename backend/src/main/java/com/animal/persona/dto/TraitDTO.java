package com.animal.persona.dto;

public class TraitDTO {
    private String description;
    private boolean isPositive;

    public TraitDTO() {}

    public TraitDTO(String description, boolean isPositive) {
        this.description = description;
        this.isPositive = isPositive;
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
