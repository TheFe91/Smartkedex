package com.example.thefe.newsmartkedex;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TheFe on 11/10/2016.
 */

public class PokemonHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pokemondb";
    private static final int DATABASE_VERSION = 1;

    PokemonHelper (Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate (SQLiteDatabase db) {

        String query =
                "CREATE TABLE IF NOT EXISTS Pokemon (" +
                    "ID INT(3) PRIMARY KEY," +
                    "Nome VARCHAR(15)," +
                    "Descrizione (500)" +
                ");";

        try {
            db.execSQL();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}