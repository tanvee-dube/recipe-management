package com.recipe.manager.util;

import com.recipe.manager.exception.InvalidSearchCriteriaException;
import com.recipe.manager.model.RecipeType;
import com.recipe.manager.model.db.Recipe;
import com.recipe.manager.repository.specification.RecipeSpecifications;

import org.springframework.data.jpa.domain.Specification;

public final class RecipeUtil {
    public static Specification<Recipe> containsSpecification(String name, String value) {
        if ("NUMBER_OF_SERVINGSEQUALS".equals(name)) {
            return RecipeSpecifications.servingsEqual(Integer.parseInt(value));
        } else if ("TYPEEQUALS".equals(name)) {
            return RecipeSpecifications.typeEqual(RecipeType.valueOf(value));
        }
        else if ("INGREDIENTSEQUALS".equals(name)) {
            return RecipeSpecifications.ingredientEquals(value);
        } else if ("INGREDIENTSNOT_EQUALS".equals(name)) {
            return RecipeSpecifications.ingredientNotEquals(value);
        } else if ("INSTRUCTIONSCONTAINS".equals(name)) {
            return RecipeSpecifications.instructionsContains(value);
        } else {
            throw new InvalidSearchCriteriaException("Invalid Search Criteria!!!");
        }
    }
}
