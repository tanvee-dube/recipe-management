package com.recipebank.request;

import lombok.Data;

import java.util.List;

@Data
public class SearchCriteriaRequest {
    List<SearchCriteria> criteria;
}
