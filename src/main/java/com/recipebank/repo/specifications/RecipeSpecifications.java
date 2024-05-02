package com.recipebank.repo.specifications;

import com.recipebank.entity.Ingredient;
import com.recipebank.entity.Recipe;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class RecipeSpecifications {


    public static Specification<Recipe> servesEqual(int serves) {
        return (recipe, cq, cb) -> cb.equal(recipe.get("serves"), serves);
    }

    public static Specification<Recipe> servesGreaterThen(int serves) {
        return (recipe, cq, cb) -> cb.greaterThan(recipe.get("serves"), serves);
    }

    public static Specification<Recipe> ingredientEquals(String ingredient) {
        return (recipe, cq, cb) -> {
            Join<Ingredient, Recipe> recipeWithIngredient = recipe.join("ingredients");
            return cb.equal(recipeWithIngredient.get("name"), ingredient);
        };
    }

    public static Specification<Recipe> ingredientNotEquals(String ingredient) {
        return (recipe, cq, cb) -> {
            Join<Ingredient, Recipe> recipeWithIngredient = recipe.join("ingredients");
            return cb.notEqual(recipeWithIngredient.get("name"), ingredient);
        };
    }

    public static Specification<Recipe> instructionsContains(String toSearch) {
        return (recipe, cq, cb) -> cb.like(recipe.get("instructions"), "%"+toSearch+"%");
    }
}
