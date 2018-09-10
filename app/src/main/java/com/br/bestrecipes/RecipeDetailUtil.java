package com.br.bestrecipes;

import com.br.bestrecipes.bean.Ingredient;

import java.util.List;

public class RecipeDetailUtil {

    public static String formatIngredients(List<Ingredient> ingredients) {
        StringBuilder builder = new StringBuilder();
        for (Ingredient i : ingredients) {
            builder.append((int) i.getQuantity());
            builder.append(" ").append(i.getMeasure());
            builder.append(" ").append(i.getIngredient()).append("\n");
        }
        return builder.toString();
    }

}
