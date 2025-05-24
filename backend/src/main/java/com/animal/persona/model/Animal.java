package com.animal.persona.model;

import jakarta.persistence.*;

@Entity
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    // Constructors
    public Animal() {}

    public Animal(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    // Getters & Setters
    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
