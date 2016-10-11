package com.example.thefe.newsmartkedex;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by TheFe on 11/10/2016.
 */

public class PokemonHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pokemondb.db";
    private static final int DATABASE_VERSION = 11;
    private Context context;

    PokemonHelper (Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        Toast.makeText(context, "Constructor called", Toast.LENGTH_LONG).show();
    }

    public void onCreate (SQLiteDatabase db) {

        String query =
                "CREATE TABLE IF NOT EXISTS Pokemon (" +
                    "ID INT(3) PRIMARY KEY," +
                    "Nome VARCHAR(15)," +
                    "Descrizione VARCHAR(500)" +
                ");";

        try {
            db.execSQL(query);
            Toast.makeText(context, "onCreate called - Pokemon", Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e) {
            System.err.println(e);
        }

        query =
                "CREATE TABLE IF NOT EXISTS Tipo (" +
                    "Tipo VARCHAR(10) PRIMARY KEY" +
                ");";

        try {
            db.execSQL(query);
            Toast.makeText(context, "onCreate called - Tipo", Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e) {
            System.err.println(e);
        }

        query =
                "CREATE TABLE IF NOT EXISTS Fortezza (" +
                        "Fortezza VARCHAR(10) PRIMARY KEY" +
                        ");";

        try {
            db.execSQL(query);
            Toast.makeText(context, "onCreate called - Fortezza", Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e) {
            System.err.println(e);
        }

        query =
                "CREATE TABLE IF NOT EXISTS Debolezza (" +
                        "Debolezza VARCHAR(10) PRIMARY KEY" +
                        ");";

        try {
            db.execSQL(query);
            Toast.makeText(context, "onCreate called - Debolezza", Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e) {
            System.err.println(e);
        }

        query =
                "CREATE TABLE IF NOT EXISTS TipiPokemon (" +
                    "Nome VARCHAR(15)," +
                    "Tipo VARCHAR(10)," +
                    "PRIMARY KEY (Nome, Tipo)," +
                    "FOREIGN KEY (Nome) REFERENCES Pokemon (Nome)," +
                    "FOREIGN KEY (Tipo) REFERENCES Tipo (Tipo)" +
                ");";

        try {
            db.execSQL(query);
            Toast.makeText(context, "onCreate called - TipiPokemon", Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e) {
            System.err.println(e);
        }

        query =
                "CREATE TABLE IF NOT EXISTS FortezzePokemon (" +
                    "Nome VARCHAR(15)," +
                    "Fortezza VARCHAR(10)," +
                    "PRIMARY KEY (Nome, Fortezza)," +
                    "FOREIGN KEY (Nome) REFERENCES Pokemon (Nome)," +
                    "FOREIGN KEY (Fortezza) REFERENCES FortezzePokemon (Fortezza)" +
                ");";

        try {
            db.execSQL(query);
            Toast.makeText(context, "onCreate called - FortezzePokemon", Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e) {
            System.err.println(e);
        }

        query =
                "CREATE TABLE IF NOT EXISTS DebolezzePokemon (" +
                    "Nome VARCHAR(15)," +
                    "Debolezza VARCHAR(10)," +
                    "PRIMARY KEY (Nome, Debolezza)," +
                    "FOREIGN KEY (Nome) REFERENCES Pokemon (Nome)," +
                    "FOREIGN KEY (Debolezza) REFERENCES DebolezzePokemon (Debolezza)" +
                ");";

        try {
            db.execSQL(query);
            Toast.makeText(context, "onCreate called - DebolezzePokemon", Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e) {
            System.err.println(e);
        }

    }

    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            String query = "DROP TABLE IF EXISTS Pokemon";
            db.execSQL(query);
            query = "DROP TABLE IF EXISTS Tipo";
            db.execSQL(query);
            query = "DROP TABLE IF EXISTS Fortezza";
            db.execSQL(query);
            query = "DROP TABLE IF EXISTS Debolezza";
            db.execSQL(query);
            onCreate(db);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e) {
            System.err.println(e);
        }
    }
}