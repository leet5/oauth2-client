package com.example.oauth2client.services;

import com.example.oauth2client.domain.Ingredient;

public interface IngredientService {
    Iterable<Ingredient> findAll();
    Ingredient addIngredient(Ingredient ingredient);
}
