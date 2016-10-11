package com.example.thefe.newsmartkedex;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TheFe on 11/10/2016.
 */

public class PokemonHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pokemondb.db";
    private static final int DATABASE_VERSION = 1;

    PokemonHelper (Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate (SQLiteDatabase db) {

    }

    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}