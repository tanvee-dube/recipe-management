package com.recipe.manager.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public abstract class IntegrationTestHelper {
    protected ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    protected String toJson(Object obj) throws IOException {
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> List<T> getListFromMvcResult(MvcResult result, Class<T> listElementClass) throws IOException {
        return objectMapper.readValue(result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, listElementClass));
    }

    protected <T> T getFromMvcResult(MvcResult result, Class<T> objectClass) throws IOException {
        return objectMapper.readValue(result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructType(objectClass));
    }

    protected <T> T readByJsonPath(MvcResult result, String jsonPath) throws UnsupportedEncodingException {
        return JsonPath.read(result.getResponse().getContentAsString(), jsonPath);
    }
}