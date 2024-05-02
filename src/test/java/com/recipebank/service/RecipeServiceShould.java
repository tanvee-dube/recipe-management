package com.recipebank.service;

import com.recipebank.entity.Ingredient;
import com.recipebank.entity.Recipe;
import com.recipebank.entity.Type;
import com.recipebank.entity.Unit;
import com.recipebank.repo.RecipeRepo;
import com.recipebank.request.SearchCriteria;
import com.recipebank.request.SearchCriteriaRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceShould {

    @Mock
    RecipeRepo recipeRepo;

    @Mock
    ModelMapper mapper;

    @Mock
    SearchCriteriaRequest searchCriteriaRequest;

    @Test
    public void searchByCriteria() {
        SearchCriteria sc1 = new SearchCriteria();
        sc1.setKey("ingredient");
        sc1.setOperation("notequals");
        sc1.setValue("salmon");
        SearchCriteria sc2 = new SearchCriteria();
        sc2.setKey("instructions");
        sc2.setOperation("contains");
        sc2.setValue("Test");

        List<SearchCriteria> criteria = List.of(sc1, sc2);

        Recipe recipe = new Recipe();
        recipe.setName("test");
        recipe.setServes(4);
        recipe.setType(Type.VEGETERIAN);
        recipe.setInstructions("Test Instructions");

        Ingredient ingredient = new Ingredient();
        ingredient.setName("Test ingredient");
        ingredient.setQuantity(2.0);
        ingredient.setUnit(Unit.KILOGRAM);


        recipe.setIngredients(List.of(ingredient));

        RecipeService service = new RecipeService(recipeRepo, mapper);
        when(searchCriteriaRequest.getCriteria()).thenReturn(criteria);
        when(recipeRepo.findAll(any(Specification.class))).thenReturn(List.of(recipe));
        service.searchByCriteria(searchCriteriaRequest);

    }
}
