package com.ellianw.univates;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    private Button btDelete = null;
    private Button btEdit = null;
    private DatabaseManager dataBaseManager = null;
    private SQLiteDatabase database = null;
    private ListView itemsList = null;
    private int lastSelectedPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar appBar = findViewById(R.id.toolbar);
        setSupportActionBar(appBar);

        dataBaseManager = new DatabaseManager(this, "aplicacaodb", 1);
        database = dataBaseManager.getWritableDatabase();

        itemsList = findViewById(R.id.listview);
        btDelete = findViewById(R.id.bt_delete);
        btEdit = findViewById(R.id.bt_edit);

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
                changeActivity(objString);
            }
        });
    }

    private void fillList(){
        itemsList.setAdapter(null);
        btDelete.setEnabled(false);
        btEdit.setEnabled(false);
        database = dataBaseManager.getReadableDatabase();
        Cursor iCursor = database.rawQuery("SELECT * FROM item", null);
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
        database.close();
    }

    protected void deleteFromDB(String itemName){
        database = dataBaseManager.getWritableDatabase();
        database.delete("item","descricao = ?",new String[]{itemName});
        database.close();
    }

    private void changeActivity(String editItem){
        Intent intencao_de_navegacao = new Intent(NotificationActivity.this, DataInsertActivity.class);
        if (editItem != null) {
            intencao_de_navegacao.putExtra("edited_item",editItem);
        }
        startActivity(intencao_de_navegacao);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_list,menu);
        menu.findItem(R.id.list_menu).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.insert_menu) {
            changeActivity(null);
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        fillList();
        super.onResume();
    }
}
