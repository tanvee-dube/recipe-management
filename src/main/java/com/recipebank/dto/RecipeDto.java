package com.recipebank.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.recipebank.entity.Ingredient;
import com.recipebank.entity.Type;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class RecipeDto {

    private Long id;
    @NotEmpty(message = "Recipe name is mandatory")
    private String name;
    @NotEmpty(message = "Recipe instructions is mandatory")
    private String instructions;
    private Type type;
    @Min(value = 1, message = "Recipe serves should be greater than 0")
    private int serves;
    @JsonManagedReference
    @NotEmpty(message = "Recipe should have ingredients")
    private List<IngredientDto> ingredients;


}
