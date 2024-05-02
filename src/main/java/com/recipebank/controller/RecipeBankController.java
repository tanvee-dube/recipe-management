package com.recipebank.controller;

import com.recipebank.dto.RecipeDto;
import com.recipebank.entity.Type;
import com.recipebank.request.SearchCriteriaRequest;
import com.recipebank.service.RecipeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
public class RecipeBankController {

    private final RecipeService recipeService;

    @PostMapping(path = "/recipe", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDto createRecipe(@Valid @RequestBody RecipeDto recipeDto) {
        return recipeService.create(recipeDto);
    }

    @GetMapping(path = "/recipe")
    public List<RecipeDto> getRecipes() {
        return recipeService.findAllRecipes();
    }

    @GetMapping(path = "/recipe/type")
    public List<RecipeDto> getRecipeByType(@Valid @RequestParam Type type) {
        return recipeService.recipeByType(type);
    }

    @PostMapping(path = "/recipe/criteria")
    public List<RecipeDto> searchByCriteria(@Valid @RequestBody SearchCriteriaRequest request) {
        return recipeService.searchByCriteria(request);
    }

}
