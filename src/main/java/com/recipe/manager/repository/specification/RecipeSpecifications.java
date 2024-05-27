package com.recipe.manager.repository.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.recipe.manager.model.RecipeType;
import com.recipe.manager.model.db.Ingredient;
import com.recipe.manager.model.db.Recipe;

@Repository
public class RecipeSpecifications {


    public static Specification<Recipe> servingsEqual(int serves) {
        return (recipe, cq, cb) -> cb.equal(recipe.get("numberOfServings"), serves);
    }

    public static Specification<Recipe> typeEqual(RecipeType type) {
        return (recipe, cq, cb) -> cb.equal(recipe.get("type"), type);
    }

    public static Specification<Recipe> ingredientEquals(String ingredient) {
        return (recipe, cq, cb) -> {
            Join<Ingredient, Recipe> recipeWithIngredient = recipe.join("recipeIngredients");
            return cb.equal(recipeWithIngredient.get("name"), ingredient);
        };
    }

    public static Specification<Recipe> ingredientNotEquals(String ingredient) {
        return (recipe, cq, cb) -> {
            Join<Ingredient, Recipe> recipeWithIngredient = recipe.join("recipeIngredients");
            return cb.notEqual(recipeWithIngredient.get("name"), ingredient);
        };
    }

    public static Specification<Recipe> instructionsContains(String toSearch) {
        return (recipe, cq, cb) -> cb.like(recipe.get("instructions"), "%"+toSearch+"%");
    }
}
