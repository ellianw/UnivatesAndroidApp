package com.ellianw.univates.Utils;

import com.ellianw.univates.Entities.Recipe;

import java.util.ArrayList;

public class RecipeUtils {
    public ArrayList<String> recipesToListItems(ArrayList<Recipe> recipes) {
        ArrayList<String> strs = new ArrayList<String>();
        recipes.forEach(recipe -> strs.add(recipe.getListName()));
        return strs;
    };
}
