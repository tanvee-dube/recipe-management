package com.recipebank.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.recipebank.entity.Unit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientDto {
    private Long id;
    @NotEmpty(message = "Ingredient name is mandatory")
    private String name;
    @Min(value = 2, message = "Ingredient quantity should be greater than 0")
    private Double quantity;
    private Unit unit;
    @JsonBackReference
    private RecipeDto recipe;

}
