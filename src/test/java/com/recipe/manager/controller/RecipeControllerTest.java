package com.recipe.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.manager.model.IngredientRequest;
import com.recipe.manager.model.IngredientResponse;
import com.recipe.manager.model.RecipeRequest;
import com.recipe.manager.model.RecipeResponse;
import com.recipe.manager.model.RecipeType;
import com.recipe.manager.service.RecipeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecipeControllerTest {
    @Mock
    private RecipeService recipeService;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new RecipeController(recipeService)).build();
    }

    @Test
    public void test_createRecipe_successfully() throws Exception {
        RecipeRequest request = new RecipeRequest();
        request.setName("pasta");
        request.setType(RecipeType.VEGETARIAN);
        request.setInstructions("instructions");
        request.setNumberOfServings(2);
        request.setIngredients(getIngredientRequest());
        when(recipeService.createRecipe(any())).thenReturn(getRecipeResponse());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("pasta"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions").value("instructions"));
    }


    @Test
    public void test_listRecipe_successfully() throws Exception {
        when(recipeService.getAllRecipes()).thenReturn(getAllRecipesResponse());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("pasta"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].instructions").value("instructions"));
    }

    @Test
    public void test_updateRecipe_successfully() throws Exception {
        RecipeRequest request = new RecipeRequest();
        request.setName("pasta");
        request.setType(RecipeType.VEGETARIAN);
        request.setInstructions("instructions");
        request.setNumberOfServings(2);
        request.setId(UUID.randomUUID());
        request.setIngredients(getIngredientRequest());
        request.setId(UUID.randomUUID());
        when(recipeService.updateRecipe(any(RecipeRequest.class))).thenReturn(getRecipeResponse());
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("pasta"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions").value("instructions"));
    }

    @Test
    public void test_deleteRecipe_successfully() throws Exception {
        doNothing().when(recipeService).deleteRecipe(UUID.randomUUID());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/recipes/04b83ac9-6c27-4af9-8601-14b32ed32021"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_getRecipeById_successfully() throws Exception {
        when(recipeService.getRecipeById(UUID.fromString("04b83ac9-6c27-4af9-8601-14b32ed32021"))).thenReturn(getRecipeResponse());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/04b83ac9-6c27-4af9-8601-14b32ed32021"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("pasta"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.instructions").value("instructions"));
    }

    @Test
    public void test_createRecipe_withInvalidRequest() throws Exception {
        RecipeRequest request = new RecipeRequest();
        //request.setName("pasta");
        request.setType(RecipeType.VEGETARIAN);
        request.setInstructions("instructions");
        request.setNumberOfServings(2);
        request.setIngredients(getIngredientRequest());
       
        when(recipeService.createRecipe(any())).thenReturn(getRecipeResponse());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_updateRecipe_withInvalidRequest() throws Exception {
        RecipeRequest request = new RecipeRequest();
        request.setName("pasta");
        request.setType(RecipeType.VEGETARIAN);
        request.setInstructions("instructions");
        //request.setNumberOfServings(2);
        request.setIngredients(getIngredientRequest());
        request.setId(UUID.randomUUID());
        when(recipeService.updateRecipe(any(RecipeRequest.class))).thenReturn(getRecipeResponse());
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_getRecipeById_withInvalidRequest() throws Exception {
        when(recipeService.getRecipeById(UUID.randomUUID())).
                thenReturn(getRecipeResponse());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/8"))
                .andExpect(status().isBadRequest());
    }

    private List<IngredientRequest> getIngredientRequest(){
        List<IngredientRequest> ingredients = new ArrayList<>();
        IngredientRequest request = new IngredientRequest();
        request.name("ingredient1");
        request.description("color");
        ingredients.add(request);
        return ingredients;

    }
    private List<RecipeResponse> getAllRecipesResponse() {
        List<RecipeResponse> recipes = new ArrayList<>();
        recipes.add(getRecipeResponse());
        return recipes;
    }

    private RecipeResponse getRecipeResponse() {
        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setId(UUID.fromString("04b83ac9-6c27-4af9-8601-14b32ed32021"));
        recipeResponse.name("pasta");
        recipeResponse.createdDate(LocalDateTime.now().atZone(ZoneId.systemDefault()).toOffsetDateTime());
        recipeResponse.instructions("instructions");
        recipeResponse.type(RecipeType.VEGETARIAN.name());
        recipeResponse.numberOfServings(2);
        recipeResponse.ingredients(getAllIngredientsResponse());
        return recipeResponse;
    }

    private List<IngredientResponse> getAllIngredientsResponse() {
        List<IngredientResponse> ingredients = new ArrayList<>();
        ingredients.add(getIngredientResponse());
        return ingredients;
    }
    
    private IngredientResponse getIngredientResponse() {
        IngredientResponse ingredientResponse = new IngredientResponse();
        ingredientResponse.id(UUID.randomUUID());
        ingredientResponse.name("ingredient1");
        return ingredientResponse;
    }
}