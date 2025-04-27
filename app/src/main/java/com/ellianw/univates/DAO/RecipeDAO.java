package com.ellianw.univates.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ellianw.univates.Entities.Recipe;
import com.ellianw.univates.Utils.DatabaseManager;

import java.util.ArrayList;

public class RecipeDAO {
    private DatabaseManager dbManager = null;
    private SQLiteDatabase db = null;

    public RecipeDAO(DatabaseManager dbManager, SQLiteDatabase db) {
        this.dbManager = dbManager;
        this.db = db;
    }

    public ArrayList<Recipe> getAllRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        db = dbManager.getReadableDatabase();
        Cursor iCursor = db.rawQuery("SELECT * FROM receitas", null);
        iCursor.moveToFirst();
        for (int i = 1; i <= iCursor.getCount(); i++) {
            Integer id = iCursor.getInt(iCursor.getColumnIndexOrThrow("id"));
            String name = iCursor.getString(iCursor.getColumnIndexOrThrow("nome"));
            String desc = iCursor.getString(iCursor.getColumnIndexOrThrow("descricao"));
            Integer serves = iCursor.getInt(iCursor.getColumnIndexOrThrow("qtde_pessoas_servidas"));
            Double cost = iCursor.getDouble(iCursor.getColumnIndexOrThrow("custo_preparacao"));
            recipes.add(new Recipe(id,name,desc,serves,cost));
            iCursor.moveToNext();
        }
        return recipes;
    }

    public void insertRecipe(Recipe recipe) {
        ContentValues values = new ContentValues();

        values.put("id", recipe.getId());
        values.put("nome", recipe.getName());
        values.put("descricao", recipe.getDescription());
        values.put("qtde_pessoas_servidas", recipe.getServes());
        values.put("custo_preparacao", recipe.getCost());

        db.insert("receitas", null, values);
    }

    public void updateRecipe(Recipe recipe) {
        if (recipe.getId() == null) {
            throw new IllegalArgumentException("ID da receita não pode ser nulo para atualização.");
        }

        ContentValues values = new ContentValues();
        values.put("nome", recipe.getName());
        values.put("descricao", recipe.getDescription());
        values.put("qtde_pessoas_servidas", recipe.getServes());
        values.put("custo_preparacao", recipe.getCost());

        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(recipe.getId()) };

        db.update("receitas", values, whereClause, whereArgs);
    }

    public Recipe getRecipeById(int id) {
        String[] columns = {"id", "nome", "descricao", "qtde_pessoas_servidas", "custo_preparacao"};
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query("receitas", columns, selection, selectionArgs, null, null, null);

        Recipe recipe = null;
        if (cursor.moveToFirst()) {
            int recipeId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
            int serves = cursor.getInt(cursor.getColumnIndexOrThrow("qtde_pessoas_servidas"));
            double cost = cursor.getDouble(cursor.getColumnIndexOrThrow("custo_preparacao"));

            recipe = new Recipe(recipeId, name, description, serves, cost);
        }

        cursor.close();
        return recipe;
    }

    public void deleteRecipeById(int id) {
        db.delete("receitas", "id = ?", new String[]{String.valueOf(id)});
    }
}
