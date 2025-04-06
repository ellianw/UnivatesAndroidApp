package com.ellianw.univates;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class DataInsertActivity extends AppCompatActivity {
    private EditText etItem = null;
    private EditText etQuantity = null;
    private DatabaseManager dataBaseManager = null;
    private SQLiteDatabase database = null;
    private String editedItemName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_insert);

        Toolbar appBar = findViewById(R.id.toolbar);
        setSupportActionBar(appBar);

        dataBaseManager = new DatabaseManager(this, "aplicacaodb", 1);
        database = dataBaseManager.getWritableDatabase();

        etItem = findViewById(R.id.name);
        etQuantity = findViewById(R.id.quantity);

        if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{POST_NOTIFICATIONS}, 1);
        }

        Button btBotaoEnviar = findViewById(R.id.bt_send);
        btBotaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues valores = new ContentValues();
                valores.put("descricao", etItem.getText().toString());
                valores.put("quantidade", etQuantity.getText().toString());

                int qtdInt = Integer.parseInt(etQuantity.getText().toString());

                if (editedItemName != null) {
                    deleteFromDB(editedItemName);
                }
                database = dataBaseManager.getWritableDatabase();
                long resultado = database.insert("item", null, valores);
                database.close();

                if (resultado == -1)
                    Toast.makeText(DataInsertActivity.this, "Não foi possível inserir este item", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(DataInsertActivity.this, "Item inserido: " + etItem.getText() + " n: " + etQuantity.getText(), Toast.LENGTH_SHORT).show();
                }
                if (qtdInt % 10 == 0 && editedItemName == null) {
                    notificationExecutor();
                }
                clearForm(findViewById(R.id.root));
                finish();
                editedItemName = null;
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            editedItemName = extras.getString("edited_item");
            setEditItem(editedItemName);
        }
    }

    private void setEditItem(String itemName) {
        database = dataBaseManager.getReadableDatabase();
        Cursor iCursor = database.rawQuery("SELECT * FROM item WHERE descricao = '"+itemName+"'", null);
        editedItemName = itemName;
        iCursor.moveToFirst();
        String name = iCursor.getString(iCursor.getColumnIndexOrThrow("descricao"));
        String qtd = iCursor.getString(iCursor.getColumnIndexOrThrow("quantidade"));
        etItem.setText(name);
        etQuantity.setText(qtd);
        database.close();
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

    protected void deleteFromDB(String itemName){
        database = dataBaseManager.getWritableDatabase();
        database.delete("item","descricao = ?",new String[]{itemName});
        database.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_list,menu);
        menu.findItem(R.id.insert_menu).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.list_menu) {// User chooses the "Settings" item. Show the app settings UI.
            finish();
            return true;
        }
        return false;
    }

    private void notificationExecutor(){
        Data data = new Data.Builder()
                .putString("name",etItem.getText().toString())
                .putString("qtt", etQuantity.getText().toString())
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(CustomNotification.class)
                .setInitialDelay(3, TimeUnit.SECONDS)
                .setInputData(data)
                .build();

        WorkManager.getInstance(this).enqueue(workRequest);
    }
}
