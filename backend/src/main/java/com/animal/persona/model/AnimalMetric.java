package com.animal.persona.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "animal_metrics")
public class AnimalMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "animal_id", nullable = false)
    private Integer animalId;

    @Column(nullable = false, length = 50)
    private String dimension;

    @Column(name = "avg_value", nullable = false, precision = 5, scale = 2)
    private BigDecimal avgValue;

    @Column(name = "p25_value", nullable = false, precision = 5, scale = 2)
    private BigDecimal p25Value;

    @Column(name = "p50_value", nullable = false, precision = 5, scale = 2)
    private BigDecimal p50Value;

    @Column(name = "p75_value", nullable = false, precision = 5, scale = 2)
    private BigDecimal p75Value;

    // Getters & setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getAnimalId() { return animalId; }
    public void setAnimalId(Integer animalId) { this.animalId = animalId; }

    public String getDimension() { return dimension; }
    public void setDimension(String dimension) { this.dimension = dimension; }

    public BigDecimal getAvgValue() { return avgValue; }
    public void setAvgValue(BigDecimal avgValue) { this.avgValue = avgValue; }

    public BigDecimal getP25Value() { return p25Value; }
    public void setP25Value(BigDecimal p25Value) { this.p25Value = p25Value; }

    public BigDecimal getP50Value() { return p50Value; }
    public void setP50Value(BigDecimal p50Value) { this.p50Value = p50Value; }

    public BigDecimal getP75Value() { return p75Value; }
    public void setP75Value(BigDecimal p75Value) { this.p75Value = p75Value; }
}