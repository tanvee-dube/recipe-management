package com.recipe.manager.controller;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import com.recipe.manager.RecipeManagerApplication;
import com.recipe.manager.model.IngredientRequest;
import com.recipe.manager.model.RecipeRequest;
import com.recipe.manager.model.RecipeResponse;
import com.recipe.manager.model.RecipeSearchRequest;
import com.recipe.manager.model.RecipeType;
import com.recipe.manager.model.SearchCriteria;
import com.recipe.manager.model.SearchCriteria.FilterKeyEnum;
import com.recipe.manager.model.SearchCriteria.OperationEnum;
import com.recipe.manager.repository.RecipeRepository;
import com.recipe.manager.util.IntegrationTestHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RecipeManagerApplication.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext
public class RecipeControllerITTest extends IntegrationTestHelper {
    @Autowired
    private MockMvc mockMvc;
    
    @Mock
    private RecipeRepository recipeRepository;

    @Test
    public void testCreateRecipeSuccessfully() throws Exception {
        RecipeRequest request = new RecipeRequest();
        request.setName("rice");
        request.setType(RecipeType.NON_VEGETARIAN);
        request.setInstructions("hot and spicy");
        request.setNumberOfServings(10);
        request.setIngredients(getIngredientRequest());
                mockMvc.perform(post("/api/v1/recipes")
                                .content(toJson(request))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").exists())
                        .andExpect(jsonPath("$.name").value("rice"));
    }

    @Test
    public void testGetRecipeSuccessfully() throws Exception {
    	RecipeRequest request = new RecipeRequest();
        request.setName("bread");
        request.setType(RecipeType.NON_VEGETARIAN);
        request.setInstructions("spongy");
        request.setNumberOfServings(3);
        request.setIngredients(getIngredientRequest());
        MvcResult createdRecipe = mockMvc.perform(post("/api/v1/recipes")
                                .content(toJson(request))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isCreated()).andReturn();
        RecipeResponse recipeResponse = getFromMvcResult(createdRecipe, RecipeResponse.class);
             
        mockMvc.perform(get("/api/v1/recipes/" + recipeResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("bread"))
                .andExpect(jsonPath("$.instructions").value("spongy"))
                .andExpect(jsonPath("$.numberOfServings").value(3));
    }
   
    @Test
    public void testGetRecipeNotFound() throws Exception {

        mockMvc.perform(get("/api/v1/recipes/10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void testGetAllRecipesSuccessfully() throws Exception {
        mockMvc.perform(get("/api/v1/recipes"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateRecipeSuccessfully() throws Exception {
        RecipeRequest request = new RecipeRequest();
        request.setName("pizza");
        request.setType(RecipeType.NON_VEGETARIAN);
        request.setInstructions("hot and spicy");
        request.setNumberOfServings(5);
        request.setIngredients(getIngredientRequest());
       MvcResult mvcResult = mockMvc.perform(post("/api/v1/recipes")
                        .content(toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated()).andReturn();
        RecipeResponse recipeResponse = getFromMvcResult(mvcResult, RecipeResponse.class);
        RecipeRequest updateRequest = new RecipeRequest();
        updateRequest.setName("lasanga");
        updateRequest.setType(RecipeType.NON_VEGETARIAN);
        updateRequest.setInstructions("hot and spicy");
        updateRequest.setNumberOfServings(3);
        updateRequest.setIngredients(getIngredientRequest());
        updateRequest.setId(recipeResponse.getId());
        mockMvc.perform(put("/api/v1/recipes")
                .content(toJson(updateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("lasanga"));
    }


    @Test
    public void testUpdateRecipeNotFound() throws Exception {
        RecipeRequest request = new RecipeRequest();
        request.setName("popcorn");
        request.setId(UUID.randomUUID());
        request.setType(RecipeType.NON_VEGETARIAN);
        request.setInstructions("hot and spicy");
        request.setNumberOfServings(7);
        request.setIngredients(getIngredientRequest());
        mockMvc.perform(put("/api/v1/recipe")
                        .content(toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteRecipeSuccessfully() throws Exception {
    	RecipeRequest request = new RecipeRequest();
        request.setName("lentil");
        request.setType(RecipeType.NON_VEGETARIAN);
        request.setInstructions("hot and spicy");
        request.setNumberOfServings(10);
        request.setIngredients(getIngredientRequest());
        MvcResult createdRecipe = mockMvc.perform(post("/api/v1/recipes")
                                .content(toJson(request))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isCreated()).andReturn();
        RecipeResponse recipeResponse = getFromMvcResult(createdRecipe, RecipeResponse.class);
        mockMvc.perform(delete("/api/v1/recipes/" + recipeResponse.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteRecipeNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/recipes/20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void testSearchRecipeByCriteriaSuccessfully() throws Exception {
        //create the recipe
        RecipeRequest createRecipeRequest = new RecipeRequest();
        createRecipeRequest.setName("pasta");
        createRecipeRequest.setType(RecipeType.VEGETARIAN);
        createRecipeRequest.setInstructions("ins");
        createRecipeRequest.setNumberOfServings(2);
        createRecipeRequest.setIngredients(getIngredientRequest());
        MvcResult createdRecipe = mockMvc.perform(post("/api/v1/recipes")
                        .content(toJson(createRecipeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String type = readByJsonPath(createdRecipe, "$.type");

        RecipeSearchRequest request = new RecipeSearchRequest();
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setFilterKey(FilterKeyEnum.TYPE);
        searchCriteria.setOperation(OperationEnum.EQUALS);
        searchCriteria.setValue(type);

        searchCriteriaList.add(searchCriteria);

        request.setCriteria(searchCriteriaList);

        mockMvc.perform(post("/api/v1/recipes/search")
                .content(toJson(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void testSearchRecipeByCriteriaFails() throws Exception {
        mockMvc.perform(post("/api/v1/recipes/search")
                .content(toJson(null))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }
    
    private List<IngredientRequest> getIngredientRequest(){
        List<IngredientRequest> ingredients = new ArrayList<>();
        IngredientRequest request = new IngredientRequest();
        request.name("ingredient1");
        request.description("color");
        ingredients.add(request);
        return ingredients;

    }
    


}