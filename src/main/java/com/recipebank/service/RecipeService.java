package com.recipebank.service;

import com.recipebank.dto.RecipeDto;
import com.recipebank.entity.Recipe;
import com.recipebank.entity.Type;
import com.recipebank.repo.RecipeRepo;
import com.recipebank.request.SearchCriteria;
import com.recipebank.request.SearchCriteriaRequest;
import com.recipebank.utils.RecipeUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepo recipeRepo;

    private final ModelMapper modelMapper;


    public RecipeService(RecipeRepo recipeRepo, ModelMapper modelMapper) {

        this.recipeRepo = recipeRepo;
        this.modelMapper = modelMapper;
    }

    public RecipeDto create(RecipeDto dto) {
        Recipe recipe = modelMapper.map(dto, Recipe.class);
        return modelMapper.map(recipeRepo.save(recipe), RecipeDto.class);
    }

    public List<RecipeDto> findAllRecipes() {
        return toDto(recipeRepo.findAll());
    }

    public List<RecipeDto> recipeByType(Type type) {
        return toDto(recipeRepo.findByType(type));
    }

    public List<RecipeDto> searchByCriteria(SearchCriteriaRequest request) {

        List<SearchCriteria> criteria = request.getCriteria();
        Map<String, String> methodNames = criteria.stream().collect(Collectors.toMap(c -> c.getKey()
                + c.getOperation(), c -> c.getValue()));

        Specification<Recipe> specifications =
                methodNames.entrySet().stream().map(e -> RecipeUtils.containsSpecification(e.getKey(), e.getValue()))
                        .reduce(Specification::and).orElseThrow(RuntimeException::new);


        return toDto(recipeRepo.findAll(specifications));
    }

    private List<RecipeDto> toDto(List<Recipe> recipes) {
        return recipes.stream().map(e -> modelMapper.map(e, RecipeDto.class))
                .collect(Collectors.toList());
    }
}
