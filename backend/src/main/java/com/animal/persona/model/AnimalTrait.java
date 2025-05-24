package com.animal.persona.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "animal_traits")
@IdClass(AnimalTrait.AnimalTraitId.class)
public class AnimalTrait {

    @Id
    @Column(name = "animal_id", nullable = false)
    private Integer animalId;

    @Id
    @Column(name = "trait_id", nullable = false)
    private Integer traitId;

    // Konstruktorji
    public AnimalTrait() {}

    public AnimalTrait(Integer animalId, Integer traitId) {
        this.animalId = animalId;
        this.traitId = traitId;
    }

    // Get/set
    public Integer getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Integer animalId) {
        this.animalId = animalId;
    }

    public Integer getTraitId() {
        return traitId;
    }

    public void setTraitId(Integer traitId) {
        this.traitId = traitId;
    }

    // IdClass za sestavljeni ključ
    public static class AnimalTraitId implements Serializable {
        private Integer animalId;
        private Integer traitId;

        public AnimalTraitId() {}

        public AnimalTraitId(Integer animalId, Integer traitId) {
            this.animalId = animalId;
            this.traitId = traitId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AnimalTraitId that)) return false;
            return Objects.equals(animalId, that.animalId) && Objects.equals(traitId, that.traitId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(animalId, traitId);
        }
    }
}
