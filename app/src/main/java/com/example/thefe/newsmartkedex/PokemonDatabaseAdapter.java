package com.example.thefe.newsmartkedex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public long insertFortezze (int pokeID, String fortezza) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PokemonHelper.ID, pokeID);
        contentValues.put(PokemonHelper.TIPO, fortezza);
        long id = db.insert(PokemonHelper.FORTEZZE_POKEMON, null, contentValues);
        return id;
    }

    public long insertDebolezze (int pokeID, String debolezza) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PokemonHelper.ID, pokeID);
        contentValues.put(PokemonHelper.TIPO, debolezza);
        long id = db.insert(PokemonHelper.DEBOLEZZE_POKEMON, null, contentValues);
        return id;
    }

    public long insertTipiPokemon (int pokeID, String tipo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PokemonHelper.ID, pokeID);
        contentValues.put(PokemonHelper.TIPO, tipo);
        long id = db.insert(PokemonHelper.TIPI_POKEMON, null, contentValues);
        return id;
    }

    //Metodi per fare le SELECT

    public String getAllData() {
        SQLiteDatabase db=helper.getWritableDatabase();

        String[] columns = {PokemonHelper.ID, PokemonHelper.NAME, PokemonHelper.DESCRIPTION};
        Cursor cursor = db.query(PokemonHelper.POKEMON, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(PokemonHelper.ID));
            String nome = cursor.getString(cursor.getColumnIndex(PokemonHelper.NAME));
            String descrizione = cursor.getString(cursor.getColumnIndex(PokemonHelper.DESCRIPTION));
            buffer.append(id + " " + nome + " " + descrizione + "\n");
        }
        return buffer.toString();
    }

    public List<String> getTipo(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {PokemonHelper.ID, PokemonHelper.TIPO};
        Cursor cursor = db.query(PokemonHelper.TIPI_POKEMON, columns, PokemonHelper.ID+"='"+id+"'", null, null, null, null);
        List<String> results = new ArrayList<String>();
        while (cursor.moveToNext()) {
           results.add(cursor.getString(cursor.getColumnIndex(PokemonHelper.TIPO))); //cursor.getString() prende la stringa dall'indice indicato; cursor.getColumnIndex() restitusce l'intero corrispondente alla colonna dal nome indicato
        }

        return results;
    }

    //classe Helper che definisce i metodi onCreate (che crea le tabelle) e onUpgrade (che le modifica strutturalmente)
    static class PokemonHelper extends SQLiteOpenHelper {

        //definizione di tutti i campi del Database come costanti (private static final)

        //Dati per il Database
        private static final String DATABASE_NAME = "pokemondb.db";
        private static final int DATABASE_VERSION = 3;

        //Dati per le Tabelle
        private static final String POKEMON = "Pokemon";
        private static final String TIPO = "Tipo";
        private static final String FORTEZZE_POKEMON = "FortezzePokemon";
        private static final String DEBOLEZZE_POKEMON = "DebolezzePokemon";
        private static final String TIPI_POKEMON = "TipiPokemon";

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
                            "ID INT(3)," +
                            "Tipo VARCHAR(10)," +
                            "PRIMARY KEY (ID, Tipo)," +
                            "FOREIGN KEY (ID) REFERENCES Pokemon (ID)," +
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
                            "ID INT(3)," +
                            "Tipo VARCHAR(10)," +
                            "PRIMARY KEY (ID, Fortezza)," +
                            "FOREIGN KEY (ID) REFERENCES Pokemon (ID)," +
                            "FOREIGN KEY (Tipo) REFERENCES Tipo (Tipo)" +
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
                            "ID INT(3)," +
                            "Tipo VARCHAR(10)," +
                            "PRIMARY KEY (ID, Debolezza)," +
                            "FOREIGN KEY (ID) REFERENCES Pokemon (ID)," +
                            "FOREIGN KEY (Tipo) REFERENCES Tipo (Tipo)" +
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
//                String query = "DROP TABLE IF EXISTS Pokemon";
//                db.execSQL(query);
                String query = "DROP TABLE IF EXISTS TipiPokemon";
                db.execSQL(query);
                query = "DROP TABLE IF EXISTS FortezzePokemon";
                db.execSQL(query);
                query = "DROP TABLE IF EXISTS DebolezzePokemon";
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