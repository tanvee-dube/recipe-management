package com.recipe.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.manager.model.db.Ingredient;

import java.util.Optional;
import java.util.UUID;

public interface IngredientRepository extends JpaRepository<Ingredient, UUID>{

	Optional<Ingredient> findByName(String name);

}
