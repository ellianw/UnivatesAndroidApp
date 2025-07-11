package com.ellianw.univates.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {
    public DatabaseManager(Context context, String databaseName, int version){
        super(context, databaseName, null, version);
    }

//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE item (id INTEGER NOT NULL PRIMARY KEY, descricao TEXT, quantidade INTEGER);");
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE item");
//        onCreate(db);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE receitas (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "descricao TEXT, " +
                "qtde_pessoas_servidas INTEGER," +
                "custo_preparacao DOUBLE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS receitas");
        onCreate(db);
    }

}
