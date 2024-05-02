package com.recipebank.controller;

import com.recipebank.dto.IngredientDto;
import com.recipebank.dto.RecipeDto;
import com.recipebank.entity.Ingredient;
import com.recipebank.entity.Recipe;
import com.recipebank.entity.Type;
import com.recipebank.entity.Unit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.recipebank.util.TestUtils.asJsonString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class RequestValidatorShould {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void validateRecipeDto() throws Exception {
        RecipeDto recipe = new RecipeDto();
        IngredientDto ingredient = new IngredientDto();
        recipe.setIngredients(List.of(ingredient));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/recipe")
                        .content(asJsonString(recipe))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.instructions").value("Recipe instructions is mandatory"))
                .andExpect(jsonPath("$.serves").value("Recipe serves should be greater than 0"))
                .andExpect(jsonPath("$.name").value("Recipe name is mandatory"))
        ;
    }

    @Test
    public void validateEnumUnitType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recipe/type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("type", "VEGETERIAN1"))
                .andExpect(status().is4xxClientError());
    }

}
