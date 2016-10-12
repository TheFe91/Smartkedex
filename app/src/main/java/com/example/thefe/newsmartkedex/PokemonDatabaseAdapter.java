package com.example.thefe.newsmartkedex;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by TheFe on 11/10/2016.
 */

public class PokemonDatabaseAdapter {

    PokemonHelper helper;

    //Costruttore dell'oggetto PokemonDatabaseAdapter
    public PokemonDatabaseAdapter (Context context) {
        helper = new PokemonHelper(context);
    }

    //Metodi per inserire dati nelle tabelle
    public long insertPokemon (int ID, String nome, String descrizione) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PokemonHelper.ID, ID);
        contentValues.put(PokemonHelper.NAME, nome);
        contentValues.put(PokemonHelper.DESCRIPTION, descrizione);
        long id = db.insert(PokemonHelper.POKEMON, null, contentValues);
        return id;
    }

    public long insertTipo (String tipo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PokemonHelper.TIPO, tipo);
        long id = db.insert(PokemonHelper.TIPO, null, contentValues);
        return id;
    }

    //classe Helper che definisce i metodi onCreate (che crea le tabelle) e onUpgrade (che le modifica strutturalmente)
    static class PokemonHelper extends SQLiteOpenHelper {

        //definizione di tutti i campi del Database come costanti (private static final)
        //Dati per il Database
        private static final String DATABASE_NAME = "pokemondb.db";
        private static final int DATABASE_VERSION = 12;

        //Dati per le Tabelle
        private static final String POKEMON = "Pokemon";
        private static final String TIPO = "Tipo";

        //Dati per i campi delle tabelle
        private static final String ID = "ID";
        private static final String NAME = "Nome";
        private static final String DESCRIPTION = "Descrizione";
        //manca il campo Tipo perchè è uguale al nome della tabella; idem per Fortezza e Debolezza

        private Context context;

        //Costruttore dell'oggetto PokemonHelper
        PokemonHelper(Context context) {
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


}