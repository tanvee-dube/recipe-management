package com.recipe.manager.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.recipe.manager.api.RecipeApi;
import com.recipe.manager.model.RecipeRequest;
import com.recipe.manager.model.RecipeResponse;
import com.recipe.manager.model.RecipeSearchRequest;
import com.recipe.manager.service.RecipeService;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@AllArgsConstructor
public class RecipeController implements RecipeApi {
    private final RecipeService recipeService;

    /**
     * Get all recipes
     * @return List of recipes
     */
    @Override
    public ResponseEntity<List<RecipeResponse>> getAllRecipes() {
        log.info("Fetching all the recipes");
        return ResponseEntity.ok(recipeService.getAllRecipes());
    }

    /**
     * Get the recipe by id
     * @param recipeId ID of the recipe to return 
     * @return RecipeResponse
     */
   @Override
    public ResponseEntity<RecipeResponse> getRecipeById(@PathVariable UUID recipeId) {
        log.info("Fetching the recipe by its id. Id: {}", recipeId);
        return ResponseEntity.ok(recipeService.getRecipeById(recipeId));
    }

    /**
     * Create a new recipe
     * @param request  
     * @return (status code 201) with created recipe
     */
    @Override
    public ResponseEntity<RecipeResponse> addRecipe(@Valid @RequestBody RecipeRequest request) {
        log.info("Creating the recipe with properties");
        return new ResponseEntity<>(recipeService.createRecipe(request), HttpStatus.CREATED);
    }

    /**
     * Update the recipe by given properties
     * @param updateRecipeRequest 
     * @return (status code 200) with updated recipe
     */
    @Override
    public ResponseEntity<RecipeResponse> updateRecipe(@Valid @RequestBody RecipeRequest updateRecipeRequest) {
        log.info("Updating the recipe by given properties");
        return ResponseEntity.ok(recipeService.updateRecipe(updateRecipeRequest));
    }

    /**
     * Delete the recipe by its id
     * @param recipeId ID of the recipe to delete 
     * @return (status code 204)
     */
    @Override
    public ResponseEntity<Void> deleteRecipeById(@PathVariable UUID recipeId) {
        log.info("Deleting the recipe by its id. Id: {}", recipeId);
        recipeService.deleteRecipe(recipeId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search the recipes by given criteria
     * @param recipeSearchRequest  
     * @return List of recipes
     */
    @Override
    public ResponseEntity<List<RecipeResponse>> searchRecipes(@Valid @RequestBody RecipeSearchRequest recipeSearchRequest) {
        log.info("Searching the recipe by given criteria");
        return ResponseEntity.ok(recipeService.searchByCriteria(recipeSearchRequest));
    }
}