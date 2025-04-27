package com.ellianw.univates;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ellianw.univates.Utils.DatabaseManager;

import java.util.ArrayList;

public class SqlManipulationActivity extends AppCompatActivity {
    private EditText etItem = null;
    private EditText etQuantidade = null;
    private Button btDelete = null;
    private Button btEdit = null;
    private DatabaseManager dataBaseManager = null;
    private SQLiteDatabase bancoDeDados = null;
    private ListView itemsList = null;
    private int lastSelectedPosition = 0;
    private String editedItemName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql_manipulation);

        dataBaseManager = new DatabaseManager(this, "aplicacaodb", 1);
        bancoDeDados = dataBaseManager.getWritableDatabase();

        etItem = findViewById(R.id.name);
        etQuantidade = findViewById(R.id.quantity);
        itemsList = findViewById(R.id.listview);
        btDelete = findViewById(R.id.bt_delete);
        btEdit = findViewById(R.id.bt_edit);

        Button btBotaoEnviar = findViewById(R.id.bt_send);
        btBotaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues valores = new ContentValues();
                valores.put("descricao", etItem.getText().toString());
                valores.put("quantidade", etQuantidade.getText().toString());

                if (editedItemName != null) {
                    deleteFromDB(editedItemName);
                }
                bancoDeDados = dataBaseManager.getWritableDatabase();
                long resultado = bancoDeDados.insert("item", null, valores);
                bancoDeDados.close();

                if (resultado == -1)
                    Toast.makeText(SqlManipulationActivity.this, "Não foi possível inserir este item", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(SqlManipulationActivity.this, "Item inserido: " + etItem.getText() + " n: " + etQuantidade.getText(), Toast.LENGTH_SHORT).show();
                }
                clearForm(findViewById(R.id.root));
                fillList();
                editedItemName = null;
            }
        });
        fillList();
        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btDelete.setEnabled(true);
                btEdit.setEnabled(true);
                lastSelectedPosition = position;
            }
        });
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object deletedItem = itemsList.getItemAtPosition(lastSelectedPosition);
                String objString = deletedItem.toString().split("\\|")[0].trim();
                deleteFromDB(objString);
                fillList();
            }
        });
        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object editItem = itemsList.getItemAtPosition(lastSelectedPosition);
                String objString = editItem.toString().split("\\|")[0].trim();
                setEditItem(objString);
                fillList();
            }
        });
    }

    private void fillList(){
        itemsList.setAdapter(null);
        btDelete.setEnabled(false);
        btEdit.setEnabled(false);
        bancoDeDados = dataBaseManager.getReadableDatabase();
        Cursor iCursor = bancoDeDados.rawQuery("SELECT * FROM item", null);
        iCursor.moveToFirst();
        ArrayList<String> items = new ArrayList<>();
        for (int i = 1; i <= iCursor.getCount(); i++) {
            String name = iCursor.getString(iCursor.getColumnIndexOrThrow("descricao"));
            String qtd = iCursor.getString(iCursor.getColumnIndexOrThrow("quantidade"));
            items.add(name+" | "+qtd);
            iCursor.moveToNext();
        }
        itemsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, items);
        itemsList.setAdapter(adapter);
        bancoDeDados.close();
    }

    private void setEditItem(String itemName) {
        bancoDeDados = dataBaseManager.getReadableDatabase();
        Cursor iCursor = bancoDeDados.rawQuery("SELECT * FROM item WHERE descricao = '"+itemName+"'", null);
        editedItemName = itemName;
        iCursor.moveToFirst();
        String name = iCursor.getString(iCursor.getColumnIndexOrThrow("descricao"));
        String qtd = iCursor.getString(iCursor.getColumnIndexOrThrow("quantidade"));
        etItem.setText(name);
        etQuantidade.setText(qtd);
        fillList();
        bancoDeDados.close();
    }

    private void deleteFromDB(String itemName){
        bancoDeDados = dataBaseManager.getWritableDatabase();
        bancoDeDados.delete("item","descricao = ?",new String[]{itemName});
        bancoDeDados.close();
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
}
