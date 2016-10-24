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

    void insertSettingsData(String owner, String smartkedex, String language, int pokemonGO) {
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
        private static final int DATABASE_VERSION = 27;

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

            //Populating the DB

            //Populating Type
            /*db.execSQL("INSERT INTO Type VALUES ('Acciaio')");
            db.execSQL("INSERT INTO Type VALUES ('Acqua')");
            db.execSQL("INSERT INTO Type VALUES ('Coleottero')");
            db.execSQL("INSERT INTO Type VALUES ('Drago')");
            db.execSQL("INSERT INTO Type VALUES ('Elettro')");
            db.execSQL("INSERT INTO Type VALUES ('Erba')");
            db.execSQL("INSERT INTO Type VALUES ('Folletto')");
            db.execSQL("INSERT INTO Type VALUES ('Fuoco')");
            db.execSQL("INSERT INTO Type VALUES ('Ghiaccio')");
            db.execSQL("INSERT INTO Type VALUES ('Lotta')");
            db.execSQL("INSERT INTO Type VALUES ('Normale')");
            db.execSQL("INSERT INTO Type VALUES ('Psico')");
            db.execSQL("INSERT INTO Type VALUES ('Roccia')");
            db.execSQL("INSERT INTO Type VALUES ('Spettro')");
            db.execSQL("INSERT INTO Type VALUES ('Terra')");
            db.execSQL("INSERT INTO Type VALUES ('Veleno')");
            db.execSQL("INSERT INTO Type VALUES ('Volante')");

            //Populating Attack
            db.execSQL("INSERT INTO Attack VALUES ('Azione', 12, 1.1, 'Normale')");
            db.execSQL("INSERT INTO Attack VALUES ('Frustata', 7, 0.65, 'Erba')");
            db.execSQL("INSERT INTO Attack VALUES ('Foglielama', 15, 1.45, 'Erba')");
            db.execSQL("INSERT INTO Attack VALUES ('Braciere', 10, 1.05, 'Fuoco')");
            db.execSQL("INSERT INTO Attack VALUES ('Graffio', 6, 0.5, 'Normale')");
            db.execSQL("INSERT INTO Attack VALUES ('Attacco d Ala', 9, 0.75, 'Volante')");
            db.execSQL("INSERT INTO Attack VALUES ('Bolla', 25, 2.3, 'Acqua')");
            db.execSQL("INSERT INTO Attack VALUES ('Pistolacqua', 6, 0.5, 'Acqua')");
            db.execSQL("INSERT INTO Attack VALUES ('Morso', 6, 0.5, 'Buio')");
            db.execSQL("INSERT INTO Attack VALUES ('Coleomorso', 5, 0.45, 'Coleottero')");
            db.execSQL("INSERT INTO Attack VALUES ('Confusione', 15, 1.51, 'Psico')");
            db.execSQL("INSERT INTO Attack VALUES ('Velenospina', 6, 0.57, 'Veleno')");
            db.execSQL("INSERT INTO Attack VALUES ('Velenpuntura', 12, 1.05, 'Veleno')");
            db.execSQL("INSERT INTO Attack VALUES ('Attacco Rapido', 10, 1.33, 'Normale')");
            db.execSQL("INSERT INTO Attack VALUES ('Alacciaio', 15, 1.33, 'Acciaio')");
            db.execSQL("INSERT INTO Attack VALUES ('Beccata', 10, 1.15, 'Volante')");
            db.execSQL("INSERT INTO Attack VALUES ('Acido', 10, 1.05, 'Veleno')");
            db.execSQL("INSERT INTO Attack VALUES ('Tuonoshock', 5, 0.6, 'Elettro')");
            db.execSQL("INSERT INTO Attack VALUES ('Scintilla', 7, 0.7, 'Elettro')");
            db.execSQL("INSERT INTO Attack VALUES ('Colpodifango', 6, 0.55, 'Terra')");
            db.execSQL("INSERT INTO Attack VALUES ('Ferrartigli', 8, 0.63, 'Acciaio')");
            db.execSQL("INSERT INTO Attack VALUES ('Tagliofuria', 3, 0.4, 'Coleottero')");
            db.execSQL("INSERT INTO Attack VALUES ('Cozzata Zen', 12, 1.05, 'Psico')");
            db.execSQL("INSERT INTO Attack VALUES ('Botta', 7, 0.54, 'Normale')");
            db.execSQL("INSERT INTO Attack VALUES ('Finta', 12, 1.04, 'Buio')");
            db.execSQL("INSERT INTO Attack VALUES ('Sbigoattacco', 7, 0.7, 'Buio')");
            db.execSQL("INSERT INTO Attack VALUES ('Colpokarate', 6, 0.8, 'Lotta')");
            db.execSQL("INSERT INTO Attack VALUES ('Colpo Basso', 5, 0.6, 'Lotta')");
            db.execSQL("INSERT INTO Attack VALUES ('Rogodenti', 10, 0.84, 'Fuoco')");
            db.execSQL("INSERT INTO Attack VALUES ('Psicotaglio', 7, 0.57, 'Psico')");
            db.execSQL("INSERT INTO Attack VALUES ('Pugnoscarica', 10, 1.2, 'Acciaio')");
            db.execSQL("INSERT INTO Attack VALUES ('Sassata', 12, 1.36, 'Roccia')");
            db.execSQL("INSERT INTO Attack VALUES ('Taglio', 12, 1.13, 'Normale')");
            db.execSQL("INSERT INTO Attack VALUES ('Geloscheggia', 15, 1.4, 'Ghiaccio')");
            db.execSQL("INSERT INTO Attack VALUES ('Alitogelido', 9, 0.81, 'Ghiaccio')");
            db.execSQL("INSERT INTO Attack VALUES ('Fangosberla', 15, 1.35, 'Terra')");
            db.execSQL("INSERT INTO Attack VALUES ('Leccata', 5, 0.5, 'Spettro')");
            db.execSQL("INSERT INTO Attack VALUES ('Ombrartigli', 11, 0.95, 'Spettro')");
            db.execSQL("INSERT INTO Attack VALUES ('Spaccaroccia', 15, 1.41, 'Lotta')");
            db.execSQL("INSERT INTO Attack VALUES ('Dragospiro', 6, 0.5, 'Drago')");
            db.execSQL("INSERT INTO Attack VALUES ('Splash', 0, 1.23, 'Acqua')");
            db.execSQL("INSERT INTO Attack VALUES ('Trasformazione', 0, 0.54, 'Normale')");

            //Populating Ulti
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Semebomba', 40, 2.4, 5, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Fangobomba', 55, 2.6, 5, 'Veleno')");
            db.execSQL("INSERT INTO Ulti VALUES ('Solarraggio', 120, 4.9, 5, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Petalodanza', 65, 3.2, 5, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Lanciafiamme', 55, 2.9, 5, 'Fuoco')");
            db.execSQL("INSERT INTO Ulti VALUES ('Pirolancio', 30, 2.1, 5, 'Fuoco')");
            db.execSQL("INSERT INTO Ulti VALUES ('Nitrocarica', 25, 3.1, 5, 'Fuoco')");
            db.execSQL("INSERT INTO Ulti VALUES ('Fuocopugno', 40, 2.8, 5, 'Fuoco')");
            db.execSQL("INSERT INTO Ulti VALUES ('Fuocobomba', 100, 4.1, 5, 'Fuoco')");
            db.execSQL("INSERT INTO Ulti VALUES ('Dragartigli', 35, 1.5, 25, 'Drago')");
            db.execSQL("INSERT INTO Ulti VALUES ('Idrondata', 45, 2.35, 5, 'Acqua')");
            db.execSQL("INSERT INTO Ulti VALUES ('Idropulsar', 35, 3.3, 5, 'Acqua')");
            db.execSQL("INSERT INTO Ulti VALUES ('Acquagetto', 25, 2.35, 5, 'Acqua')");
            db.execSQL("INSERT INTO Ulti VALUES ('Idropompa', 90, 3.8, 5, 'Acqua')");
            db.execSQL("INSERT INTO Ulti VALUES ('Geloraggio', 65, 3.65, 5, 'Ghiaccio')");
            db.execSQL("INSERT INTO Ulti VALUES ('Cannonflash', 60, 3.9, 5, 'Acciaio')");
            db.execSQL("INSERT INTO Ulti VALUES ('Scontro', 15, 1.69, 0, 'Normale')");
            db.execSQL("INSERT INTO Ulti VALUES ('Ronzio', 75, 4.25, 5, 'Coleottero')");
            db.execSQL("INSERT INTO Ulti VALUES ('Segnoraggio', 45, 3.1, 5, 'Coleottero')");
            db.execSQL("INSERT INTO Ulti VALUES ('Psichico', 55, 2.8, 5, 'Psico')");
            db.execSQL("INSERT INTO Ulti VALUES ('Forbice X', 35, 2.1, 5, 'Coleottero')");
            db.execSQL("INSERT INTO Ulti VALUES ('Aeroassalto', 30, 2.9, 5, 'Volante')");
            db.execSQL("INSERT INTO Ulti VALUES ('Tornado', 25, 2.7, 5, 'Drago')");
            db.execSQL("INSERT INTO Ulti VALUES ('Aerasoio', 30, 3.3, 25, 'Volante')");
            db.execSQL("INSERT INTO Ulti VALUES ('Tifone', 80, 3.2, 5, 'Tifone')");
            db.execSQL("INSERT INTO Ulti VALUES ('Fossa', 70, 5.8, 5, 'Terra')");
            db.execSQL("INSERT INTO Ulti VALUES ('Corposcontro', 40, 1.56, 5, 'Normale')");
            db.execSQL("INSERT INTO Ulti VALUES ('Iperzanna', 35, 2.1, 5, 'Normale')");
            db.execSQL("INSERT INTO Ulti VALUES ('Iperraggio', 120, 5.0, 5, 'Normale')");
            db.execSQL("INSERT INTO Ulti VALUES ('Perforbecco', 40, 2.7, 5, 'Volante')");
            db.execSQL("INSERT INTO Ulti VALUES ('Giravvita', 50, 3.4, 25, 'Terra')");
            db.execSQL("INSERT INTO Ulti VALUES ('Sporcolancio', 65, 3.0, 5, 'Veleno')");
            db.execSQL("INSERT INTO Ulti VALUES ('Avvolgibotta', 25, 4.0, 5, 'Normale')");
            db.execSQL("INSERT INTO Ulti VALUES ('Fangonda', 70, 3.4, 5, 'Veleno')");
            db.execSQL("INSERT INTO Ulti VALUES ('Tuono', 100, 4.30, 5, 'Elettro')");
            db.execSQL("INSERT INTO Ulti VALUES ('Fulmine', 55, 2.7, 5, 'Elettro')");
            db.execSQL("INSERT INTO Ulti VALUES ('Scarica', 35, 2.5, 5, 'Elettro')");
            db.execSQL("INSERT INTO Ulti VALUES ('Tuonopugno', 40, 2.4, 5, 'Elettro')");
            db.execSQL("INSERT INTO Ulti VALUES ('Breccia', 30, 1.6, 25, 'Lotta')");
            db.execSQL("INSERT INTO Ulti VALUES ('Frana', 50, 3.2, 5, 'Roccia')");
            db.execSQL("INSERT INTO Ulti VALUES ('Rocciotomba', 30, 3.4, 25, 'Roccia')");
            db.execSQL("INSERT INTO Ulti VALUES ('Terremoto', 100, 4.2, 5, 'Terra')");
            db.execSQL("INSERT INTO Ulti VALUES ('Battiterra', 35, 3.4, 5, 'Terra')");
            db.execSQL("INSERT INTO Ulti VALUES ('Velenodenti', 25, 2.4, 5, 'Veleno')");
            db.execSQL("INSERT INTO Ulti VALUES ('Pietrataglio', 80, 3.1, 50, 'Roccia')");
            db.execSQL("INSERT INTO Ulti VALUES ('Incornata', 25, 2.2, 5, 'Normale')");
            db.execSQL("INSERT INTO Ulti VALUES ('Megacorno', 80, 3.2, 5, 'Coleottero')");
            db.execSQL("INSERT INTO Ulti VALUES ('Forza Lunare', 85, 4.1, 5, 'Folletto')");
            db.execSQL("INSERT INTO Ulti VALUES ('Incantavoce', 25, 3.9, 5, 'Folletto')");
            db.execSQL("INSERT INTO Ulti VALUES ('Magibrillio', 55, 4.2, 5, 'Folletto')");
            db.execSQL("INSERT INTO Ulti VALUES ('Ondacalda', 80, 3.8, 5, 'Fuoco')");
            db.execSQL("INSERT INTO Ulti VALUES ('Carineria', 55, 4.2, 5, 'Folletto')");
            db.execSQL("INSERT INTO Ulti VALUES ('Funestovento', 30, 3.1, 5, 'Spettro')");
            db.execSQL("INSERT INTO Ulti VALUES ('Velenocroce', 25, 1.5, 25, 'Veleno')");
            db.execSQL("INSERT INTO Ulti VALUES ('Psicoraggio', 40, 3.8, 5, 'Psico')");
            db.execSQL("INSERT INTO Ulti VALUES ('Pantanobomba', 30, 2.6, 5, 'Terra')");
            db.execSQL("INSERT INTO Ulti VALUES ('Neropulsar', 45, 3.5, 5, 'Buio')");
            db.execSQL("INSERT INTO Ulti VALUES ('Nottesferza', 30, 2.7, 25, 'Buio')");
            db.execSQL("INSERT INTO Ulti VALUES ('Gemmoforza', 40, 2.9, 5, 'Roccia')");
            db.execSQL("INSERT INTO Ulti VALUES ('Incrocolpo', 60, 2.0, 25, 'Lotta')");
            db.execSQL("INSERT INTO Ulti VALUES ('Calciobasso', 30, 2.25, 5, 'Lotta')");
            db.execSQL("INSERT INTO Ulti VALUES ('Ruotafuoco', 40, 4.6, 5, 'Fuoco')");
            db.execSQL("INSERT INTO Ulti VALUES ('Bollaraggio', 30, 2.9, 5, 'Acqua')");
            db.execSQL("INSERT INTO Ulti VALUES ('Idrovampata', 55, 4.0, 5, 'Acqua')");
            db.execSQL("INSERT INTO Ulti VALUES ('Gelopugno', 45, 3.5, 5, 'Ghiaccio')");
            db.execSQL("INSERT INTO Ulti VALUES ('Sottomissione', 30, 2.1, 5, 'Lotta')");
            db.execSQL("INSERT INTO Ulti VALUES ('Palla Ombra', 45, 3.08, 5, 'Spettro')");
            db.execSQL("INSERT INTO Ulti VALUES ('Psicoshock', 40, 2.7, 5, 'Psico')");
            db.execSQL("INSERT INTO Ulti VALUES ('Fendifoglia', 55, 2.8, 25, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Bora', 100, 3.9, 5, 'Ghiaccio')");
            db.execSQL("INSERT INTO Ulti VALUES ('Forzantica', 35, 3.6, 5, 'Roccia')");
            db.execSQL("INSERT INTO Ulti VALUES ('Bombagnete', 30, 2.8, 5, 'Acciaio')");
            db.execSQL("INSERT INTO Ulti VALUES ('Comete', 30, 3.0, 5, 'Normale')");
            db.execSQL("INSERT INTO Ulti VALUES ('Ventogelato', 25, 3.8, 5, 'Ghiaccio')");
            db.execSQL("INSERT INTO Ulti VALUES ('Fango', 30, 2.6, 5, 'Veleno')");
            db.execSQL("INSERT INTO Ulti VALUES ('Metaltestata', 30, 2.0, 5, 'Acciaio')");
            db.execSQL("INSERT INTO Ulti VALUES ('Presa', 25, 2.1, 5, 'Normale')");
            db.execSQL("INSERT INTO Ulti VALUES ('Ossoclava', 25, 1.6, 5, 'Terra')");*/

            //Cubone Incluso
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");
            db.execSQL("INSERT INTO Ulti VALUES ('Vigorcolpo', 70, 2.8, 12, 'Erba')");

            Toast.makeText(context, "onCreate called", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_SHORT).show();
        }
    }
}