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

    void insertData(String owner, String smartkedex, String language, int pokemonGO) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO Settings (Owner, SmartkedexName, Language, PokemonGO) VALUES ('"+owner+"', '"+smartkedex+"', '"+language+"', '"+pokemonGO+"')");
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
        String[] columns = {PokemonHelper.SMARTKEDEXNAME};
        Cursor cursor = db.query(PokemonHelper.SETTINGS, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            smartkedex = cursor.getString(0); //va bene 0 perchè io seleziono SEMPRE una colonna alla volta, quindi ha per forza indice 0
        }

        return smartkedex;
    }

    int getPokemonGO () {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {PokemonHelper.POKEMONGO};
        Cursor cursor = db.query(PokemonHelper.SETTINGS, columns, null, null, null, null, null);
        int pokemonGO = 0;

        while (cursor.moveToNext()) {
            pokemonGO = cursor.getInt(0);
        }

        return pokemonGO;
    }

    void updateLanguage (String newLanguage, String oldLanguage) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Settings SET Language = '"+newLanguage+"' WHERE Language = '"+oldLanguage+"';");
    }

    void updateOwner (String newOwner, String oldOwner) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Settings SET Owner = '"+newOwner+"' WHERE Owner = '"+oldOwner+"';");
    }

    void updateSmartkedex (String newSmartkedex, String oldSmartkedex) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Settings SET SmartkedexName = '"+newSmartkedex+"' WHERE SmartkedexName = '"+oldSmartkedex+"';");
    }

    void updatePokemonGO (int newPokemonGO, int oldPokemonGO) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Settings SET PokemonGO = '"+newPokemonGO+"' WHERE PokemonGO = '"+oldPokemonGO+"'");
        System.err.println("getPokemonGO (after query):" + getPokemonGO());
    }

    private static class PokemonHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "PokemonDatabase.db";
        private static final int DATABASE_VERSION = 19;

        //Types Declaration
        private static final String VARCHAR = " VARCHAR(";
        private static final String INT = " INT(";
        private static final String FLOAT = " FLOAT(";

        //Tables Declaration
        private static final String SETTINGS = "Settings";
        private static final String POKEMON = "Pokemon";
        private static final String ATTACK = "Attack";
        private static final String ULTI = "Ulti";
        private static final String TYPE = "Type";
        private static final String CATCHES = "Catches";
        private static final String USER = "User";

        //Columns Declaration
        private static final String LANGUAGE = "Language";
        private static final String OWNER = "Owner";
        private static final String SMARTKEDEXNAME = "SmartkedexName";
        private static final String POKEMONGO = "PokemonGO";
        private static final String ID = "ID";
        private static final String POKEMONNAME = "PokemonName";
        private static final String ULTI_NAME = "UltiName";
        private static final String ATTACK_NAME = "AttackName";
        private static final String DAMAGE_DEALT = "DamageDealt";
        private static final String DURATION = "Duration";
        private static final String CRITICAL_CHANCE = "CriticalChance";
        private static final String TYPE_NAME = "TypeName";
        private static final String QUANTITY = "Quantity";
        private static final String EMAIL = "Email";
        private static final String NAME = "Name";
        private static final String SURNAME = "Surname";
        private static final String USERNAME = "Username";
        private static final String PASSWORD = "Password";
        private static final String CITY = "City";
        private static final String POSTALCODE = "PostalCode";
        private static final String ADDRESS = "Address";
        private static final String PROVINCE = "Province";


        //CREATE TABLE Statements
        private static final String CREATE_SETTINGS = "CREATE TABLE IF NOT EXISTS " + SETTINGS + "(" +
                                                       LANGUAGE + VARCHAR + "3), " +
                                                       OWNER + VARCHAR + "20), " +
                                                       SMARTKEDEXNAME + VARCHAR + "20), " +
                                                       POKEMONGO + INT + "1), " +
                                                       EMAIL + VARCHAR + "40) PRIMARY KEY, " +
                                                       "FOREIGN KEY ("+ EMAIL +") REFERENCES " + USER + " (" + EMAIL + "))";

        private static final String CREATE_POKEMON = "CREATE TABLE IF NOT EXISTS " + POKEMON + "(" +
                                                      POKEMONNAME + VARCHAR + "20), " +
                                                      ID + INT + "3) PRIMARY KEY, " +
                                                      ATTACK_NAME + VARCHAR + "15), " +
                                                      ULTI_NAME + VARCHAR + "15), " +
                                                      "FOREIGN KEY (" + ULTI_NAME + ") REFERENCES " + ULTI + " ("+ ULTI_NAME +"), " +
                                                      "FOREIGN KEY (" + ATTACK_NAME + ") REFERENCES " + ATTACK + "(" + ATTACK_NAME + "))";

        private static final String CREATE_TYPE = "CREATE TABLE IF NOT EXISTS " + TYPE + "(" +
                                                   TYPE_NAME + VARCHAR + "10) PRIMARY KEY)";

        private static final String CREATE_ULTI = "CREATE TABLE IF NOT EXISTS " + ULTI + "(" +
                                                   ULTI_NAME + VARCHAR + "15) PRIMARY KEY, " +
                                                   DAMAGE_DEALT + INT + "3), " +
                                                   DURATION + FLOAT + "3,2), " +
                                                   CRITICAL_CHANCE + INT + "2), " +
                                                   TYPE_NAME + VARCHAR + "10), " +
                                                   "FOREIGN KEY (" + TYPE_NAME + ") REFERENCES " + TYPE + " (" + TYPE_NAME + "))";

        private static final String CREATE_ATTACK = "CREATE TABLE IF NOT EXISTS " + ATTACK + "(" +
                                                     ATTACK_NAME + VARCHAR + "15) PRIMARY KEY, " +
                                                     DAMAGE_DEALT + INT + "3), " +
                                                     DURATION + FLOAT + "3,2), " +
                                                     TYPE_NAME + VARCHAR + "10), " +
                                                     "FOREIGN KEY (" + TYPE_NAME + ") REFERENCES " + TYPE + " (" + TYPE_NAME + "))";

        private static final String CREATE_USER = "CREATE TABLE IF NOT EXISTS " + USER + "(" +
                                                   EMAIL + VARCHAR + "40) PRIMARY KEY, " +
                                                   NAME + VARCHAR + "15), " +
                                                   SURNAME + VARCHAR + "15), " +
                                                   USERNAME + VARCHAR + "15), " +
                                                   PASSWORD + VARCHAR + "16), " +
                                                   ADDRESS + VARCHAR + "50), " +
                                                   POSTALCODE + VARCHAR + "10), " +
                                                   PROVINCE + VARCHAR + "7), " +
                                                   CITY + VARCHAR + "15))";

        private static final String CREATE_CATCHES = "CREATE TABLE IF NOT EXISTS " + CATCHES + "(" +
                                                      QUANTITY + INT + "2), " +
                                                      ID + INT + "3), " +
                                                      EMAIL + VARCHAR + "40), " +
                                                      "PRIMARY KEY (" + EMAIL + ", " + ID + "), " +
                                                      "FOREIGN KEY (" + EMAIL + ") REFERENCES " + USER + " (" + EMAIL + "), " +
                                                      "FOREIGN KEY (" + ID + ") REFERENCES " + POKEMON + " (" + ID + "))";



        private Context context;

        PokemonHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
//            Toast.makeText(context, "Constructor called", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_USER);
            db.execSQL(CREATE_SETTINGS);
            db.execSQL(CREATE_TYPE);
            db.execSQL(CREATE_ATTACK);
            db.execSQL(CREATE_ULTI);
            db.execSQL(CREATE_POKEMON);
            db.execSQL(CREATE_CATCHES);
            Toast.makeText(context, "onCreate called", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_SHORT).show();
        }
    }
}