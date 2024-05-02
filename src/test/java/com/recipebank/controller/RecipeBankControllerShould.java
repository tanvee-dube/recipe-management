package com.recipebank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipebank.entity.Ingredient;
import com.recipebank.entity.Recipe;
import com.recipebank.entity.Type;
import com.recipebank.entity.Unit;
import com.recipebank.request.SearchCriteria;
import com.recipebank.request.SearchCriteriaRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.recipebank.util.TestUtils.asJsonString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class RecipeBankControllerShould {

    @Autowired
    private MockMvc mockMvc;



    @BeforeEach
    public void createRecipe() throws Exception {
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

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/recipe")
                        .content(asJsonString(recipe))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }


    @Test
    public void getRecipes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].id").exists());

    }

    @Test
    public void getRecipeByType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recipe/type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("type", "VEGETERIAN"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].ingredients").isArray())
                .andExpect(jsonPath("$.[0].id").exists());
    }

    @Test
    public void searchByCriteriaWithServeEqualAndIngredientEqualsCriteria() throws Exception {
        SearchCriteria sc1 = new SearchCriteria();
        sc1.setKey("serves");
        sc1.setOperation("equals");
        sc1.setValue("4");
        SearchCriteria sc2 = new SearchCriteria();
        sc2.setKey("ingredient");
        sc2.setOperation("equals");
        sc2.setValue("Test ingredient");

        List<SearchCriteria> criteria = List.of(sc1, sc2);
        SearchCriteriaRequest request = new SearchCriteriaRequest();
        request.setCriteria(criteria);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/recipe/criteria")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].ingredients").isArray())
                .andExpect(jsonPath("$.[0].id").exists());
    }

    @Test
    public void searchByCriteriaWithIngredientNotEqualsAndInstructionsEqualsCriteria() throws Exception {
        SearchCriteria sc1 = new SearchCriteria();
        sc1.setKey("ingredient");
        sc1.setOperation("notequals");
        sc1.setValue("salmon");
        SearchCriteria sc2 = new SearchCriteria();
        sc2.setKey("instructions");
        sc2.setOperation("contains");
        sc2.setValue("Test");

        List<SearchCriteria> criteria = List.of(sc1, sc2);
        SearchCriteriaRequest request = new SearchCriteriaRequest();
        request.setCriteria(criteria);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/recipe/criteria")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].ingredients").isArray())
                .andExpect(jsonPath("$.[0].id").exists());
    }

}
