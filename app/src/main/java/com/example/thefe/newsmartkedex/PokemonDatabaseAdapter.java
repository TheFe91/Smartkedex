package com.example.thefe.newsmartkedex;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TheFe on 19/10/2016.
 */

public class PokemonDatabaseAdapter {

    private PokemonHelper helper;

    PokemonDatabaseAdapter(Context context) {
        helper = new PokemonHelper(context);
    }

    void clearCopy (String pokeName, Context context) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM Copy WHERE PokemonName = '"+pokeName+"'");
        String[] columns = {PokemonHelper.POKEMONNAME};
        Cursor cursor = db.query(PokemonHelper.COPY, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Toast.makeText(context, cursor.getString(0)+"\n", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    void insertSettingsData(String owner, String smartkedex, String language, int pokemonGO) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO Settings (Owner, SmartkedexName, Language, PokemonGO) VALUES ('"+owner+"', '"+smartkedex+"', '"+language+"', '"+pokemonGO+"')");
        db.close();
    }

    void insertCatches (int pokeID, String owner) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO Catches (ID, Owner) VALUES ("+pokeID+", '"+owner+"')");
        db.close();
    }

    ////////////////////////////////////////////////////////////////////GETTERS////////////////////////////////////////////////////////////////////////////////////

    int getRows (String tableName) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        int rows = 0;
        while (cursor.moveToNext()) {
            rows++;
        }

        db.close();
        return rows;
    }

    List<String> getPokeTypes(int pokeID) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {PokemonHelper.TYPE_NAME};
        Cursor cursor = db.query(PokemonHelper.HASTYPE, columns, PokemonHelper.ID+"="+pokeID, null, null, null, null);
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(PokemonHelper.TYPE_NAME)));
        }
        return list;
    }

    String getAttackTypes(String name, String table) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {PokemonHelper.TYPE_NAME};
        String columnName = "";

        if (table.equals("Ulti"))
            columnName = "UltiName";
        else
            columnName = "AttackName";

        Cursor cursor = db.query(table, columns, columnName+"="+name, null, null, null, null);
        String type = "";
        while (cursor.moveToNext()) {
            type = cursor.getString(cursor.getColumnIndex(PokemonHelper.TYPE_NAME));
        }
        return type;
    }

    int getCatched (int pokeID) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(PokemonHelper.CATCHES, null, PokemonHelper.ID+"="+pokeID, null, null, null, null);
        int rows = 0;
        while (cursor.moveToNext()) {
            rows++;
        }

        db.close();
        return rows;
    }

    int getCopy (int pokeID) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {PokemonHelper.QUANTITY};
        Cursor cursor = db.query(PokemonHelper.CATCHES, columns, PokemonHelper.ID+"="+pokeID, null, null, null, null);
        int quantity = 0;
        while (cursor.moveToNext()) {
            quantity = cursor.getInt(cursor.getColumnIndex(PokemonHelper.QUANTITY));
        }

        db.close();
        return quantity;
    }

    String getPokeAttack (int pokeCopy) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String attack = "";
        String[] columns = {PokemonHelper.ATTACK_NAME};
        Cursor cursor = db.query(PokemonHelper.COPY, columns, PokemonHelper.ID+"="+pokeCopy, null, null, null, null);
        while (cursor.moveToNext()) {
            attack = cursor.getString(cursor.getColumnIndex(PokemonHelper.ATTACK_NAME));
        }

        db.close();
        return attack;
    }

    public String getLanguage () {
        String language = "";
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {PokemonHelper.LANGUAGE};
        Cursor cursor = db.query(PokemonHelper.SETTINGS, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            language = cursor.getString(0); //va bene 0 perchè io seleziono SEMPRE una colonna alla volta, quindi ha per forza indice 0
        }

        db.close();
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

        db.close();
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

        db.close();
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

        db.close();
        return pokemonGO;
    }

    int getNumberOfCopies (int pokeID) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {PokemonHelper.QUANTITY};
        Cursor cursor = db.query(PokemonHelper.CATCHES, columns, PokemonHelper.ID + "=" + pokeID, null, null, null, null);

        int numberOfCopies = 0;
        while (cursor.moveToNext()) {
            numberOfCopies = cursor.getInt(cursor.getColumnIndex(PokemonHelper.QUANTITY));
        }

        db.close();
        return numberOfCopies;
    }

    String[] getAttacks (int pokeID) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {PokemonHelper.ATTACK_NAME};
        Cursor cursor = db.query(PokemonHelper.HASATTACK, columns, PokemonHelper.ID + "=" + pokeID, null, null, null, null);

        String[] attacks = {"", ""};
        int i = 0;

        while (cursor.moveToNext()) {
            attacks[i] = cursor.getString(cursor.getColumnIndex(PokemonHelper.ATTACK_NAME)); //putting the content at the i position of the attacks array, taking the column index of ATTACK_NAME
            i++;
        }

        db.close();
        return attacks;
    }

    ////////////////////////////////////////////////////////////////////UPDATERS////////////////////////////////////////////////////////////////////////////////////

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
    }

    void updateCatches (int pokeID, int numberOfCopies) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Catches SET Quantity = "+numberOfCopies+" WHERE ID = "+pokeID);
    }

    private static class PokemonHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "PokemonDatabase.db";
        private static final int DATABASE_VERSION = 5;

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
        private static final String COPY = "Copy";
        private static final String HASATTACK = "HasAttack";
        private static final String HASULTI = "HasUlti";
        private static final String HASCOPY = "HasCopy";
        private static final String HASTYPE = "HasType";

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
        private static final String IDCOPY = "IDCopy";
        private static final String IDPOKEMON = "IDPokemon";



        //CREATE TABLE Statements
        private static final String CREATE_SETTINGS = "CREATE TABLE IF NOT EXISTS " + SETTINGS + "(" +
                                                       LANGUAGE + VARCHAR + "3), " +
                                                       OWNER + VARCHAR + "20) PRIMARY KEY, " +
                                                       SMARTKEDEXNAME + VARCHAR + "20), " +
                                                       POKEMONGO + INT + "1))";

        private static final String CREATE_POKEMON = "CREATE TABLE IF NOT EXISTS " + POKEMON + "(" +
                                                      POKEMONNAME + VARCHAR + "20), " +
                                                      ID + INT + "3) PRIMARY KEY)";

        private static final String CREATE_TYPE = "CREATE TABLE IF NOT EXISTS " + TYPE + "(" + TYPE_NAME + VARCHAR + "10) PRIMARY KEY)";

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

        private static final String CREATE_CATCHES = "CREATE TABLE IF NOT EXISTS " + CATCHES + "(" +
                                                      QUANTITY + INT + "2), " +
                                                      ID + INT + "3), " +
                                                      OWNER + VARCHAR + "40), " +
                                                      "PRIMARY KEY (" + OWNER + ", " + ID + "), " +
                                                      "FOREIGN KEY (" + OWNER + ") REFERENCES " + SETTINGS + " (" + OWNER + "), " +
                                                      "FOREIGN KEY (" + ID + ") REFERENCES " + POKEMON + " (" + ID + "))";

        private static final String CREATE_HASATTACK = "CREATE TABLE IF NOT EXISTS " + HASATTACK + "(" +
                                                        ID + INT + "3), " +
                                                        ATTACK_NAME + VARCHAR + "15), " +
                                                        "PRIMARY KEY (" + ID + ", " + ATTACK_NAME + "), " +
                                                        "FOREIGN KEY (" + ID + ") REFERENCES " + POKEMON + " (" + ID + "), " +
                                                        "FOREIGN KEY (" + ATTACK_NAME + ") REFERENCES " + ATTACK + " (" + ATTACK_NAME + "))";

        private static final String CREATE_HASULTI = "CREATE TABLE IF NOT EXISTS " + HASULTI + "(" +
                                                     ID + INT + "3), " +
                                                     ULTI_NAME + VARCHAR + "15), " +
                                                     "PRIMARY KEY (" + ID + ", " + ULTI_NAME + "), " +
                                                     "FOREIGN KEY (" + ID + ") REFERENCES " + POKEMON + " (" + ID + "), " +
                                                     "FOREIGN KEY (" + ULTI_NAME + ") REFERENCES " + ULTI + " (" + ULTI_NAME + "))";

        private static final String CREATE_HASTYPE = "CREATE TABLE IF NOT EXISTS " + HASTYPE + "(" +
                                                     ID + INT + "3), " +
                                                     TYPE_NAME + VARCHAR + "10), " +
                                                     "PRIMARY KEY (" + ID + ", " + TYPE_NAME + "), " +
                                                     "FOREIGN KEY (" + ID + ") REFERENCES " + POKEMON + " (" + ID + "), " +
                                                     "FOREIGN KEY (" + TYPE_NAME + ") REFERENCES " + TYPE + " (" + TYPE_NAME + "))";

        private static final String CREATE_COPY = "CREATE TABLE IF NOT EXISTS " + COPY + "(" +
                                                  ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                  ATTACK_NAME + VARCHAR + "15), " +
                                                  ULTI_NAME + VARCHAR + "15), " +
                                                  POKEMONNAME + VARCHAR + "20), " +
                                                  "FOREIGN KEY (" + POKEMONNAME + ") REFERENCES " + POKEMON + " (" + POKEMONNAME + "))";

        private static final String CREATE_HASCOPY = "CREATE TABLE IF NOT EXISTS " + HASCOPY + "(" +
                                                     IDCOPY + INT + "3), " +
                                                     IDPOKEMON + INT + "3), " +
                                                     "PRIMARY KEY (" + IDCOPY + ", " + IDPOKEMON + "), " +
                                                     "FOREIGN KEY (" + IDPOKEMON + ") REFERENCES " + POKEMON + " (" + ID + "), " +
                                                     "FOREIGN KEY (" + IDCOPY + ") REFERENCES " + COPY + " (" + ID + "))";

        private Context context;

        PokemonHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
//            Toast.makeText(context, "Constructor called", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_SETTINGS);
            db.execSQL(CREATE_TYPE);
            db.execSQL(CREATE_ATTACK);
            db.execSQL(CREATE_ULTI);
            db.execSQL(CREATE_POKEMON);
            db.execSQL(CREATE_CATCHES);
            db.execSQL(CREATE_HASATTACK);
            db.execSQL(CREATE_HASULTI);
            db.execSQL(CREATE_COPY);
            db.execSQL(CREATE_HASCOPY);
            db.execSQL(CREATE_HASTYPE);

            //Populating the DB

            //Populating Type
            db.execSQL("INSERT INTO Type VALUES ('Acciaio')");
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
            db.execSQL("INSERT INTO Ulti VALUES ('Ossoclava', 25, 1.6, 5, 'Terra')");
            db.execSQL("INSERT INTO Ulti VALUES ('Pestone', 30, 2.25, 5, 'Normale')");
            db.execSQL("INSERT INTO Ulti VALUES ('Assorbibacio', 25, 2.8, 5, 'Folletto')");
            db.execSQL("INSERT INTO Ulti VALUES ('Acquadisale', 25, 2.4, 5, 'Acqua')");
            db.execSQL("INSERT INTO Ulti VALUES ('Psicobotta', 100, 2.8, 5, 'Psico')");

            //Populating Pokémon
            for (int i = 1; i < 152; i++) {
                PokemonDetails pokemonDetails = new PokemonDetails();
                db.execSQL("INSERT INTO Pokemon VALUES ('" + pokemonDetails.getName(i) + "', " + i + ")");
            }

            //Populatinh HasType
            db.execSQL("INSERT INTO HasType VALUES (1, 'Erba')");
            db.execSQL("INSERT INTO HasType VALUES (1, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (2, 'Erba')");
            db.execSQL("INSERT INTO HasType VALUES (2, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (3, 'Erba')");
            db.execSQL("INSERT INTO HasType VALUES (3, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (4, 'Fuoco')");
            db.execSQL("INSERT INTO HasType VALUES (5, 'Fuoco')");
            db.execSQL("INSERT INTO HasType VALUES (6, 'Fuoco')");
            db.execSQL("INSERT INTO HasType VALUES (6, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (7, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (8, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (9, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (10, 'Coleotter')");
            db.execSQL("INSERT INTO HasType VALUES (11, 'Coleotter')");
            db.execSQL("INSERT INTO HasType VALUES (12, 'Coleotter')");
            db.execSQL("INSERT INTO HasType VALUES (12, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (13, 'Coleottero')");
            db.execSQL("INSERT INTO HasType VALUES (14, 'Coleottero')");
            db.execSQL("INSERT INTO HasType VALUES (15, 'Coleottero')");
            db.execSQL("INSERT INTO HasType VALUES (13, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (14, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (15, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (16, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (17, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (18, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (16, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (17, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (18, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (19, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (20, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (21, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (22, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (21, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (22, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (23, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (24, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (25, 'Elettro')");
            db.execSQL("INSERT INTO HasType VALUES (26, 'Elettro')");
            db.execSQL("INSERT INTO HasType VALUES (27, 'Terra')");
            db.execSQL("INSERT INTO HasType VALUES (28, 'Terra')");
            db.execSQL("INSERT INTO HasType VALUES (29, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (30, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (31, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (31, 'Terra')");
            db.execSQL("INSERT INTO HasType VALUES (32, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (33, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (34, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (34, 'Terra')");
            db.execSQL("INSERT INTO HasType VALUES (35, 'Folletto')");
            db.execSQL("INSERT INTO HasType VALUES (36, 'Folletto')");
            db.execSQL("INSERT INTO HasType VALUES (37, 'Fuoco')");
            db.execSQL("INSERT INTO HasType VALUES (38, 'Fuoco')");
            db.execSQL("INSERT INTO HasType VALUES (39, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (40, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (39, 'Folletto')");
            db.execSQL("INSERT INTO HasType VALUES (40, 'Folletto')");
            db.execSQL("INSERT INTO HasType VALUES (41, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (42, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (41, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (42, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (43, 'Erba')");
            db.execSQL("INSERT INTO HasType VALUES (44, 'Erba')");
            db.execSQL("INSERT INTO HasType VALUES (45, 'Erba')");
            db.execSQL("INSERT INTO HasType VALUES (43, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (44, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (45, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (46, 'Erba')");
            db.execSQL("INSERT INTO HasType VALUES (47, 'Erba')");
            db.execSQL("INSERT INTO HasType VALUES (46, 'Coleottero')");
            db.execSQL("INSERT INTO HasType VALUES (47, 'Coleottero')");
            db.execSQL("INSERT INTO HasType VALUES (48, 'Coleottero')");
            db.execSQL("INSERT INTO HasType VALUES (49, 'Coleottero')");
            db.execSQL("INSERT INTO HasType VALUES (48, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (49, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (50, 'Terra')");
            db.execSQL("INSERT INTO HasType VALUES (51, 'Terra')");
            db.execSQL("INSERT INTO HasType VALUES (52, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (53, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (54, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (55, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (56, 'Lotta')");
            db.execSQL("INSERT INTO HasType VALUES (57, 'Lotta')");
            db.execSQL("INSERT INTO HasType VALUES (58, 'Fuoco')");
            db.execSQL("INSERT INTO HasType VALUES (59, 'Fuoco')");
            db.execSQL("INSERT INTO HasType VALUES (60, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (61, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (62, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (63, 'Psico')");
            db.execSQL("INSERT INTO HasType VALUES (64, 'Psico')");
            db.execSQL("INSERT INTO HasType VALUES (65, 'Psico')");
            db.execSQL("INSERT INTO HasType VALUES (66, 'Lotta')");
            db.execSQL("INSERT INTO HasType VALUES (67, 'Lotta')");
            db.execSQL("INSERT INTO HasType VALUES (68, 'Lotta')");
            db.execSQL("INSERT INTO HasType VALUES (69, 'Erba')");
            db.execSQL("INSERT INTO HasType VALUES (70, 'Erba')");
            db.execSQL("INSERT INTO HasType VALUES (71, 'Erba')");
            db.execSQL("INSERT INTO HasType VALUES (69, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (70, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (71, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (72, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (73, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (72, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (73, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (74, 'Roccia')");
            db.execSQL("INSERT INTO HasType VALUES (75, 'Roccia')");
            db.execSQL("INSERT INTO HasType VALUES (76, 'Roccia')");
            db.execSQL("INSERT INTO HasType VALUES (74, 'Terra')");
            db.execSQL("INSERT INTO HasType VALUES (75, 'Terra')");
            db.execSQL("INSERT INTO HasType VALUES (76, 'Terra')");
            db.execSQL("INSERT INTO HasType VALUES (77, 'Fuoco')");
            db.execSQL("INSERT INTO HasType VALUES (78, 'Fuoco')");
            db.execSQL("INSERT INTO HasType VALUES (79, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (80, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (79, 'Psico')");
            db.execSQL("INSERT INTO HasType VALUES (80, 'Psico')");
            db.execSQL("INSERT INTO HasType VALUES (81, 'Elettro')");
            db.execSQL("INSERT INTO HasType VALUES (82, 'Elettro')");
            db.execSQL("INSERT INTO HasType VALUES (81, 'Acciaio')");
            db.execSQL("INSERT INTO HasType VALUES (82, 'Acciaio')");
            db.execSQL("INSERT INTO HasType VALUES (83, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (84, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (85, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (83, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (84, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (85, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (86, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (87, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (87, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasType VALUES (88, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (89, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (90, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (91, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (91, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasType VALUES (92, 'Spettro')");
            db.execSQL("INSERT INTO HasType VALUES (93, 'Spettro')");
            db.execSQL("INSERT INTO HasType VALUES (94, 'Spettro')");
            db.execSQL("INSERT INTO HasType VALUES (92, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (93, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (94, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (95, 'Roccia')");
            db.execSQL("INSERT INTO HasType VALUES (95, 'Terra')");
            db.execSQL("INSERT INTO HasType VALUES (96, 'Psico')");
            db.execSQL("INSERT INTO HasType VALUES (97, 'Psico')");
            db.execSQL("INSERT INTO HasType VALUES (98, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (99, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (100, 'Elettro')");
            db.execSQL("INSERT INTO HasType VALUES (101, 'Elettro')");
            db.execSQL("INSERT INTO HasType VALUES (102, 'Erba')");
            db.execSQL("INSERT INTO HasType VALUES (103, 'Erba')");
            db.execSQL("INSERT INTO HasType VALUES (102, 'Psico')");
            db.execSQL("INSERT INTO HasType VALUES (103, 'Psico')");
            db.execSQL("INSERT INTO HasType VALUES (104, 'Terra')");
            db.execSQL("INSERT INTO HasType VALUES (105, 'Terra')");
            db.execSQL("INSERT INTO HasType VALUES (106, 'Lotta')");
            db.execSQL("INSERT INTO HasType VALUES (107, 'Lotta')");
            db.execSQL("INSERT INTO HasType VALUES (108, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (109, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (110, 'Veleno')");
            db.execSQL("INSERT INTO HasType VALUES (111, 'Terra')");
            db.execSQL("INSERT INTO HasType VALUES (112, 'Terra')");
            db.execSQL("INSERT INTO HasType VALUES (111, 'Roccia')");
            db.execSQL("INSERT INTO HasType VALUES (112, 'Roccia')");
            db.execSQL("INSERT INTO HasType VALUES (113, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (114, 'Erba')");
            db.execSQL("INSERT INTO HasType VALUES (115, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (116, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (117, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (118, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (119, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (120, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (121, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (121, 'Psico')");
            db.execSQL("INSERT INTO HasType VALUES (122, 'Psico')");
            db.execSQL("INSERT INTO HasType VALUES (123, 'Coleottero')");
            db.execSQL("INSERT INTO HasType VALUES (123, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (124, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasType VALUES (124, 'Psico')");
            db.execSQL("INSERT INTO HasType VALUES (125, 'Elettro')");
            db.execSQL("INSERT INTO HasType VALUES (126, 'Fuoco')");
            db.execSQL("INSERT INTO HasType VALUES (127, 'Coleottero')");
            db.execSQL("INSERT INTO HasType VALUES (128, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (129, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (130, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (130, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (131, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (131, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasType VALUES (132, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (133, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (134, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (135, 'Elettro')");
            db.execSQL("INSERT INTO HasType VALUES (136, 'Fuoco')");
            db.execSQL("INSERT INTO HasType VALUES (137, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (138, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (139, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (138, 'Roccia')");
            db.execSQL("INSERT INTO HasType VALUES (139, 'Roccia')");
            db.execSQL("INSERT INTO HasType VALUES (140, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (141, 'Acqua')");
            db.execSQL("INSERT INTO HasType VALUES (140, 'Roccia')");
            db.execSQL("INSERT INTO HasType VALUES (141, 'Roccia')");
            db.execSQL("INSERT INTO HasType VALUES (142, 'Roccia')");
            db.execSQL("INSERT INTO HasType VALUES (142, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (143, 'Normale')");
            db.execSQL("INSERT INTO HasType VALUES (144, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasType VALUES (145, 'Elettro')");
            db.execSQL("INSERT INTO HasType VALUES (146, 'Fuoco')");
            db.execSQL("INSERT INTO HasType VALUES (144, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (145, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (146, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (147, 'Drago')");
            db.execSQL("INSERT INTO HasType VALUES (148, 'Drago')");
            db.execSQL("INSERT INTO HasType VALUES (149, 'Drago')");
            db.execSQL("INSERT INTO HasType VALUES (149, 'Volante')");
            db.execSQL("INSERT INTO HasType VALUES (150, 'Psico')");
            db.execSQL("INSERT INTO HasType VALUES (151, 'Psico')");

            //Populating HasAttack
            db.execSQL("INSERT INTO HasAttack VALUES (1, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (1, 'Frustata')");
            db.execSQL("INSERT INTO HasAttack VALUES (2, 'Foglielama')");
            db.execSQL("INSERT INTO HasAttack VALUES (2, 'Frustata')");
            db.execSQL("INSERT INTO HasAttack VALUES (3, 'Foglielama')");
            db.execSQL("INSERT INTO HasAttack VALUES (3, 'Frustata')");
            db.execSQL("INSERT INTO HasAttack VALUES (4, 'Braciere')");
            db.execSQL("INSERT INTO HasAttack VALUES (4, 'Graffio')");
            db.execSQL("INSERT INTO HasAttack VALUES (5, 'Braciere')");
            db.execSQL("INSERT INTO HasAttack VALUES (5, 'Graffio')");
            db.execSQL("INSERT INTO HasAttack VALUES (6, 'Braciere')");
            db.execSQL("INSERT INTO HasAttack VALUES (6, 'Attacco d Ala')");
            db.execSQL("INSERT INTO HasAttack VALUES (7, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (7, 'Bolla')");
            db.execSQL("INSERT INTO HasAttack VALUES (8, 'Morso')");
            db.execSQL("INSERT INTO HasAttack VALUES (8, 'Pistolacqua')");
            db.execSQL("INSERT INTO HasAttack VALUES (9, 'Morso')");
            db.execSQL("INSERT INTO HasAttack VALUES (9, 'Pistolacqua')");
            db.execSQL("INSERT INTO HasAttack VALUES (10, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (10, 'Coleomorso')");
            db.execSQL("INSERT INTO HasAttack VALUES (11, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (11, 'Coleomorso')");
            db.execSQL("INSERT INTO HasAttack VALUES (12, 'Confusione')");
            db.execSQL("INSERT INTO HasAttack VALUES (12, 'Coleomorso')");
            db.execSQL("INSERT INTO HasAttack VALUES (13, 'Velenospina')");
            db.execSQL("INSERT INTO HasAttack VALUES (13, 'Coleomorso')");
            db.execSQL("INSERT INTO HasAttack VALUES (14, 'Velenospina')");
            db.execSQL("INSERT INTO HasAttack VALUES (14, 'Coleomorso')");
            db.execSQL("INSERT INTO HasAttack VALUES (15, 'Velenpuntura')");
            db.execSQL("INSERT INTO HasAttack VALUES (15, 'Coleomorso')");
            db.execSQL("INSERT INTO HasAttack VALUES (16, 'Attacco Rapido')");
            db.execSQL("INSERT INTO HasAttack VALUES (16, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (17, 'Alacciaio')");
            db.execSQL("INSERT INTO HasAttack VALUES (17, 'Attacco d Ala')");
            db.execSQL("INSERT INTO HasAttack VALUES (18, 'Alacciaio')");
            db.execSQL("INSERT INTO HasAttack VALUES (18, 'Attacco d Ala')");
            db.execSQL("INSERT INTO HasAttack VALUES (19, 'Attacco Rapido')");
            db.execSQL("INSERT INTO HasAttack VALUES (19, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (20, 'Attacco Rapido')");
            db.execSQL("INSERT INTO HasAttack VALUES (20, 'Morso')");
            db.execSQL("INSERT INTO HasAttack VALUES (21, 'Attacco Rapido')");
            db.execSQL("INSERT INTO HasAttack VALUES (21, 'Beccata')");
            db.execSQL("INSERT INTO HasAttack VALUES (22, 'Alacciaio')");
            db.execSQL("INSERT INTO HasAttack VALUES (22, 'Beccata')");
            db.execSQL("INSERT INTO HasAttack VALUES (23, 'Acido')");
            db.execSQL("INSERT INTO HasAttack VALUES (23, 'Velenospina')");
            db.execSQL("INSERT INTO HasAttack VALUES (24, 'Acido')");
            db.execSQL("INSERT INTO HasAttack VALUES (24, 'Morso')");
            db.execSQL("INSERT INTO HasAttack VALUES (25, 'Attacco Rapido')");
            db.execSQL("INSERT INTO HasAttack VALUES (25, 'Tuonoshock')");
            db.execSQL("INSERT INTO HasAttack VALUES (26, 'Scintilla')");
            db.execSQL("INSERT INTO HasAttack VALUES (26, 'Tuonoshock')");
            db.execSQL("INSERT INTO HasAttack VALUES (27, 'Graffio')");
            db.execSQL("INSERT INTO HasAttack VALUES (27, 'Colpodifango')");
            db.execSQL("INSERT INTO HasAttack VALUES (28, 'Ferrartigli')");
            db.execSQL("INSERT INTO HasAttack VALUES (28, 'Colpodifango')");
            db.execSQL("INSERT INTO HasAttack VALUES (29, 'Velenospina')");
            db.execSQL("INSERT INTO HasAttack VALUES (29, 'Morso')");
            db.execSQL("INSERT INTO HasAttack VALUES (30, 'Velenospina')");
            db.execSQL("INSERT INTO HasAttack VALUES (30, 'Morso')");
            db.execSQL("INSERT INTO HasAttack VALUES (31, 'Velenpuntura')");
            db.execSQL("INSERT INTO HasAttack VALUES (31, 'Morso')");
            db.execSQL("INSERT INTO HasAttack VALUES (32, 'Beccata')");
            db.execSQL("INSERT INTO HasAttack VALUES (32, 'Velenospina')");
            db.execSQL("INSERT INTO HasAttack VALUES (33, 'Velenpuntura')");
            db.execSQL("INSERT INTO HasAttack VALUES (33, 'Velenospina')");
            db.execSQL("INSERT INTO HasAttack VALUES (34, 'Velenpuntura')");
            db.execSQL("INSERT INTO HasAttack VALUES (34, 'Tagliofuria')");
            db.execSQL("INSERT INTO HasAttack VALUES (35, 'Cozzata Zen')");
            db.execSQL("INSERT INTO HasAttack VALUES (35, 'Botta')");
            db.execSQL("INSERT INTO HasAttack VALUES (36, 'Cozzata Zen')");
            db.execSQL("INSERT INTO HasAttack VALUES (36, 'Botta')");
            db.execSQL("INSERT INTO HasAttack VALUES (37, 'Braciere')");
            db.execSQL("INSERT INTO HasAttack VALUES (37, 'Attazzo Rapido')");
            db.execSQL("INSERT INTO HasAttack VALUES (38, 'Finta')");
            db.execSQL("INSERT INTO HasAttack VALUES (38, 'Braciere')");
            db.execSQL("INSERT INTO HasAttack VALUES (39, 'Finta')");
            db.execSQL("INSERT INTO HasAttack VALUES (39, 'Botta')");
            db.execSQL("INSERT INTO HasAttack VALUES (40, 'Finta')");
            db.execSQL("INSERT INTO HasAttack VALUES (40, 'Botta')");
            db.execSQL("INSERT INTO HasAttack VALUES (41, 'Attacco Rapido')");
            db.execSQL("INSERT INTO HasAttack VALUES (41, 'Morso')");
            db.execSQL("INSERT INTO HasAttack VALUES (42, 'Attacco d Ala')");
            db.execSQL("INSERT INTO HasAttack VALUES (42, 'Morso')");
            db.execSQL("INSERT INTO HasAttack VALUES (43, 'Foglielama')");
            db.execSQL("INSERT INTO HasAttack VALUES (43, 'Acido')");
            db.execSQL("INSERT INTO HasAttack VALUES (44, 'Foglielama')");
            db.execSQL("INSERT INTO HasAttack VALUES (44, 'Acido')");
            db.execSQL("INSERT INTO HasAttack VALUES (45, 'Foglielama')");
            db.execSQL("INSERT INTO HasAttack VALUES (45, 'Acido')");
            db.execSQL("INSERT INTO HasAttack VALUES (46, 'Graffio')");
            db.execSQL("INSERT INTO HasAttack VALUES (46, 'Coleomorso')");
            db.execSQL("INSERT INTO HasAttack VALUES (47, 'Coleomorso')");
            db.execSQL("INSERT INTO HasAttack VALUES (47, 'Tagliofuria')");
            db.execSQL("INSERT INTO HasAttack VALUES (48, 'Confusione')");
            db.execSQL("INSERT INTO HasAttack VALUES (48, 'Coleomorso')");
            db.execSQL("INSERT INTO HasAttack VALUES (49, 'Confusione')");
            db.execSQL("INSERT INTO HasAttack VALUES (49, 'Coleomorso')");
            db.execSQL("INSERT INTO HasAttack VALUES (50, 'Graffio')");
            db.execSQL("INSERT INTO HasAttack VALUES (50, 'Colpodifango')");
            db.execSQL("INSERT INTO HasAttack VALUES (51, 'Sbigoattacco')");
            db.execSQL("INSERT INTO HasAttack VALUES (51, 'Colpodifango')");
            db.execSQL("INSERT INTO HasAttack VALUES (52, 'Graffio')");
            db.execSQL("INSERT INTO HasAttack VALUES (52, 'Morso')");
            db.execSQL("INSERT INTO HasAttack VALUES (53, 'Finta')");
            db.execSQL("INSERT INTO HasAttack VALUES (53, 'Graffio')");
            db.execSQL("INSERT INTO HasAttack VALUES (54, 'Cozzata Zen')");
            db.execSQL("INSERT INTO HasAttack VALUES (54, 'Pistolacqua')");
            db.execSQL("INSERT INTO HasAttack VALUES (55, 'Confusione')");
            db.execSQL("INSERT INTO HasAttack VALUES (55, 'Pistolacqua')");
            db.execSQL("INSERT INTO HasAttack VALUES (56, 'Colpokarate')");
            db.execSQL("INSERT INTO HasAttack VALUES (56, 'Graffio')");
            db.execSQL("INSERT INTO HasAttack VALUES (57, 'Colpokarate')");
            db.execSQL("INSERT INTO HasAttack VALUES (57, 'Colpo Basso')");
            db.execSQL("INSERT INTO HasAttack VALUES (58, 'Braciere')");
            db.execSQL("INSERT INTO HasAttack VALUES (58, 'Morso')");
            db.execSQL("INSERT INTO HasAttack VALUES (59, 'Rogodenti')");
            db.execSQL("INSERT INTO HasAttack VALUES (59, 'Morso')");
            db.execSQL("INSERT INTO HasAttack VALUES (60, 'Bolla')");
            db.execSQL("INSERT INTO HasAttack VALUES (60, 'Colpodifango')");
            db.execSQL("INSERT INTO HasAttack VALUES (61, 'Bolla')");
            db.execSQL("INSERT INTO HasAttack VALUES (61, 'Colpodifango')");
            db.execSQL("INSERT INTO HasAttack VALUES (62, 'Bolla')");
            db.execSQL("INSERT INTO HasAttack VALUES (62, 'Colpodifango')");
            db.execSQL("INSERT INTO HasAttack VALUES (63, 'Cozzata Zen')");
            db.execSQL("INSERT INTO HasAttack VALUES (64, 'Confusione')");
            db.execSQL("INSERT INTO HasAttack VALUES (64, 'Psicotaglio')");
            db.execSQL("INSERT INTO HasAttack VALUES (65, 'Confusione')");
            db.execSQL("INSERT INTO HasAttack VALUES (65, 'Psicotaglio')");
            db.execSQL("INSERT INTO HasAttack VALUES (66, 'Colpokarate')");
            db.execSQL("INSERT INTO HasAttack VALUES (66, 'Colpo Basso')");
            db.execSQL("INSERT INTO HasAttack VALUES (67, 'Colpokarate')");
            db.execSQL("INSERT INTO HasAttack VALUES (67, 'Colpo Basso')");
            db.execSQL("INSERT INTO HasAttack VALUES (68, 'Pugnoscarica')");
            db.execSQL("INSERT INTO HasAttack VALUES (68, 'Colpokarate')");
            db.execSQL("INSERT INTO HasAttack VALUES (69, 'Acido')");
            db.execSQL("INSERT INTO HasAttack VALUES (69, 'Frustata')");
            db.execSQL("INSERT INTO HasAttack VALUES (70, 'Foglielama')");
            db.execSQL("INSERT INTO HasAttack VALUES (70, 'Acido')");
            db.execSQL("INSERT INTO HasAttack VALUES (71, 'Foglielama')");
            db.execSQL("INSERT INTO HasAttack VALUES (71, 'Acido')");
            db.execSQL("INSERT INTO HasAttack VALUES (72, 'Bolla')");
            db.execSQL("INSERT INTO HasAttack VALUES (72, 'Velenospina')");
            db.execSQL("INSERT INTO HasAttack VALUES (73, 'Velenpuntura')");
            db.execSQL("INSERT INTO HasAttack VALUES (73, 'Acido')");
            db.execSQL("INSERT INTO HasAttack VALUES (74, 'Sassata')");
            db.execSQL("INSERT INTO HasAttack VALUES (74, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (75, 'Sassata')");
            db.execSQL("INSERT INTO HasAttack VALUES (75, 'Colpodifango')");
            db.execSQL("INSERT INTO HasAttack VALUES (76, 'Sassata')");
            db.execSQL("INSERT INTO HasAttack VALUES (76, 'Colpodifango')");
            db.execSQL("INSERT INTO HasAttack VALUES (77, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (77, 'Braciere')");
            db.execSQL("INSERT INTO HasAttack VALUES (78, 'Colpo Basso')");
            db.execSQL("INSERT INTO HasAttack VALUES (78, 'Braciere')");
            db.execSQL("INSERT INTO HasAttack VALUES (79, 'Confusione')");
            db.execSQL("INSERT INTO HasAttack VALUES (79, 'Pistolacqua')");
            db.execSQL("INSERT INTO HasAttack VALUES (80, 'Confusione')");
            db.execSQL("INSERT INTO HasAttack VALUES (80, 'Pistolacqua')");
            db.execSQL("INSERT INTO HasAttack VALUES (81, 'Scintilla')");
            db.execSQL("INSERT INTO HasAttack VALUES (81, 'Tuonoshock')");
            db.execSQL("INSERT INTO HasAttack VALUES (82, 'Scintilla')");
            db.execSQL("INSERT INTO HasAttack VALUES (82, 'Tuonoshock')");
            db.execSQL("INSERT INTO HasAttack VALUES (83, 'Taglio')");
            db.execSQL("INSERT INTO HasAttack VALUES (83, 'Tagliofuria')");
            db.execSQL("INSERT INTO HasAttack VALUES (84, 'Beccata')");
            db.execSQL("INSERT INTO HasAttack VALUES (84, 'Attacco Rapido')");
            db.execSQL("INSERT INTO HasAttack VALUES (85, 'Alacciaio')");
            db.execSQL("INSERT INTO HasAttack VALUES (85, 'Finta')");
            db.execSQL("INSERT INTO HasAttack VALUES (86, 'Geloscheggia')");
            db.execSQL("INSERT INTO HasAttack VALUES (86, 'Pistolacqua')");
            db.execSQL("INSERT INTO HasAttack VALUES (87, 'Geloscheggia')");
            db.execSQL("INSERT INTO HasAttack VALUES (87, 'Alitogelido')");
            db.execSQL("INSERT INTO HasAttack VALUES (88, 'Fangosberla')");
            db.execSQL("INSERT INTO HasAttack VALUES (88, 'Acido')");
            db.execSQL("INSERT INTO HasAttack VALUES (89, 'Velenpuntura')");
            db.execSQL("INSERT INTO HasAttack VALUES (89, 'Acido')");
            db.execSQL("INSERT INTO HasAttack VALUES (90, 'Geloscheggia')");
            db.execSQL("INSERT INTO HasAttack VALUES (90, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (91, 'Geloscheggia')");
            db.execSQL("INSERT INTO HasAttack VALUES (91, 'Alitogelido')");
            db.execSQL("INSERT INTO HasAttack VALUES (92, 'Sbigoattacco')");
            db.execSQL("INSERT INTO HasAttack VALUES (92, 'Leccata')");
            db.execSQL("INSERT INTO HasAttack VALUES (93, 'Ombrartigli')");
            db.execSQL("INSERT INTO HasAttack VALUES (93, 'Leccata')");
            db.execSQL("INSERT INTO HasAttack VALUES (94, 'Ombrartigli')");
            db.execSQL("INSERT INTO HasAttack VALUES (94, 'Sbigoattacco')");
            db.execSQL("INSERT INTO HasAttack VALUES (95, 'Sassata')");
            db.execSQL("INSERT INTO HasAttack VALUES (95, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (96, 'Confusione')");
            db.execSQL("INSERT INTO HasAttack VALUES (96, 'Botta')");
            db.execSQL("INSERT INTO HasAttack VALUES (97, 'Confusione')");
            db.execSQL("INSERT INTO HasAttack VALUES (97, 'Cozzata Zen')");
            db.execSQL("INSERT INTO HasAttack VALUES (98, 'Bolla')");
            db.execSQL("INSERT INTO HasAttack VALUES (98, 'Colpodifango')");
            db.execSQL("INSERT INTO HasAttack VALUES (99, 'Ferrartigli')");
            db.execSQL("INSERT INTO HasAttack VALUES (99, 'Colpodifango')");
            db.execSQL("INSERT INTO HasAttack VALUES (100, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (100, 'Scintilla')");
            db.execSQL("INSERT INTO HasAttack VALUES (101, 'Scintilla')");
            db.execSQL("INSERT INTO HasAttack VALUES (101, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (102, 'Confusione')");
            db.execSQL("INSERT INTO HasAttack VALUES (103, 'Confusione')");
            db.execSQL("INSERT INTO HasAttack VALUES (103, 'Cozzata Zen')");
            db.execSQL("INSERT INTO HasAttack VALUES (104, 'Spaccaroccia')");
            db.execSQL("INSERT INTO HasAttack VALUES (104, 'Fangosberla')");
            db.execSQL("INSERT INTO HasAttack VALUES (105, 'Spaccaroccia')");
            db.execSQL("INSERT INTO HasAttack VALUES (105, 'Fangosberla')");
            db.execSQL("INSERT INTO HasAttack VALUES (106, 'Spaccaroccia')");
            db.execSQL("INSERT INTO HasAttack VALUES (106, 'Colpo Basso')");
            db.execSQL("INSERT INTO HasAttack VALUES (107, 'Spaccaroccia')");
            db.execSQL("INSERT INTO HasAttack VALUES (107, 'Pugnoscarica')");
            db.execSQL("INSERT INTO HasAttack VALUES (108, 'Cozzata Zen')");
            db.execSQL("INSERT INTO HasAttack VALUES (108, 'Leccata')");
            db.execSQL("INSERT INTO HasAttack VALUES (109, 'Acido')");
            db.execSQL("INSERT INTO HasAttack VALUES (109, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (110, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (110, 'Acido')");
            db.execSQL("INSERT INTO HasAttack VALUES (111, 'Spaccaroccia')");
            db.execSQL("INSERT INTO HasAttack VALUES (111, 'Fangosberla')");
            db.execSQL("INSERT INTO HasAttack VALUES (112, 'Spaccaroccia')");
            db.execSQL("INSERT INTO HasAttack VALUES (112, 'Fangosberla')");
            db.execSQL("INSERT INTO HasAttack VALUES (113, 'Cozzata Zen')");
            db.execSQL("INSERT INTO HasAttack VALUES (113, 'Botta')");
            db.execSQL("INSERT INTO HasAttack VALUES (114, 'Frustata')");
            db.execSQL("INSERT INTO HasAttack VALUES (115, 'Colpo Basso')");
            db.execSQL("INSERT INTO HasAttack VALUES (115, 'Fangosberla')");
            db.execSQL("INSERT INTO HasAttack VALUES (116, 'Bolla')");
            db.execSQL("INSERT INTO HasAttack VALUES (116, 'Pistolacqua')");
            db.execSQL("INSERT INTO HasAttack VALUES (117, 'Pistolacqua')");
            db.execSQL("INSERT INTO HasAttack VALUES (117, 'Dragospiro')");
            db.execSQL("INSERT INTO HasAttack VALUES (118, 'Beccata')");
            db.execSQL("INSERT INTO HasAttack VALUES (118, 'Colpodifango')");
            db.execSQL("INSERT INTO HasAttack VALUES (119, 'Velenpuntura')");
            db.execSQL("INSERT INTO HasAttack VALUES (119, 'Beccata')");
            db.execSQL("INSERT INTO HasAttack VALUES (120, 'Attacco Rapido')");
            db.execSQL("INSERT INTO HasAttack VALUES (120, 'Pistolacqua')");
            db.execSQL("INSERT INTO HasAttack VALUES (121, 'Attacco Rapido')");
            db.execSQL("INSERT INTO HasAttack VALUES (121, 'Pistolacqua')");
            db.execSQL("INSERT INTO HasAttack VALUES (122, 'Confusione')");
            db.execSQL("INSERT INTO HasAttack VALUES (122, 'Cozzata Zen')");
            db.execSQL("INSERT INTO HasAttack VALUES (123, 'Alacciaio')");
            db.execSQL("INSERT INTO HasAttack VALUES (123, 'Tagliofuria')");
            db.execSQL("INSERT INTO HasAttack VALUES (124, 'Alitogelido')");
            db.execSQL("INSERT INTO HasAttack VALUES (124, 'Botta')");
            db.execSQL("INSERT INTO HasAttack VALUES (125, 'Colpo Basso')");
            db.execSQL("INSERT INTO HasAttack VALUES (125, 'Tuonoshock')");
            db.execSQL("INSERT INTO HasAttack VALUES (126, 'Braciere')");
            db.execSQL("INSERT INTO HasAttack VALUES (126, 'Colpokarate')");
            db.execSQL("INSERT INTO HasAttack VALUES (127, 'Spaccaroccia')");
            db.execSQL("INSERT INTO HasAttack VALUES (127, 'Tagliofuria')");
            db.execSQL("INSERT INTO HasAttack VALUES (128, 'Cozzata Zen')");
            db.execSQL("INSERT INTO HasAttack VALUES (128, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (129, 'Splash')");
            db.execSQL("INSERT INTO HasAttack VALUES (130, 'Morso')");
            db.execSQL("INSERT INTO HasAttack VALUES (130, 'Dragospiro')");
            db.execSQL("INSERT INTO HasAttack VALUES (131, 'Geloscheggia')");
            db.execSQL("INSERT INTO HasAttack VALUES (131, 'Alitogelido')");
            db.execSQL("INSERT INTO HasAttack VALUES (132, 'Trasformazione')");
            db.execSQL("INSERT INTO HasAttack VALUES (133, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (133, 'Attacco Rapido')");
            db.execSQL("INSERT INTO HasAttack VALUES (134, 'Pistolacqua')");
            db.execSQL("INSERT INTO HasAttack VALUES (135, 'Tuonoshock')");
            db.execSQL("INSERT INTO HasAttack VALUES (136, 'Braciere')");
            db.execSQL("INSERT INTO HasAttack VALUES (137, 'Attacco Rapido')");
            db.execSQL("INSERT INTO HasAttack VALUES (137, 'Azione')");
            db.execSQL("INSERT INTO HasAttack VALUES (138, 'Pistolacqua')");
            db.execSQL("INSERT INTO HasAttack VALUES (138, 'Colpodifango')");
            db.execSQL("INSERT INTO HasAttack VALUES (139, 'Sassata')");
            db.execSQL("INSERT INTO HasAttack VALUES (139, 'Pistolacqua')");
            db.execSQL("INSERT INTO HasAttack VALUES (140, 'Graffio')");
            db.execSQL("INSERT INTO HasAttack VALUES (140, 'Colpodifango')");
            db.execSQL("INSERT INTO HasAttack VALUES (141, 'Colpodifango')");
            db.execSQL("INSERT INTO HasAttack VALUES (141, 'Tagliofuria')");
            db.execSQL("INSERT INTO HasAttack VALUES (142, 'Alacciaio')");
            db.execSQL("INSERT INTO HasAttack VALUES (142, 'Morso')");
            db.execSQL("INSERT INTO HasAttack VALUES (143, 'Cozzata Zen')");
            db.execSQL("INSERT INTO HasAttack VALUES (143, 'Leccata')");
            db.execSQL("INSERT INTO HasAttack VALUES (144, 'Alitogelido')");;
            db.execSQL("INSERT INTO HasAttack VALUES (145, 'Tuonoshock')");;
            db.execSQL("INSERT INTO HasAttack VALUES (146, 'Braciere')");
            db.execSQL("INSERT INTO HasAttack VALUES (147, 'Dragospiro')");
            db.execSQL("INSERT INTO HasAttack VALUES (148, 'Dragospiro')");
            db.execSQL("INSERT INTO HasAttack VALUES (149, 'Alacciaio')");
            db.execSQL("INSERT INTO HasAttack VALUES (149, 'Dragospiro')");
            db.execSQL("INSERT INTO HasAttack VALUES (150, 'Confusione')");
            db.execSQL("INSERT INTO HasAttack VALUES (150, 'Cozzata Zen')");
            db.execSQL("INSERT INTO HasAttack VALUES (151, 'Psicotaglio')");
            db.execSQL("INSERT INTO HasAttack VALUES (151, 'Botta')");

            Toast.makeText(context, "onCreate called", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE " + SETTINGS);
            db.execSQL("DROP TABLE " + HASATTACK);
            db.execSQL("DROP TABLE " + HASULTI);
            db.execSQL("DROP TABLE " + ATTACK);
            db.execSQL("DROP TABLE " + ULTI);
            db.execSQL("DROP TABLE " + TYPE);
            db.execSQL("DROP TABLE " + POKEMON);
            db.execSQL("DROP TABLE " + CATCHES);
            onCreate(db);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_SHORT).show();
        }
    }
}