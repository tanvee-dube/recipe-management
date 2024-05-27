package com.recipe.manager.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.recipe.manager.config.MessageProvider;
import com.recipe.manager.exception.NotFoundException;
import com.recipe.manager.model.IngredientRequest;
import com.recipe.manager.model.RecipeRequest;
import com.recipe.manager.model.RecipeResponse;
import com.recipe.manager.model.RecipeSearchRequest;
import com.recipe.manager.model.RecipeType;
import com.recipe.manager.model.db.Ingredient;
import com.recipe.manager.model.db.Recipe;
import com.recipe.manager.repository.IngredientRepository;
import com.recipe.manager.repository.RecipeRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;
    
    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private MessageProvider messageProvider;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    public void testCreateRecipeSuccessfully() {
        when(recipeRepository.save(any(Recipe.class))).thenReturn(getRecipe());
        RecipeResponse recipeResponse = recipeService.createRecipe(getRecipeRequest());
        Assertions.assertEquals(recipeResponse.getName(), "Pasta");
    }


    @Test
    public void testUpdateRecipeSuccessfully() {
    	var id = UUID.randomUUID();
        when(recipeRepository.save(any(Recipe.class))).thenReturn(getRecipe());
        when(recipeRepository.findById(id)).thenReturn(Optional.of(getRecipe()));
        RecipeRequest recipeRequest = getRecipeRequest();
        recipeRequest.setId(id);
        RecipeResponse recipeResponse = recipeService.updateRecipe(recipeRequest);
        Assertions.assertEquals(recipeResponse.getName(), "Pasta");
    }

    @Test
    public void testUpdateRecipeNotFound() {
    	var id = UUID.randomUUID();
        when(recipeRepository.findById(id)).thenThrow(NotFoundException.class);
        RecipeRequest recipeRequest = getRecipeRequest();
        recipeRequest.setId(id);
        Assertions.assertThrows(NotFoundException.class, () -> recipeService.updateRecipe(recipeRequest));
    }

    @Test
    public void testDeleteRecipeSuccessfully() {
    	var id = UUID.randomUUID();
        when(recipeRepository.existsById(id)).thenReturn(true);
        doNothing().when(recipeRepository).deleteById(id);
        recipeService.deleteRecipe(id);
    }

    @Test
    public void testDeleteRecipeNotFound() {
    	var id = UUID.randomUUID();
        when(recipeRepository.existsById(id)).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> recipeService.deleteRecipe(id));
    }

    @Test
    public void testFindBySearchCriteriaNotFound() {
        RecipeSearchRequest recipeSearchRequest = mock(RecipeSearchRequest.class);
        Assertions.assertThrows(RuntimeException.class, () -> recipeService.searchByCriteria(recipeSearchRequest));
    }


    private static Recipe getRecipe() {
        Recipe recipe = new Recipe();
        recipe.name("Pasta");
        recipe.id(UUID.randomUUID());
        recipe.instructions("instructions");
        recipe.numberOfServings(2);
        recipe.type(RecipeType.VEGAN);
        recipe.recipeIngredients(getIngredients());
        return recipe;
    }
    
    private static Set<Ingredient> getIngredients() {
        Ingredient ingredient1 = new Ingredient();
        ingredient1.id(UUID.randomUUID());
        ingredient1.createdAt(LocalDateTime.now());
        ingredient1.name("Tomato");
        Ingredient ingredient2 = new Ingredient();
        ingredient2.id(UUID.randomUUID());
        ingredient2.createdAt(LocalDateTime.now());
        ingredient2.name("Onion");
        return Set.of(ingredient1, ingredient2);
    }


    private static RecipeRequest getRecipeRequest() {
        RecipeRequest request = new RecipeRequest();
        request.setName("Pasta");
        request.setType(RecipeType.VEGETARIAN);
        request.setInstructions("ins");
        request.setIngredients(getIngredientRequest());
        request.setNumberOfServings(2);
        return request;
    }
    
    private static List<IngredientRequest> getIngredientRequest(){
        List<IngredientRequest> ingredients = new ArrayList<>();
        IngredientRequest request = new IngredientRequest();
        request.name("ingredient1");
        request.description("color");
        ingredients.add(request);
        return ingredients;

    }
}