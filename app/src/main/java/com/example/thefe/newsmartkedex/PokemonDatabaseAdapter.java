package com.example.thefe.newsmartkedex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by TheFe on 19/10/2016.
 */

class PokemonDatabaseAdapter {

    private PokemonHelper helper;

    PokemonDatabaseAdapter(Context context) {
        helper = new PokemonHelper(context);
    }

    long insertLanguage (String language) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PokemonHelper.LANGUAGE, language);

        long id = db.insert(PokemonHelper.SETTINGS, null, contentValues);

        return id;
    }

    long insertOwner(String owner) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PokemonHelper.OWNER, owner);

        long id = db.insert(PokemonHelper.SETTINGS, null, contentValues);

        return id;
    }

    long insertSmartkedex (String smartkedex) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PokemonHelper.SMARTKEDEX, smartkedex);

        long id = db.insert(PokemonHelper.SETTINGS, null, contentValues);

        return id;
    }

    int getRows () {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(PokemonHelper.SETTINGS, null, null, null, null, null, null);
        int rows = 0;
        while (cursor.moveToNext()) {
            rows++;
        }

        return rows;
    }

    public String getLanguage () {
        String language = "";
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {PokemonHelper.LANGUAGE};
        Cursor cursor = db.query(PokemonHelper.SETTINGS, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            language = cursor.getString(0); //va bene 0 perchè io seleziono SEMPRE una colonna alla volta, quindi ha per forza indice 0
        }

        return language;
    }

    String getOwner () {
        String owner = "";
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {PokemonHelper.OWNER};
        Cursor cursor = db.query(PokemonHelper.SETTINGS, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            owner = cursor.getString(0); //va bene 0 perchè io seleziono SEMPRE una colonna alla volta, quindi ha per forza indice 0
        }

        return owner;
    }

    String getSmartkedex () {
        String smartkedex = "";
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {PokemonHelper.SMARTKEDEX};
        Cursor cursor = db.query(PokemonHelper.SETTINGS, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            smartkedex = cursor.getString(0); //va bene 0 perchè io seleziono SEMPRE una colonna alla volta, quindi ha per forza indice 0
        }

        return smartkedex;
    }

    void updateLanguage (String newLanguage, String oldLanguage) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Settings SET Smartkedex = '"+newLanguage+"' WHERE Smartkedex = '"+oldLanguage+"';");
    }

    void updateOwner (String newOwner, String oldOwner) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Settings SET Smartkedex = '"+newOwner+"' WHERE Smartkedex = '"+oldOwner+"';");
    }

    void updateSmartkedex (String newSmartkedex, String oldSmartkedex) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Settings SET Smartkedex = '"+newSmartkedex+"' WHERE Smartkedex = '"+oldSmartkedex+"';");
    }

    private static class PokemonHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "PokemonDatabase.db";
        private static final int DATABASE_VERSION = 12;

        private static final String VARCHAR = " VARCHAR(";

        private static final String SETTINGS = "Settings";
        private static final String LANGUAGE = "Language";
        private static final String OWNER = "Owner";
        private static final String SMARTKEDEX = "Smartkedex";

        private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + SETTINGS + "(" + LANGUAGE + VARCHAR + "3), " + OWNER + VARCHAR + "20), " + SMARTKEDEX + VARCHAR + "20) PRIMARY KEY)";

        private Context context;

        PokemonHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
            System.out.println(CREATE_TABLE);
            Toast.makeText(context, "Constructor called", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
            Toast.makeText(context, "onCreate called", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Settings");
            onCreate(db);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_SHORT).show();
        }
    }
}