package com.recipe.manager.service;

import com.recipe.manager.config.MessageProvider;
import com.recipe.manager.model.IngredientRequest;
import com.recipe.manager.model.IngredientResponse;
import com.recipe.manager.model.RecipeRequest;
import com.recipe.manager.model.RecipeResponse;
import com.recipe.manager.model.RecipeSearchRequest;
import com.recipe.manager.model.SearchCriteria;
import com.recipe.manager.model.db.Ingredient;
import com.recipe.manager.model.db.Recipe;
import com.recipe.manager.repository.IngredientRepository;
import com.recipe.manager.repository.RecipeRepository;
import com.recipe.manager.util.RecipeUtil;
import com.recipe.manager.exception.NotFoundException;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Transactional
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    
    private final IngredientRepository ingredientRepository;

    
    private final MessageProvider messageProvider;

    
    public RecipeResponse createRecipe(RecipeRequest request) {
    	
    	var ingredients =checkIngredientsExists(request.getIngredients());
    	Recipe recipe = recipeRepository.save(createRecipeDetails(request,ingredients));
        return recipeResponse(recipe);
        
    }


    public List<RecipeResponse> getAllRecipes() {
         
    	 return recipeRepository.findAll().stream().map(this::recipeResponse).collect(toList());
     }    

    public RecipeResponse getRecipeById(UUID id) {
        Recipe recipe =  recipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageProvider.getMessage("recipe.notFound")));
         return recipeResponse(recipe);
    }

    public RecipeResponse updateRecipe(RecipeRequest request) {
        return recipeRepository.findById(request.getId()).map(recipe -> { Recipe updatedRecipe = recipeRepository.save(updateRecipeDetails(request, recipe));
                 return recipeResponse(updatedRecipe);
        }).orElseThrow(() -> new NotFoundException(messageProvider.getMessage("recipe.notFound")));

    }

    public void deleteRecipe(UUID id) {
        if (!recipeRepository.existsById(id)) {
            throw new NotFoundException(messageProvider.getMessage("recipe.notFound"));
        }
        recipeRepository.deleteById(id);
    }

    public List<RecipeResponse> searchByCriteria(RecipeSearchRequest request) {

        List<SearchCriteria> criteria = request.getCriteria();
        Map<String, String> methodNames = criteria.stream().collect(Collectors.toMap(c -> c.getFilterKey().getValue()
                + c.getOperation(), c -> c.getValue()));

       Specification<Recipe> specifications =
                methodNames.entrySet().stream().map(e -> RecipeUtil.containsSpecification(e.getKey(), e.getValue()))
                        .reduce(Specification::and).orElseThrow(RuntimeException::new);
       
   	   return recipeRepository.findAll(specifications).stream().map(this::recipeResponse).collect(toList());
    }
    
    private Set<Ingredient> checkIngredientsExists(List<IngredientRequest> ingredients) {
     	
    	return ingredients.stream().map(ingredient -> createOrUpdateIngredient(ingredient)).collect(Collectors.toSet());
    }
    
    private Ingredient createOrUpdateIngredient(IngredientRequest request) {
    	var ingredient = ingredientRepository.findByName(request.getName());
    	if (ingredient.isPresent()) {
    		return ingredientRepository.save(ingredient.get().toBuilder().updatedAt(LocalDateTime.now()).build());
    	}
    	else {
        	return Ingredient.builder().name(request.getName()).description(request.getDescription()).build();

    	}
    }
    
    private Recipe createRecipeDetails(RecipeRequest request,Set<Ingredient> ingredients) {
        
        return Recipe.builder().name(request.getName()).instructions(request.getInstructions()).type(request.getType()).numberOfServings(request.getNumberOfServings()).recipeIngredients(ingredients).build();
    }
    
    
    private Recipe updateRecipeDetails(RecipeRequest request, Recipe existingRecipe) {
        return existingRecipe.toBuilder().name(request.getName()).type(request.getType()).instructions(request.getInstructions()).numberOfServings(request.getNumberOfServings()).build();
    }
    
    private RecipeResponse recipeResponse(Recipe recipe) {

    	        RecipeResponse recipeResponse = new RecipeResponse();
    	        recipeResponse.setId(recipe.id());
    	        recipeResponse.setName(recipe.name());
    	        recipeResponse.setInstructions(recipe.instructions());
    	        recipeResponse.setType(recipe.type().name());
    	        recipeResponse.setNumberOfServings(recipe.numberOfServings());
    	        recipeResponse.setIngredients(recipe.recipeIngredients().stream().map(this::ingredientsResponse).collect(toList()));
    	        return recipeResponse;
   }    

    private IngredientResponse ingredientsResponse(Ingredient ingredient) {
       
            IngredientResponse ingredientResponse = new IngredientResponse();
            ingredientResponse.setId(ingredient.id());
            ingredientResponse.setName(ingredient.name());
            ingredientResponse.setDescription(ingredient.description());
      
        return ingredientResponse;
    }
    
  
    
   
}
