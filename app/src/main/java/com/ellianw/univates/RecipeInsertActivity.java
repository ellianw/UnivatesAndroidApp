package com.ellianw.univates;

import com.ellianw.univates.DAO.RecipeDAO;
import com.ellianw.univates.Entities.Recipe;
import com.ellianw.univates.Utils.DatabaseManager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeInsertActivity extends AppCompatActivity {
    private DatabaseManager dbManager = MainActivity.getDbManager();
    private SQLiteDatabase db = MainActivity.getDb();
    private RecipeDAO dao = MainActivity.getDao();

    private Recipe editingRecipe = null;

    private EditText etName = null;
    private EditText etDescription = null;
    private EditText etServes = null;
    private EditText etPrepCost = null;
    private Button btSave = null;
    private Button btBack = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        etName = findViewById(R.id.name);
        etDescription = findViewById(R.id.description);
        etServes = findViewById(R.id.quantity);
        etPrepCost = findViewById(R.id.prep_cost);
        btSave = findViewById(R.id.save);
        btBack = findViewById(R.id.back);

        Intent intent = getIntent();
        int itemId = intent.getIntExtra("to_edit_item", -1); // -1 é o valor padrão caso não exista
        if (itemId > 0) {
            editingRecipe = dao.getRecipeById(itemId);
        }

        if (editingRecipe != null) {
            fillFieldsWithRecipe(editingRecipe);
        }

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (missingField(findViewById(R.id.root))) {
                    Toast.makeText(RecipeInsertActivity.this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editingRecipe == null) {
                    String name = etName.getText().toString();
                    String description = etDescription.getText().toString();
                    int serves = Integer.parseInt(etServes.getText().toString());
                    double cost = Double.parseDouble(etPrepCost.getText().toString());

                    Recipe recipe = new Recipe(name, description, serves, cost);

                    dao.insertRecipe(recipe);

                    Toast.makeText(RecipeInsertActivity.this, "Receita criada: " + recipe.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    editingRecipe.setName(etName.getText().toString());
                    editingRecipe.setDescription(etDescription.getText().toString());
                    try {
                        int serves = Integer.parseInt(etServes.getText().toString());
                        editingRecipe.setServes(serves);
                    } catch (NumberFormatException e) {
                        editingRecipe.setServes(0);
                    }
                    try {
                        double cost = Double.parseDouble(etPrepCost.getText().toString());
                        editingRecipe.setCost(cost);
                    } catch (NumberFormatException e) {
                        editingRecipe.setCost(0.0);
                    }

                    dao.updateRecipe(editingRecipe);
                }
                clearForm(findViewById(R.id.root));
                finish();
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm(findViewById(R.id.root));
                finish();
            }
        });
    }

    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) { // Loop pelo ViewGroup
            View view = group.getChildAt(i);
            if (view instanceof EditText) { //Se a view for um EditText
                ((EditText) view).setText("");
                continue;
            }
            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0)) //Se for outro ViewGroup
                clearForm((ViewGroup) view); //Faz uma chamada recursiva
        }
    }

    private boolean missingField(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) { // Loop pelo ViewGroup
            View view = group.getChildAt(i);
            if (view instanceof EditText) { //Se a view for um EditText
                if (((EditText) view).getText().toString().isBlank()) return true;
                continue;
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0)) //Se for outro ViewGroup(RadioGroup)
                missingField((ViewGroup)view); //Faz uma chamada recursiva
        }
        return false;
    }

    public void fillFieldsWithRecipe(Recipe recipe) {
        etName.setText(recipe.getName());
        etDescription.setText(recipe.getDescription());
        etServes.setText(String.valueOf(recipe.getServes()));
        etPrepCost.setText(String.valueOf(recipe.getCost()));
    }
}
