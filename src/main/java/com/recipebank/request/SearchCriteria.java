package com.recipebank.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SearchCriteria {
    @NotEmpty(message = "Key in the criteria can't be empty")
    private String key;
    @NotEmpty(message = "Operation in the criteria can't be empty")
    private String operation;
    @NotEmpty(message = "Value in the criteria can't be empty")
    private String value;

}
