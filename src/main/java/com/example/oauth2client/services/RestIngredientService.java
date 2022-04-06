package com.example.oauth2client.services;

import com.example.oauth2client.domain.Ingredient;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class RestIngredientService implements IngredientService {

    private final RestTemplate restTemplate;

    public RestIngredientService(String accessToken) {
        this.restTemplate = new RestTemplate();
        if (accessToken != null) {
            this.restTemplate.getInterceptors().add(getBearerTokenInterceptor(accessToken));
        }
    }

    @Override
    public Iterable<Ingredient> findAll() {
        final Ingredient[] ingredients = restTemplate.getForObject("http://localhost:8080/api/ingredients", Ingredient[].class);
        assert ingredients != null;
        return List.of(ingredients);
    }

    @Override
    public Ingredient addIngredient(Ingredient ingredient) {
        restTemplate.postForObject("http://localhost:8080/api-secured/ingredients", ingredient, Ingredient.class);
        return null;
    }

    private ClientHttpRequestInterceptor getBearerTokenInterceptor(String accessToken) {
        return (request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + accessToken);
            return execution.execute(request, body);
        };
    }
}
