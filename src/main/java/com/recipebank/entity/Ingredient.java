package com.recipebank.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double quantity;

    @Enumerated(EnumType.ORDINAL)
    private Unit unit;

    @JsonBackReference
   // @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Recipe recipe;

}
