package com.ellianw.univates;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ellianw.univates.DAO.RecipeDAO;
import com.ellianw.univates.Entities.Recipe;
import com.ellianw.univates.Utils.DatabaseManager;
import com.ellianw.univates.Utils.RecipeUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static DatabaseManager dbManager = null;
    private static SQLiteDatabase db = null;
    private static RecipeDAO dao = null;
    private RecipeUtils utils = new RecipeUtils();

    private ListView list = null;
    private Button btDelete = null;
    private Button btEdit = null;
    private Button btNew = null;
    private Button btInfo = null;

    private Integer lastSelectedPosition = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DatabaseManager(this, "aplicacaodb", 1);
        db = dbManager.getWritableDatabase();
        dao = new RecipeDAO(dbManager,db);

        list = findViewById(R.id.listview);
        btDelete = findViewById(R.id.bt_delete);
        btEdit = findViewById(R.id.bt_edit);
        btNew = findViewById(R.id.bt_new);
        btInfo = findViewById(R.id.bt_info);

        fillList();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btInfo.setVisibility(View.VISIBLE);
                btDelete.setVisibility(View.VISIBLE);
                btEdit.setVisibility(View.VISIBLE);
                lastSelectedPosition = position;
            }
        });

        btNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(null);
            }
        });

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(getSelectedId());
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.deleteRecipeById(getSelectedId());
                Toast.makeText(MainActivity.this, "Receita excluída!", Toast.LENGTH_SHORT).show();
                fillList();
            }
        });

        btInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe = dao.getRecipeById(getSelectedId());
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Detalhes da Receita")
                        .setMessage(Html.fromHtml(
                                "<b>Nome:</b> " + recipe.getName() + "<br>" +
                                        "<b>Descrição:</b> " + recipe.getDescription() + "<br>" +
                                        "<b>Pessoas servidas:</b> " + recipe.getServes() + "<br>" +
                                        "<b>Custo de preparação:</b> R$ " + recipe.getCost(),
                                Html.FROM_HTML_MODE_LEGACY
                        ))
                        .setPositiveButton("Fechar", null);

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void fillList(){
        btInfo.setVisibility(View.GONE);
        btDelete.setVisibility(View.GONE);
        btEdit.setVisibility(View.GONE);
        lastSelectedPosition = null;
        list.setAdapter(null);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayList<String> items = utils.recipesToListItems(dao.getAllRecipes());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, items);
        list.setAdapter(adapter);
    }

    private void changeActivity(Integer editItem){
        Intent intencao_de_navegacao = new Intent(MainActivity.this, RecipeInsertActivity.class);
        if (editItem != null) {
            intencao_de_navegacao.putExtra("to_edit_item",editItem);
        }
        startActivity(intencao_de_navegacao);
    }

    @Override
    protected void onResume() {
        fillList();
        super.onResume();
    }

    public static DatabaseManager getDbManager() {
        return dbManager;
    }

    public static SQLiteDatabase getDb() {
        return db;
    }

    public static RecipeDAO getDao() {
        return dao;
    }

    public Integer getSelectedId(){
        Object editItem = list.getItemAtPosition(lastSelectedPosition);
        return Integer.valueOf(editItem.toString().split("\\.")[0].trim());
    }
}
