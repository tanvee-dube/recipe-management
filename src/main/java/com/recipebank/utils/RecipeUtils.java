package com.recipebank.utils;

import com.recipebank.entity.Recipe;
import com.recipebank.exception.SearchCriteriaInvalidException;
import com.recipebank.repo.specifications.RecipeSpecifications;
import org.springframework.data.jpa.domain.Specification;

public final class RecipeUtils {
    public static Specification<Recipe> containsSpecification(String name, String value) {
        if ("servesequals".equals(name)) {
            return RecipeSpecifications.servesEqual(Integer.parseInt(value));
        } else if ("ingredientequals".equals(name)) {
            return RecipeSpecifications.ingredientEquals(value);
        } else if ("ingredientnotequals".equals(name)) {
            return RecipeSpecifications.ingredientNotEquals(value);
        } else if ("instructionscontains".equals(name)) {
            return RecipeSpecifications.instructionsContains(value);
        } else {
            throw new SearchCriteriaInvalidException("Search Criteria not correct");
        }
    }
}
