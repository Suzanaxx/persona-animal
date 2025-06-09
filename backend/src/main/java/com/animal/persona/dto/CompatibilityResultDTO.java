package com.animal.persona.dto;

public class CompatibilityResultDTO {
    private double compatibilityPercent;
    private String categoryName;
    private String categoryDescription;
    private Integer categoryId;

    public CompatibilityResultDTO(double compatibilityPercent, Integer categoryId, String categoryName, String categoryDescription) {
        this.compatibilityPercent = compatibilityPercent;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
    }

    public double getCompatibilityPercent() { return compatibilityPercent; }
    public void setCompatibilityPercent(double compatibilityPercent) { this.compatibilityPercent = compatibilityPercent; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getCategoryDescription() { return categoryDescription; }
    public void setCategoryDescription(String categoryDescription) { this.categoryDescription = categoryDescription; }
}