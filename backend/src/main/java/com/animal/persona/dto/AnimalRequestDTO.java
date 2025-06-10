package com.animal.persona.dto;

import java.util.List;

public class AnimalRequestDTO {
    private String name;
    private String imageUrl;
    private String categoryName;
    private List<TraitDTO> traits;

    public AnimalRequestDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<TraitDTO> getTraits() {
        return traits;
    }

    public void setTraits(List<TraitDTO> traits) {
        this.traits = traits;
    }

    public static class TraitDTO {
        private String description;
        private boolean isPositive;

        public TraitDTO() {}

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
}

