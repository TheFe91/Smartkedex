package com.example.thefe.newsmartkedex;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TheFe on 19/10/2016.
 */

public class PokemonDatabaseAdapter {

    private PokemonHelper helper;

    PokemonDatabaseAdapter(Context context) {
        helper = new PokemonHelper(context);
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

    void insertCopy (String attackName, String ultiName, String pokeName, int pokeID) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO Copy (AttackName, UltiName, PokemonName, PokemonID) VALUES ('"+attackName+"', '"+ultiName+"', '"+pokeName+"', '"+pokeID+"')");
        db.close();
    }

    void deleteCopy (int pokeId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM Copy WHERE ID = '"+pokeId+"'");
        db.close();
    }

    void erase () {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM Copy");
        db.execSQL("DELETE FROM Catches");
        db.execSQL("DELETE FROM Settings");
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

    String getCopyName (int copyID) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {PokemonHelper.POKEMONNAME};
        String name = "";
        Cursor cursor = db.query(PokemonHelper.COPY, columns, PokemonHelper.ID+"='"+copyID+"'", null, null, null, null);
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(PokemonHelper.POKEMONNAME));
        }

        db.close();
        return name;
    }

    String getMovesType (String name, String type) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {PokemonHelper.TYPE_NAME};
        Cursor cursor;
        String moveType = "";
        if (type.equals("Ulti"))
            cursor = db.query(type, columns, PokemonHelper.ULTI_NAME+"='"+name+"'", null, null, null, null);

        else
            cursor = db.query(type, columns, PokemonHelper.ATTACK_NAME+"='"+name+"'", null, null, null, null);

        while (cursor.moveToNext()) {
            moveType = cursor.getString(0);
        }

        db.close();
        return moveType;
    }

    List<String> getPokeTypes(int pokeID) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {PokemonHelper.TYPE_NAME};
        Cursor cursor = db.query(PokemonHelper.HASTYPE, columns, PokemonHelper.ID+"="+pokeID, null, null, null, null);
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(PokemonHelper.TYPE_NAME)));
        }

        db.close();
        return list;
    }

    Map<String, String> getAttacksStuff(String name, String table) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String columnName;

        Map<String, String> map = new HashMap<>();

        if (table.equals("Ulti"))
            columnName = "UltiName";
        else
            columnName = "AttackName";

        Cursor cursor = db.query(table, null, columnName+"='"+name+"'", null, null, null, null);
        while (cursor.moveToNext()) {
            map.put("type", cursor.getString(cursor.getColumnIndex(PokemonHelper.TYPE_NAME)));
            map.put("duration", cursor.getString(cursor.getColumnIndex(PokemonHelper.DURATION)));
            map.put("damage", cursor.getString(cursor.getColumnIndex(PokemonHelper.DAMAGE_DEALT)));
            if (table.equals("Ulti"))
                map.put("critical", cursor.getString(cursor.getColumnIndex(PokemonHelper.CRITICAL_CHANCE)));
        }

        db.close();
        return map;
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

    List<Integer> getIdsFromPokeID (int pokeID) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {PokemonHelper.ID};
        List<Integer> list = new ArrayList<>();
        Cursor cursor = db.query(PokemonHelper.COPY, columns, PokemonHelper.POKEMONID+"='"+pokeID+"'", null, null, null, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getInt(cursor.getColumnIndex(PokemonHelper.ID)));
        }

        db.close();
        return list;
    }

    String[] getPokeAttacks (int pokeCopy) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] attack = {"", ""};
        String[] columns = {PokemonHelper.ATTACK_NAME, PokemonHelper.ULTI_NAME};
        Cursor cursor = db.query(PokemonHelper.COPY, columns, PokemonHelper.ID+"="+pokeCopy, null, null, null, null);
        while (cursor.moveToNext()) {
            attack[0] = cursor.getString(cursor.getColumnIndex(PokemonHelper.ATTACK_NAME));
            attack[1] = cursor.getString(cursor.getColumnIndex(PokemonHelper.ULTI_NAME));
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

    List<String> getMoves (int pokeID, String table) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {""};
        if (table.equals("HasAttack"))
            columns[0] = PokemonHelper.ATTACK_NAME;
        else
            columns[0] = PokemonHelper.ULTI_NAME;

        Cursor cursor = db.query(table, columns, PokemonHelper.ID + "=" + pokeID, null, null, null, null);

        List<String> attacks = new ArrayList<>();

        while (cursor.moveToNext()) {
            if (table.equals("HasAttack"))
                attacks.add(cursor.getString(cursor.getColumnIndex(PokemonHelper.ATTACK_NAME))); //putting the content at the i position of the attacks array, taking the column index of ATTACK_NAME
            else
                attacks.add(cursor.getString(cursor.getColumnIndex(PokemonHelper.ULTI_NAME)));
        }

        db.close();
        return attacks;
    }

    ////////////////////////////////////////////////////////////////////UPDATERS////////////////////////////////////////////////////////////////////////////////////

    void updateLanguage (String newLanguage, String oldLanguage) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Settings SET Language = '"+newLanguage+"' WHERE Language = '"+oldLanguage+"';");
        db.close();
    }

    void updateOwner (String newOwner, String oldOwner) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Settings SET Owner = '"+newOwner+"' WHERE Owner = '"+oldOwner+"';");
        db.close();
    }

    void updateSmartkedex (String newSmartkedex, String oldSmartkedex) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Settings SET SmartkedexName = '"+newSmartkedex+"' WHERE SmartkedexName = '"+oldSmartkedex+"';");
        db.close();
    }

    void updatePokemonGO (int newPokemonGO, int oldPokemonGO) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Settings SET PokemonGO = '"+newPokemonGO+"' WHERE PokemonGO = '"+oldPokemonGO+"'");
        db.close();
    }

    void updatePokeAttacks (String attack, String ulti, String copyName, int pokeID) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Copy SET AttackName = '"+attack+"', UltiName = '" + ulti + "', PokemonName = '"+copyName+"' WHERE ID = "+pokeID);
        db.close();
    }

    private static class PokemonHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "PokemonDatabase.db";
        private static final int DATABASE_VERSION = 33;

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
        private static final String HASSTRENGHT = "HasStrenght";
        private static final String HASWEAKNESS = "HasWeakness";
        private static final String HASTYPE = "HasType";

        //Columns Declaration
        private static final String LANGUAGE = "Language";
        private static final String OWNER = "Owner";
        private static final String SMARTKEDEXNAME = "SmartkedexName";
        private static final String POKEMONGO = "PokemonGO";
        private static final String ID = "ID";
        private static final String POKEMONID = "PokemonID";
        private static final String POKEMONNAME = "PokemonName";
        private static final String ULTI_NAME = "UltiName";
        private static final String ATTACK_NAME = "AttackName";
        private static final String DAMAGE_DEALT = "DamageDealt";
        private static final String DURATION = "Duration";
        private static final String CRITICAL_CHANCE = "CriticalChance";
        private static final String TYPE_NAME = "TypeName";
        private static final String DESCRIPTION = "Description";



        //CREATE TABLE Statements
        private static final String CREATE_SETTINGS = "CREATE TABLE IF NOT EXISTS " + SETTINGS + "(" +
                                                       LANGUAGE + VARCHAR + "3), " +
                                                       OWNER + VARCHAR + "20) PRIMARY KEY, " +
                                                       SMARTKEDEXNAME + VARCHAR + "20), " +
                                                       POKEMONGO + INT + "1))";

        private static final String CREATE_POKEMON = "CREATE TABLE IF NOT EXISTS " + POKEMON + "(" +
                                                      POKEMONNAME + VARCHAR + "20), " +
                                                      DESCRIPTION + VARCHAR + "150), " +
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

        private static final String CREATE_HASSTRENGHT = "CREATE TABLE IF NOT EXISTS " + HASSTRENGHT + "(" +
                                                         ID + INT + "3), " +
                                                         TYPE_NAME + VARCHAR + "10), " +
                                                         "PRIMARY KEY (" + ID + ", " + TYPE_NAME + "), " +
                                                         "FOREIGN KEY (" + ID + ") REFERENCES " + POKEMON + "(" + ID + "), " +
                                                         "FOREIGN KEY (" + TYPE_NAME + ") REFERENCES " + TYPE + "(" + TYPE_NAME + "))";

        private static final String CREATE_HASWEAKNESS = "CREATE TABLE IF NOT EXISTS " + HASWEAKNESS + "(" +
                                                         ID + INT + "3), " +
                                                         TYPE_NAME + VARCHAR + "10), " +
                                                         "PRIMARY KEY (" + ID + ", " + TYPE_NAME + "), " +
                                                         "FOREIGN KEY (" + ID + ") REFERENCES " + POKEMON + "(" + ID + "), " +
                                                         "FOREIGN KEY (" + TYPE_NAME + ") REFERENCES " + TYPE + "(" + TYPE_NAME + "))";

        private static final String CREATE_COPY = "CREATE TABLE IF NOT EXISTS " + COPY + "(" +
                                                  ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                  POKEMONID + INT + " 3), " +
                                                  ATTACK_NAME + VARCHAR + "15), " +
                                                  ULTI_NAME + VARCHAR + "15), " +
                                                  POKEMONNAME + VARCHAR + "20), " +
                                                  "FOREIGN KEY (" + POKEMONID + ") REFERENCES " + POKEMON + " (" + ID + "))";

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
            db.execSQL(CREATE_HASTYPE);
            db.execSQL(CREATE_HASSTRENGHT);
            db.execSQL(CREATE_HASWEAKNESS);

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
                db.execSQL("INSERT INTO Pokemon (PokemonName, ID) VALUES ('" + pokemonDetails.getName(i) + "', " + i + ")");
            }
            
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Bulbasaur. È possibile vedere Bulbasaur mentre schiaccia un pisolino sotto al sole. Ha un seme piantato sulla schiena. Grazie ai raggi solari, il seme cresce, ingrandendosi progressivamente')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Ivysaur. Cè un germoglio piantato nella schiena di Ivysaur. Per sopportarne il peso, le zampe e il corpo crescono robusti. Quando inizia a passare più tempo esposto al sole, significa che il germoglio sboccerà presto in un grande fiore')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Venusaur. C\'è un grande fiore sulla schiena di Venusaur. Si dice che i colori diventino più vividi con il giusto nutrimento e i raggi solari. Il suo profumo calma le reazioni emotive delle persone')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Charmander. La fiamma sulla punta della coda indica il suo stato emotivo. Se la fiamma ondeggia significa che Charmander si sta divertendo. Quando il Pokémon si infuria, la fiamma arde violentemente')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Charmeleon. Charmeleon distrugge il nemico senza pietà con i suoi artigli affilati. Quando incontra un avversario molto forte diventa aggressivo. In questo stato di grande agitazione la fiamma della coda diventa di colore bianco bluastro')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Charizard. Charizard solca i cieli in cerca di nemici molto forti. Riesce a emettere fiammate di un calore tale da fondere ogni cosa. Tuttavia, non rivolge mai le sue micidiali lingue di fuoco contro avversari più deboli di lui')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Squirtle. La corazza di Squirtle non serve soltando da protezione. La particolare forma arrotondata e le scanalature superficiali lo aiutano a minimizzare l'attrito dell'acqua per nuotare ad alta velocità')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Wartortle. La grande coda di Wartortle è coperta da una folta pelliccia, che diventa sempre più scura con l'avanzare dell'età. I graffi sulla corazza indicano la potenza di questo Pokémon come lottare')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Blastoise. Blastoise è dotato di cannoni ad acqua che fuoriescono dalla corazza. I getti emessi sono così precisi da riuscire a colpire un bersaglio a una distanza di 50m')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Caterpie. Caterpie mangia voracemente. È in grado di divorare foglie più grandi del suo stesso corpo in pochi istanti. Questo Pokémon emette un odore terrificante dalle antenne')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Metapod. La corazza di questo Pokémon è dura come una lastra di ferro. Metapod non si muove molto. Sta immobile per preparare il morbido interno della dura corazza all'evoluzione')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Butterfree. Butterfree è dotato di abilità molto raffinate per individuare il delizioso nettare dei fiori. Riesce a trovare, estrarre a trasportare il nettare dai fiori in boccio al nido anche per 10km')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Weedle. Weedle ha un senso dell'olfatto estremamente sviluppato riesce a distinguere le sue foglie preferite da quelle che lo disgustano semplicemente annusandole con il grande naso rosso')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Kakuna. Kakuna rimane praticamente immobile abbarbicato agli alberi. Tuttavia, interamente è intento a preparare la sua futura evoluzione. Lo si può capire dall'intensità del calore sviluppato dal guscio')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Beedrill. Beedrill difende strenuamente il proprio territorio. Per ragioni di sicurezza nessuno può avvicinarsi al suo nido. Se vengono disturbati, questi Pokémon attaccano violentemente in sciami')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Pidgey.Pidgey ha un senso dell'orientamento molto sviluppato. È sempre in grado di ritornare al suo nido, anche quando si spinge molto lontano dal suo ambiente abituale')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Pidgeotto. Pidgeotto delimita come proprio un territorio immenso, che difende controllando costantemente. Se tale territorio viene invaso, questo Pokémon non ha pietà nel punire i nemici con i suoi artigli affilati')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Pidgeot. Questo Pokémon è caratterizzato da uno stupendo piumaggio dai colori vivaci e brillanti. Molti Allenatori sono colpiti dall'evidente bellezza delle piume sulla testa. Per questo spesso scelgono Pidgeot come loro Pokémon')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Rattata. Rattata è estremamente cauto. Anche quando dorme tiene sempre le orecchie tese, muovendole come sonde. Non ha particolari esigenze di habitat: costruisce la propria tana ovunque')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Raticate. Le potenti zanne di Raticate crescono in continuazione. Per ridurne la crescita rode rocce e tronchi. Spesso si vedono i segni delle sue zanne anche sui muri delle case')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Spearow. Spearow emette un grido molto acuto, percepibile anche a 1 km di distanza. Quando questo grido riecheggia nei dintorni, questo Pokémon intende avvertire i suoi simili di un pericolo imminente')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Fearow. Fearow è caratterizzato da un collo e un becco molto lunghi, dalla forma ottimale per la cattura della preda a terra o in acqua. Muove agilmente il becco lungo e affusolato per stanare la preda')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Ekans. Ekans si attorciglia a spirale per riposarsi. In questa posizione riesce a reagire alle insidie provenienti da ogni parte grazie alla testa sollevata e allo sguardo fulminante')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Arbok. Questo Pokémon è così forte da riuscire a stritolare qualsiasi cosa col corpo, persino un bidone in acciaio. Se Arbok si avvinghia a un nemico è impossibile sfuggire a questa morsa fatale')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Pikachu. Quando Pikachu incontra qualcosa che non conosce, lo colpisce con una scarica elettrica. Quando si vede una bacca annerita, è evidente che questo Pokemon ha emesso una scossa troppo forte')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Raichu. Se l'elettricità accumulata nelle guance diventa eccessiva, Raichu la scarica piantando la coda nel terreno. Vicino al suo nido sono presenti spesso chiazze di erba arsa')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Sandshrew. Il corpo di Sandshrew ha una conformazione che gli consente di assorbire l'acqua senza perdite e di sopravvivere nell'aridità del deserto. Questo Pokémon si avvolge su se stesso per proteggersi dai nemici')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Sandslash. Il corpo di Sandslash è ricoperto da aculei coriacei, costituiti da parti di corazza indurita. I vecchi aculei cadono una volta all'anno per essere sostituiti dai nuovi che crescono sotto i precedenti')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Nidoran Femmina. Gli aculei di Nidoran Femmina secernono un potente veleno. Si pensa che si siano sviluppati per proteggere questo Pokémon dal corpo minuto. Quando è adirato, rilascia una potente tossina dal corno')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Nidorina. Quando questi Pokémon si riuniscono con gli amici e la famiglia, tengono i loro aculei a debita distanza per evitare di ferirsi a vicenda. Se allontanati dal branco, diventano nervosi e irascibili')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Nidoqueen. Il corpo di Nidoqueen è racchiuso in una corazza durissima. Riesce a scagliare i nemici lontano con un colpo secco. Questo Pokémon dà il massimo di sé quando difende i propri cuccioli')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Nidoran Maschio. Nidoran Maschio ha sviluppato dei muscoli per muovere liberamente le orecchie in qualsiasi direzione. Così, questo Pokémon è in grado di percepire anche il più flebile fruscio')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Nidorino. Nidorino è dotato di un corno più duro del diamante. Quando percepisce una presenza ostile, gli si rizzano immediatamente tutti gli aculei sulla schiena. A questo punto sfida il nemico con tutta la sua forza')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Nidoking. La possente coda di Nidoking è dotata di un enorme potere distruttivo. Con un solo colpo riesce ad abbattere un pilone metallico. Quando si scatena non c'è modo di fermare la sua furia')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Clefairy. In ogni notte di luna piena questi Pokémon escono in gruppo a giocare. All'alba i Clefairy tornano stanchi nella quiete delle loro tane montane e vanno a dormire stretti fra loro')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Clefabe. Clefable si muove saltellando leggero come se fluttuasse sorretto dalle sue ali. Così riesce anche a camminare sull'acqua. È solito passeggiare sui laghi in silenziose notti di luna piena')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Vulpix. Al momento della nascita Vulpix ha una sola coda bianca. Se il Pokémon riceve molto amore dal proprio Allenatore la coda si divide in sei diramazioni che poi si arricciano elegantemente')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Ninetales. Ninetales emana sinistri bagliori dai luminosi occhi rossi per acquisire il controllo totale della mente del suo nemico. Si dice che possa vivere anche per mille anni')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Jigglypuff. Le corde vocali di Jigglypuff gli consentono di cantare esattamente alla lunghezza d'onda richiesta per addormentare il suo avversario')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Wigglytuff. Wigglytuff è dotato di occhi grandi come dischi, resi lucidi da un sottile strato lacrimale in superficie. Se gli va della polvere negli occhi, questa è lavata via subito')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Zubat. Zubat rimane perfettamente immobile al buio durante le ore diurne più luminose. Infatti la prolungata esposizione al sole gli provoca delle bruciature')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Golbat. Golbat adora bere il sangue di creature viventi. È particolarmente attivo di notte, nella totale oscurità. Svolazza nel cielo notturno in cerca di sangue fresco')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Oddish. Durante il giorno, Oddish si nasconde nel terreno per assorbire sostanze nutritive con tutto il corpo. Più il suolo è fertile, più lucide diventano le foglie')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Gloom. Gloom rilascia un odore fetido dal pistillo del suo fiore. Quando è in pericolo, l'olezzo peggiora. Quando invece si sente tranquillo e sicuro, non emana questa sostanza maleodorante')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Vileplume. Il polline velenoso di Vileplume scatena paurose reazioni allergiche. Per questo motivo si consiglia di non avvicinarsi mai a nessun fiore di bosco, per quanto bello possa essere')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Paras. Paras ha sulla schiena dei funghi parassiti chiamati tochukaso, che crescono traendo nutrimento dal Paras ospite. Sono molto apprezzati come farmaco di longevità')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Parasect. I Parasect agiscono in gruppo e sono noti per la loro abilità di infestare gli alberi traendo nutrimento da tronchi e radici. Quando muore un albero infestato, ne cercano subito un altro')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Venonat. Si dice che per proteggersi Venonat abbia sviluppato una pelliccia sottile e irsuta che ricopre il suo corpo. Nemmeno la preda più piccola può sfuggire ai suoi occhi enormi')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Venomoth. Venomoth è un Pokémon notturno, cioè attivo soltanto di notte. Le sue prede favorite sono i piccoli insetti che si raggruppano attorno ai lampioni attratti dalla luce nel buio')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Diglett. Diglett è allevato in molte aziende agricole. La ragione è semplice: quando questo Pokémon scava, lascia il suolo perfettamente arato per la semina. Il suolo è poi pronto per la coltivazione di squisite verdure')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Dugtrio. Dugtrio è un Pokémon formato da tre unità legate in un solo corpo. Ogni trio è un'unica mente pensante. Le tre unità collaborano scavando instancabilmente')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Meowth. Meowth ritira i suoi artigli affilati all'interno delle zampe per poter aggirarsi furtivo camuffando le tracce. Per qualche oscura ragione questo Pokémon è attratto dalle monetine che splendono alla luce')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Persian. Persian è dotato di sei baffetti furbi che gli conferiscono un aspetto forte. I baffi captano ogni movimento dell'aria riconoscendo presenze vicine. Se afferrato per i baffi, diventa docile')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Psyduck. Psyduck usa poteri misteriosi, che scatenano onde cerebrali rilevabili apparentemente soltanto su persone addormentate. Questa scoperta ha causato reazioni contrastanti tra gli studiosi')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES (' Golduck. Le zampe anteriori e posteriori a pinna dotate di membrana interdigitale e il corpo longilineo di Golduck gli conferiscono una velocità incredibile. Infatti è molto più veloce dei più esperti nuotatori umani')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Mankey. Quando Mankey inizia a tremare e la sua respirazione nasale diventa pesante significa che si sta infuriando. Tuttavia, a causa dei suoi repentini attacchi d'ira, è impossibile per chiunque sfuggire alla sua furia')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Primeape. Quando Primeape s'infuria, la circolazione sanguigna si fa intensa, irrobustendo i suoi muscoli. Allo stesso tempo, tuttavia, perde anche la sua lucidità e intelligenza')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Growlithe. Growlithe ha un senso dell'olfatto molto sviluppato: una volta annusato qualcosa non scorda più quell'odore. Sfrutta questa dote per determinare le emozioni delle altre creature')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Arcanine. Arcanine è noto per la sua velocità. Si dice sia in grado di percorrere 10.000 km in un giorno e una notte. Il fuoco che arde indomabile nel suo corpo è fonte di forza')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Poliwag. Poliwag ha una pelle molto sottile, attraverso cui è possibile intravedere i suoi organi interni spiraliformi. Nonostante il suo esiguo spessore, la pelle è molto elastica. Anche le zanne più affilate non riescono a lacerarla')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Poliwhirl. La superficie del corpo di Poliwhirl è sempre umida e unta di fluido oleoso. Grazie a questa pellicola viscida, nella lotta riesce a sfuggire agilmente alle grinfie del nemico')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Poliwrath. Poliwrath è dotato di muscoli molto sviluppati e robusti. Per quanto si alleni, non si stanca mai. Questo Pokémon è così infaticabile e forte che riesce ad attraversare a nuoto interi oceani')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Abra. Abra dorme per 18 ore al giorno, ma riesce a captare la presenza dei nemici anche durante il sonno. In questo caso il Pokémon si teletrasporta immediatamente al sicuro')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Kadabra. Kadabra emette particolari onde alfa che causano mal di testa a chi si trova nelle vicinanze. Soltanto chi ha una psiche molto stabile può sperare di diventare Allenatore di questo Pokémon')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Alakazam. Il cervello di Alakazam cresce in continuazione rendendo la sua testa troppo pesante per il collo. Pertanto è costretto a usare i suoi poteri psicocinetici per sostenerla')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Machop. I particolari muscoli di Machop non si infiammano mai, nemmeno in caso di esercizio eccessivo. Questo Pokémon ha forza sufficiente per scagliare lontano un centinaio di uomini')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Machoke. I muscoli perfettamente tonici di Machoke possiedono la durezza dell'acciaio. Questo Pokémon è così forte da riuscire a sollevare un lottatore di sumo con un dito')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Machamp. Machamp è dotato di una forza tale da riuscire a scagliare lontano ogni cosa. Le sue braccia sono troppo possenti per riuscire a eseguire qualsiasi lavoro che richieda attenzione e destrezza. Tende ad agire prima di pensare')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Bellsprout. Il corpo esile e flessibile di Bellsprout gli consente di piegarsi e oscillare per evitare ogni attacco, anche violento. Dalla bocca sputa un fluido corrosivo in grado di sciogliere anche il ferro')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Weepinbell. Weepinbell è dotato di un grande uncino posteriore. Di notte lo usa per appendersi a un ramo e mettersi a dormire. Talvolta, agitandosi nel sonno, si sveglia al suolo')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Victreebel. Victreebel è dotato di una lunga liana che parte dalla testa. Il Pokémon la sventola e la agita come fosse un'esca per attirare la preda. Quando la preda ignara si avvicina, il Pokémon la inghiotte in un sol boccone')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Tentacool. Il corpo di Tentacool è costituito quasi solo d'acqua. Se pescato ed esposto all'aria, si secca totalmente. In caso di disidratazione, dunque, si consiglia di rimetterlo in mare')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Tentacruel. Tentacruel ha due grandi sfere rosse sulla testa. Le sfere brillano prima di emettere potenti ultrasuoni. Gli attacchi di questo Pokémon creano onde impetuose nei dintorni')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Geodude. Col passare del tempo, i profili di Geodude diventano sempre più levigati, rendendolo più tondeggiante. Tuttavia il cuore di questo Pokémon rimane sempre duro, ruvido e roccioso')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Graveler. Graveler si ciba di rocce. Pare che preferisca quelle ricoperte di muschio. Questo Pokémon mangia ogni giorno dieci quintali di pietre')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Golem. I Golem vivono in alta montagna. In caso di terremoto, questi Pokémon rotolano giù per i monti in massa e si fermano a valle')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Ponyta. Ponyta è molto debole alla nascita e riesce a malapena a reggersi in piedi. Poi si rinforza a furia di inciampare e cadere, cercando di tenere il passo dei suoi genitori')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Rapidash. Rapidash si vede comunemente galoppare in campagne e pianure. Quando si lancia a tutta velocità la sua criniera infuocata brilla e arde mentre galoppa fino a 240 km/h')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Slowpoke. Slowpoke usa la coda per pescare la preda, immergendola in acqua dalle rive dei fiumi. Tuttavia, spesso dimentica cosa stava facendo e trascorre giorni interi a ciondolare vicino ai corsi d'acqua')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Slowbro. Slowbro ha uno Shellder saldamente attaccato alla coda, che quindi non può più essere usata per pescare. Così Slowbro può essere visto nuotare di malavoglia per catturare una preda')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Magnemite. Magnemite si attacca alle linee elettriche per caricarsi di energia. In caso di assenza di corrente in casa, controllare gli interruttori. Potrebbero esserci dei Pokémon attaccati alla cassetta degli interruttori')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Magneton. Magneton emette una potente carica magnetica fatale per tutte le apparecchiature meccaniche. Nelle città, le sirene avvertono la popolazione di grandi invasioni di questi Pokémon')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Farfetch\'d. Farfetch\'d ha sempre con sé il gambo di qualche pianta. Pare che alcuni gambi siano migliori di altri. Spesso lotta con altri Pokémon per avere i gambi migliori')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Doduo. Le due teste di Doduo non dormono mai nello stesso momento, in modo che una sia sempre vigile per difendersi dai nemici, mentre l'altra può riposare tranquilla')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Dodrio. Se le tre teste di Dodrio guardano in tre direzioni diverse, significa che il Pokémon è vigile. In questo stato il Pokémon non va avvicinato, poiché potrebbe decidere di attaccare')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Seel. Seel caccia le prede nel mare gelido sotto le lastre di ghiaccio. Per respirare apre un foro nel ghiaccio usando la protuberanza appuntita sulla sommità del capo')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Dewgong. Dewgong adora riposare su una gelida lastra di ghiaccio. In passato i marinai confondevano il profilo di questo Pokémon addormentato sulla superficie gelata con una sirena')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Grimer. Il corpo fangoso e gommoso di Grimer può passare attraverso ogni apertura, anche la più angusta. Si intrufola dentro le condotte fognarie per bere le ripugnanti acque di scarico')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Muk. Il corpo di questo Pokémon emana un liquido marcio dall'odore nauseabondo. Una sola goccia di questo liquido è in grado di rendere l'acqua di una piscina putrida e stagnante')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Shellder. Di notte Shellder usa la sua lingua larga per scavare una buca sul fondo del mare e infilarcisi per dormire. Quando dorme chiude la conchiglia ma lascia la lingua fuori a penzoloni')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Cloyster. Cloyster è in grado di nuotare in mare aperto, ingerendo acqua dalla bocca per poi espellerla violentemente a mo' di propulsore. Questo Pokémon usa lo stesso metodo per sparare punte dalla conchiglia')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Gastly. Gastly è composto prevalentemente di sostanze gassose. Se esposto al vento forte il suo corpo gassoso si disperde subito. Gruppi di questi Pokémon si radunano sotto i cornicioni per ripararsi dal vento')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Haunter. Haunter è un Pokémon pericoloso. Se lo si vede far cenni mentre fluttua nell'oscurità, è meglio fuggire, poiché il Pokémon cercherà di leccare il malcapitato e succhiargli la vita')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Gengar. Talvolta, nelle notti buie, può accadere che un passante sia improvvisamente assalito dalla propria ombra. È opera di Gengar, che usa avvicinarsi fingendosi un'ombra')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Onix. Onix nel cervello ha una calamita, che agisce da bussola per consentirgli di non perdere l'orientamento mentre scava sottoterra. Con il passare del tempo il suo corpo diventa sempre più smussato e levigato')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Drowzee. Drowzee è solito nutrirsi dei sogni degli altri mentre dormono, estraendoli dalle loro narici. Se durante il sonno si avverte prurito al naso, significa che Drowzee è già all'opera')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Hypno. Hypno tiene in mano un pendolo. Il movimento oscillatorio e i riflessi del pendolo fanno cadere l'avversario in un profondo stato d'ipnosi. Cercando la preda il Pokémon si prepara lucidando il pendolo')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Krabby. Krabby vive in spiaggia, nascosto in buche scavate nella sabbia. Sulle spiagge molto sabbiose, dove scarseggia il cibo, è possibile vedere questi Pokémon attaccarsi a vicenda in difesa del proprio territorio')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Kingler. Kingler è dotato di un'enorme chela, che brandisce nell'aria per comunicare con i suoi simili. Tuttavia, dato il notevole peso della chela sovradimensionata, il Pokémon si stanca in fretta')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Voltrob. Voltorb è stato avvistato per la prima volta in un'azienda produttrice di Poké Ball. Il nesso tra la sua presenza in questa fabbrica e l'estrema somiglianza a una Poké Ball rimane un mistero')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Electrode. Electrode si nutre dell'elettricità presente nell'atmosfera. Quando i fulmini solcano il cielo, è possibile scorgere molti di questi Pokémon esplodere per aver ingerito un'eccessiva dose di elettricità')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Exeggcute. Questo Pokémon è costituito da un nucleo compatto formato da sei uova, che girano vorticosamente attraendosi a vicenda. Quando i gusci iniziano a rompersi, Exeggcute è prossimo all'evoluzione')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Exeggutor. Exeggutor è originario dei tropici. Le teste crescono costantemente se esposte a forte luce solare. Pare che quando le teste cadono, si uniscano per formare un Exeggcute')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Cubone. Cubone si strugge per la madre che non rivedrà mai più. Piange alla luna piena che somiglia molto alla madre. Le macchie sul teschio che indossa sono dovute alle lacrime copiose')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Marowak. Marowak si evolve da Cubone dopo aver superato il lutto per la perdita della madre ed essersi fatto forte. Questo Pokémon dall'animo forgiato e temprato non si fa prendere facilmente dallo sconforto')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Hitmonlee. Le zampe di Hitmonlee sono estremamente elastiche. Usandole a mo' di molla, si scaglia sul nemico con calci rovinosi. Dopo la lotta si strofina le zampe e scioglie i muscoli per recuperare le forze')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Hitmonchan. Si dice che Hitmonchan abbia l'animo di un pugile destinato a partecipare al campionato mondiale. Questo Pokémon ha uno spirito indomito e non demorde mai di fronte alle avversità')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Lickitung. Quando Lickitung si trova di fronte a qualcosa di nuovo, istintivamente lo assaggia con una leccata. È dotato infatti di una memoria legata al gusto e alla consistenza degli oggetti. Non ama i sapori aspri')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Koffing. Se Koffing si agita, aumenta la tossicità dei suoi gas interni e li espelle violentemente da tutto il corpo. Questo Pokémon riesce anche a gonfiarsi fino al punto di esplodere')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Weezing. Weezing adora i gas emanati dai resti alimentari in decomposizione. Si sente a suo agio in case sudicie e maltenute. La notte, mentre dormono tutti, lui si aggira furtivo tra i rifiuti domestici')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Rhyhorn. Rhyhorn corre diritto per la sua strada fracassando tutto ciò che trova. Non si scompone nemmeno scontrandosi con un blocco d'acciaio. Tuttavia, il giorno seguente può accusare qualche dolore')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Rhydon. Il corno di Rhydon riesce a fracassare anche un diamante grezzo. Un colpo della sua coda abbatte un edificio. La pelle-armatura di questo Pokémon è così coriacea da essere immune anche alle cannonate')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Chansey. Ogni giorno Chansey depone uova molto nutrienti. Queste uova sono così buone da far tornare l'appetito a chi lo ha perso')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Tangela. Se afferrate, le liane di Tangela si staccano con facilità, ma in modo indolore, consentendo così una pratica scappatoia. Le liane cadute vengono sostituite da quelle nuove il giorno dopo')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Kangaskhan. Vedendo un cucciolo di Kangaskhan giocare per conto suo, non bisogna mai disturbarlo o cercare di catturarlo. Il suo genitore, infatti, è sicuramente nei paraggi e potrebbe reagire molto violentemente')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Horsea. Horsea si nutre di piccoli insetti e del muschio trovato sugli scogli. Se la corrente oceanica si fa rapida, avvolge la coda attorno agli spuntoni di roccia o ai coralli per evitare di essere spazzato via')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Seadra. Seadra dorme dopo essersi sistemato tra i rami di un corallo. I cercatori di corallo spesso rischiano di essere punti dai suoi aculei velenosi se non si accorgono della sua presenza')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Goldeen. Goldeen è un Pokémon molto elegante dai movimenti aggraziati. Tuttavia, è necessario stare attenti poiché potrebbe attaccare violentemente con il suo corno')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Seaking. In autunno, i Seaking maschio sono soliti corteggiare le femmine con particolari danze rituali. In questa stagione il corpo di questi Pokémon assume una colorazione particolare')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Staryu. Al centro del corpo di Staryu è presente un nucleo che brilla di un rosso intenso. In spiaggia, a fine estate, il nucleo di questi Pokémon ha l'aspetto delle stelle nel firmamento')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Starmie. Il centro del corpo di Starmie, il suo nucleo, brilla in sette colori. Per la sua luminosità è chiamato \"Gemma del mare\"')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Mr.Mime. Mr. Mime è un esperto di mimica. La sua gestualità e i suoi movimenti convincono l'osservatore dell'esistenza di oggetti invisibili, che saranno percepiti come concreti e reali')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Scyther. Scyther è veloce come una saetta. La sua rapidità migliora l'efficacia delle due falci sulle zampe anteriori. Infatti riesce a tranciare con un colpo secco persino un tronco secolare')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Jynx. Jynx avanza oscillando e ancheggiando ritmicamente come in una specie di danza tesa ad ammaliare il nemico. Chi vede questo Pokémon è costretto a iniziare la stessa danza senza pensare a cosa sta facendo')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Electabuzz. All'arrivo di un temporale questi Pokémon si raggruppano e a gara scalano le vette più alte sperando di riuscire a prendere qualche fulmine. Alcune città usano gli Electabuzz al posto dei parafulmini')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Magmar. In lotta, Magmar sputa fiamme roventi da tutto il corpo per intimidire l'avversario. La sua furia irruente provoca ondate di calore che bruciano tutta la vegetazione attorno a lui')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Pinsir. Pinsir è incredibilmente forte. Può afferrare e sollevare tra le chele sul capo un nemico dal peso doppio rispetto al suo. I suoi movimenti sono impacciati in luoghi freddi')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Tauros. Questo Pokémon è molto insofferente e sempre alla ricerca della rissa. Se non trova un avversario con cui lottare, Tauros si lancia contro grossi tronchi per abbatterli e calmarsi')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Magikarp. Magikarp è un patetico esempio di Pokémon capace soltanto di dimenarsi e sguazzare. La natura di questo comportamento inutile è oggetto di studi da parte del mondo scientifico')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Gyarados. Quando Magikarp si evolve in Gyarados, le cellule cerebrali subiscono una trasformazione strutturale che presumibilmente determina la natura selvaggia e violenta di questo Pokémon')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Lapras. L'uomo ha portato Lapras alla sua quasi totale estinzione. Si dice che la sera lo si senta cantare malinconico per richiamare i suoi pochi simili ancora in vita')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Ditto. Ditto cambia la sua struttura cellulare per assumere molte altre forme. Tuttavia, quando si affida solo alla sua memoria, talvolta dimentica dettagli importanti')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Eevee. Eevee è dotato di un instabile corredo genetico che muta in base all'habitat naturale in cui è integrato. Le radiazioni di diverse pietre scatenano l'evoluzione di questo Pokémon')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Vaporeon. Vaporeon, frutto di una mutazione naturale, ha sviluppato pinne e branchie per poter vivere sott'acqua. Questo Pokémon ha la capacità di sfruttare l'acqua a suo vantaggio')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Jolteon. Le cellule di Jolteon generano un basso potenziale elettrico, che viene tuttavia amplificato dall'elettricità statica della sua pelliccia, da cui scatena fulmini. La pelliccia arruffata è fatta di aghi caricati elettricamente')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Flareon. La morbida pelliccia di Flareon ha uno scopo ben preciso: libera calore nell'aria in modo da abbassare la temperatura corporea, che nel caso di questo Pokémon può raggiungere anche 900 °C')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Porygon. Porygon è in grado di regredire ai suoi codici di programmazione originari ed entrare nel cyberspazio. Questo Pokémon è dotato di un sistema antipirateria per evitarne la duplicazione illecita')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Omanyte. Omanyte è uno degli antichi Pokémon estinti, recentemente rigenerati dall'uomo. In caso di aggressione da parte di un nemico, si ritira all'interno della sua dura corazza')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Omastar. Omastar usa i suoi tentacoli per catturare la preda. Si pensa che si sia estinto a causa della conchiglia ormai troppo pesante e ingombrante, che rendeva i suoi movimenti lenti e difficoltosi')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Kabuto. Kabuto è stato rigenerato da un fossile, sebbene in rarissimi casi siano stati scoperti esemplari viventi. Il Pokémon non ha subito alcuna mutazione nell'arco di oltre 300 milioni di anni')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Kabutops. Nell'antichità Kabutops cacciava le prede nuotando sott'acqua. Pare che il Pokémon si stesse adattando alla vita sulla terraferma, come confermano le mutazioni su branchie e zampe')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Aerodactyl. Aerodactyl risale all'epoca dei dinosauri. È stato rigenerato da materiale genetico estratto dall'ambra. Si suppone che in tempi antichi fosse considerato il signore dei cieli')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Snorlax. La giornata tipica di Snorlax consiste in nient'altro che mangiare e dormire. È un Pokémon così docile che i bambini usano la sua enorme pancia come parco giochi')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Articuno. Articuno è un Pokémon uccello leggendario, perfettamente a suo agio tra i ghiacci. Quando batte le ali, l'aria circostante diventa gelida. Per questo motivo, quando nevica, si dice che sia passato di lì')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Zapdos. Zapdos è un Pokémon uccello leggendario, perfettamente in grado di gestire l'elettricità. Acquista forza ed energia se colpito da qualche fulmine')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Moltres. Moltres è un Pokémon uccello leggendario, perfettamente in grado di gestire il fuoco. Se rimane ferito, si dice che si immerga nel magma liquido di un vulcano per ardere e tornare in salute')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Dratini. Dratini fa la muta cambiando continuamente la pelle vecchia, perché l'energia vitale del suo corpo aumenta fino a raggiungere livelli incontrollabili')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Dragonair. Dragonair accumula un'enorme quantità d'energia all'interno del corpo. Si dice che sia in grado di mutare le condizioni meteorologiche circostanti scaricando energia dai cristalli del collo e della coda')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Dragonite. Dragonite è in grado di fare il giro del mondo in sole sedici ore. È un Pokémon buono e mansueto che guida fino alla terraferma le navi prossime al naufragio')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Mewtwo. Mewtwo è stato creato grazie a una manipolazione genetica. Tuttavia, sebbene la scienza sia riuscita a creare un corpo di Pokémon, ha fallito nell'intento di dare a Mewtwo un animo generoso')");
            db.execSQL("INSERT INTO Pokemon (Description) VALUES ('Mew. Si dice che Mew possegga il patrimonio genetico di tutti i Pokémon. È in grado di rendersi invisibile, quando vuole, in modo da non farsi notare nemmeno da vicino')");
            
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
            db.execSQL("INSERT INTO HasType VALUES (10, 'Coleottero')");
            db.execSQL("INSERT INTO HasType VALUES (11, 'Coleottero')");
            db.execSQL("INSERT INTO HasType VALUES (12, 'Coleottero')");
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
            db.execSQL("INSERT INTO HasAttack VALUES (144, 'Alitogelido')");
            db.execSQL("INSERT INTO HasAttack VALUES (145, 'Tuonoshock')");
            db.execSQL("INSERT INTO HasAttack VALUES (146, 'Braciere')");
            db.execSQL("INSERT INTO HasAttack VALUES (147, 'Dragospiro')");
            db.execSQL("INSERT INTO HasAttack VALUES (148, 'Dragospiro')");
            db.execSQL("INSERT INTO HasAttack VALUES (149, 'Alacciaio')");
            db.execSQL("INSERT INTO HasAttack VALUES (149, 'Dragospiro')");
            db.execSQL("INSERT INTO HasAttack VALUES (150, 'Confusione')");
            db.execSQL("INSERT INTO HasAttack VALUES (150, 'Cozzata Zen')");
            db.execSQL("INSERT INTO HasAttack VALUES (151, 'Psicotaglio')");
            db.execSQL("INSERT INTO HasAttack VALUES (151, 'Botta')");

            //Populating HasUlti
            db.execSQL("INSERT INTO HasUlti VALUES (1, 'Vigorcolpo')");
            db.execSQL("INSERT INTO HasUlti VALUES (1, 'Semebomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (1, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (2, 'Solarraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (2, 'Vigorcolpo')");
            db.execSQL("INSERT INTO HasUlti VALUES (2, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (3, 'Solarraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (3, 'Petalodanza')");
            db.execSQL("INSERT INTO HasUlti VALUES (3, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (4, 'Lanciafiamme')");
            db.execSQL("INSERT INTO HasUlti VALUES (5, 'Lanciafiamme')");
            db.execSQL("INSERT INTO HasUlti VALUES (4, 'Pirolancio')");
            db.execSQL("INSERT INTO HasUlti VALUES (5, 'Pirolancio')");
            db.execSQL("INSERT INTO HasUlti VALUES (4, 'Nitrocarica')");
            db.execSQL("INSERT INTO HasUlti VALUES (5, 'Fuocopugno')");
            db.execSQL("INSERT INTO HasUlti VALUES (6, 'Fuocobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (6, 'Lanciafiamme')");
            db.execSQL("INSERT INTO HasUlti VALUES (6, 'Dragartigli')");
            db.execSQL("INSERT INTO HasUlti VALUES (7, 'Idrondata')");
            db.execSQL("INSERT INTO HasUlti VALUES (7, 'Idropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (7, 'Acquagetto')");
            db.execSQL("INSERT INTO HasUlti VALUES (8, 'Idropompa')");
            db.execSQL("INSERT INTO HasUlti VALUES (8, 'Acquagetto')");
            db.execSQL("INSERT INTO HasUlti VALUES (8, 'Geloraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (9, 'Idropompa')");
            db.execSQL("INSERT INTO HasUlti VALUES (9, 'Geloraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (9, 'Cannonflash')");
            db.execSQL("INSERT INTO HasUlti VALUES (10, 'Scontro')");
            db.execSQL("INSERT INTO HasUlti VALUES (11, 'Scontro')");
            db.execSQL("INSERT INTO HasUlti VALUES (12, 'Ronzio')");
            db.execSQL("INSERT INTO HasUlti VALUES (12, 'Segnoraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (12, 'Psichico')");
            db.execSQL("INSERT INTO HasUlti VALUES (13, 'Scontro')");
            db.execSQL("INSERT INTO HasUlti VALUES (14, 'Scontro')");
            db.execSQL("INSERT INTO HasUlti VALUES (15, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (15, 'Forbice X')");
            db.execSQL("INSERT INTO HasUlti VALUES (15, 'Aeroassalto')");
            db.execSQL("INSERT INTO HasUlti VALUES (16, 'Aerasoio')");
            db.execSQL("INSERT INTO HasUlti VALUES (16, 'Aeroassalto')");
            db.execSQL("INSERT INTO HasUlti VALUES (16, 'Tornado')");
            db.execSQL("INSERT INTO HasUlti VALUES (17, 'Aerasoio')");
            db.execSQL("INSERT INTO HasUlti VALUES (17, 'Aeroassalto')");
            db.execSQL("INSERT INTO HasUlti VALUES (17, 'Tornado')");
            db.execSQL("INSERT INTO HasUlti VALUES (18, 'Tifone')");
            db.execSQL("INSERT INTO HasUlti VALUES (18, 'Aerasoio')");
            db.execSQL("INSERT INTO HasUlti VALUES (18, 'Aeroassalto')");
            db.execSQL("INSERT INTO HasUlti VALUES (19, 'Fossa')");
            db.execSQL("INSERT INTO HasUlti VALUES (19, 'Corposcontro')");
            db.execSQL("INSERT INTO HasUlti VALUES (19, 'Iperzanna')");
            db.execSQL("INSERT INTO HasUlti VALUES (20, 'Iperraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (20, 'Iperzanna')");
            db.execSQL("INSERT INTO HasUlti VALUES (20, 'Fossa')");
            db.execSQL("INSERT INTO HasUlti VALUES (21, 'Perforbecco')");
            db.execSQL("INSERT INTO HasUlti VALUES (21, 'Aeroassalto')");
            db.execSQL("INSERT INTO HasUlti VALUES (21, 'Tornado')");
            db.execSQL("INSERT INTO HasUlti VALUES (22, 'Giravvita')");
            db.execSQL("INSERT INTO HasUlti VALUES (22, 'Aeroassalto')");
            db.execSQL("INSERT INTO HasUlti VALUES (22, 'Tornado')");
            db.execSQL("INSERT INTO HasUlti VALUES (23, 'Sporcolancio')");
            db.execSQL("INSERT INTO HasUlti VALUES (23, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (23, 'Avvolgibotta')");
            db.execSQL("INSERT INTO HasUlti VALUES (24, 'Fangonda')");
            db.execSQL("INSERT INTO HasUlti VALUES (24, 'Sporcolancio')");
            db.execSQL("INSERT INTO HasUlti VALUES (24, 'Neropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (25, 'Tuono')");
            db.execSQL("INSERT INTO HasUlti VALUES (25, 'Fulmine')");
            db.execSQL("INSERT INTO HasUlti VALUES (25, 'Scarica')");
            db.execSQL("INSERT INTO HasUlti VALUES (26, 'Tuono')");
            db.execSQL("INSERT INTO HasUlti VALUES (26, 'Tuonopugno')");
            db.execSQL("INSERT INTO HasUlti VALUES (26, 'Breccia')");
            db.execSQL("INSERT INTO HasUlti VALUES (27, 'Fossa')");
            db.execSQL("INSERT INTO HasUlti VALUES (27, 'Frana')");
            db.execSQL("INSERT INTO HasUlti VALUES (27, 'Rocciotomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (28, 'Terremoto')");
            db.execSQL("INSERT INTO HasUlti VALUES (28, 'Battiterra')");
            db.execSQL("INSERT INTO HasUlti VALUES (28, 'Rocciotomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (29, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (29, 'Velenodenti')");
            db.execSQL("INSERT INTO HasUlti VALUES (29, 'Corposcontro')");
            db.execSQL("INSERT INTO HasUlti VALUES (30, 'Fossa')");
            db.execSQL("INSERT INTO HasUlti VALUES (30, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (30, 'Velenodenti')");
            db.execSQL("INSERT INTO HasUlti VALUES (31, 'Terremoto')");
            db.execSQL("INSERT INTO HasUlti VALUES (31, 'Pietrataglio')");
            db.execSQL("INSERT INTO HasUlti VALUES (31, 'Fangonda')");
            db.execSQL("INSERT INTO HasUlti VALUES (32, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (32, 'Corposcontro')");
            db.execSQL("INSERT INTO HasUlti VALUES (32, 'Incornata')");
            db.execSQL("INSERT INTO HasUlti VALUES (33, 'Fossa')");
            db.execSQL("INSERT INTO HasUlti VALUES (33, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (33, 'Incornata')");
            db.execSQL("INSERT INTO HasUlti VALUES (34, 'Terremoto')");
            db.execSQL("INSERT INTO HasUlti VALUES (34, 'Megacorno')");
            db.execSQL("INSERT INTO HasUlti VALUES (34, 'Fangonda')");
            db.execSQL("INSERT INTO HasUlti VALUES (35, 'Forza Lunare')");
            db.execSQL("INSERT INTO HasUlti VALUES (35, 'Incantavoce')");
            db.execSQL("INSERT INTO HasUlti VALUES (35, 'Corposcontro')");
            db.execSQL("INSERT INTO HasUlti VALUES (36, 'Forza Lunare')");
            db.execSQL("INSERT INTO HasUlti VALUES (36, 'Magibrillio')");
            db.execSQL("INSERT INTO HasUlti VALUES (36, 'Psichico')");
            db.execSQL("INSERT INTO HasUlti VALUES (37, 'Lanciafiamme')");
            db.execSQL("INSERT INTO HasUlti VALUES (37, 'Nitrocarica')");
            db.execSQL("INSERT INTO HasUlti VALUES (37, 'Corposcontro')");
            db.execSQL("INSERT INTO HasUlti VALUES (38, 'Fuocobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (38, 'Ondacalda')");
            db.execSQL("INSERT INTO HasUlti VALUES (38, 'Lanciafiamme')");
            db.execSQL("INSERT INTO HasUlti VALUES (39, 'Carineria')");
            db.execSQL("INSERT INTO HasUlti VALUES (39, 'Incantavoce')");
            db.execSQL("INSERT INTO HasUlti VALUES (39, 'Corposcontro')");
            db.execSQL("INSERT INTO HasUlti VALUES (40, 'Iperraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (40, 'Magibrillio')");
            db.execSQL("INSERT INTO HasUlti VALUES (40, 'Carineria')");
            db.execSQL("INSERT INTO HasUlti VALUES (41, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (41, 'Velenodenti')");
            db.execSQL("INSERT INTO HasUlti VALUES (41, 'Aerasoio')");
            db.execSQL("INSERT INTO HasUlti VALUES (42, 'Funestovento')");
            db.execSQL("INSERT INTO HasUlti VALUES (42, 'Aerasoio')");
            db.execSQL("INSERT INTO HasUlti VALUES (42, 'Velenodenti')");
            db.execSQL("INSERT INTO HasUlti VALUES (43, 'Forza Lunare')");
            db.execSQL("INSERT INTO HasUlti VALUES (43, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (43, 'Semebomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (44, 'Forza Lunare')");
            db.execSQL("INSERT INTO HasUlti VALUES (44, 'Petalodanza')");
            db.execSQL("INSERT INTO HasUlti VALUES (44, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (45, 'Petalodanza')");
            db.execSQL("INSERT INTO HasUlti VALUES (45, 'Solarraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (45, 'Forza Lunare')");
            db.execSQL("INSERT INTO HasUlti VALUES (46, 'Semebomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (46, 'Forbice X')");
            db.execSQL("INSERT INTO HasUlti VALUES (46, 'Velenocroce')");
            db.execSQL("INSERT INTO HasUlti VALUES (47, 'Solarraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (47, 'Forbice X')");
            db.execSQL("INSERT INTO HasUlti VALUES (47, 'Velenocroce')");
            db.execSQL("INSERT INTO HasUlti VALUES (48, 'Segnoraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (48, 'Psicoraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (48, 'Velenodenti')");
            db.execSQL("INSERT INTO HasUlti VALUES (49, 'Ronzio')");
            db.execSQL("INSERT INTO HasUlti VALUES (49, 'Psichico')");
            db.execSQL("INSERT INTO HasUlti VALUES (49, 'Velenodenti')");
            db.execSQL("INSERT INTO HasUlti VALUES (50, 'Fossa')");
            db.execSQL("INSERT INTO HasUlti VALUES (50, 'Pantanobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (50, 'Rocciotomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (51, 'Terremoto')");
            db.execSQL("INSERT INTO HasUlti VALUES (51, 'Pantanobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (51, 'Pietrataglio')");
            db.execSQL("INSERT INTO HasUlti VALUES (52, 'Neropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (52, 'Nottesferza')");
            db.execSQL("INSERT INTO HasUlti VALUES (52, 'Corposcontro')");
            db.execSQL("INSERT INTO HasUlti VALUES (53, 'Carineria')");
            db.execSQL("INSERT INTO HasUlti VALUES (53, 'Gemmoforza')");
            db.execSQL("INSERT INTO HasUlti VALUES (53, 'Nottesferza')");
            db.execSQL("INSERT INTO HasUlti VALUES (54, 'Incrocolpo')");
            db.execSQL("INSERT INTO HasUlti VALUES (54, 'Idrondata')");
            db.execSQL("INSERT INTO HasUlti VALUES (54, 'Psicoraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (55, 'Idropompa')");
            db.execSQL("INSERT INTO HasUlti VALUES (55, 'Geloraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (55, 'Psichico')");
            db.execSQL("INSERT INTO HasUlti VALUES (56, 'Incrocolpo')");
            db.execSQL("INSERT INTO HasUlti VALUES (56, 'Breccia')");
            db.execSQL("INSERT INTO HasUlti VALUES (56, 'Calciobasso')");
            db.execSQL("INSERT INTO HasUlti VALUES (57, 'Incrocolpo')");
            db.execSQL("INSERT INTO HasUlti VALUES (57, 'Calciobasso')");
            db.execSQL("INSERT INTO HasUlti VALUES (57, 'Nottesferza')");
            db.execSQL("INSERT INTO HasUlti VALUES (58, 'Lanciafiamme')");
            db.execSQL("INSERT INTO HasUlti VALUES (58, 'Ruotafuoco')");
            db.execSQL("INSERT INTO HasUlti VALUES (58, 'Battiterra')");
            db.execSQL("INSERT INTO HasUlti VALUES (59, 'Fuocobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (59, 'Lanciafiamme')");
            db.execSQL("INSERT INTO HasUlti VALUES (59, 'Battiterra')");
            db.execSQL("INSERT INTO HasUlti VALUES (60, 'Corposcontro')");
            db.execSQL("INSERT INTO HasUlti VALUES (60, 'Pantanobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (60, 'Bollaraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (61, 'Idrovampata')");
            db.execSQL("INSERT INTO HasUlti VALUES (61, 'Bollaraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (61, 'Pantanobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (62, 'Idropompa')");
            db.execSQL("INSERT INTO HasUlti VALUES (62, 'Gelopugno')");
            db.execSQL("INSERT INTO HasUlti VALUES (62, 'Sottomissione')");
            db.execSQL("INSERT INTO HasUlti VALUES (63, 'Palla Ombra')");
            db.execSQL("INSERT INTO HasUlti VALUES (63, 'Segnoraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (63, 'Psicoshock')");
            db.execSQL("INSERT INTO HasUlti VALUES (64, 'Magibrillio')");
            db.execSQL("INSERT INTO HasUlti VALUES (64, 'Palla Ombra')");
            db.execSQL("INSERT INTO HasUlti VALUES (64, 'Psicoraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (65, 'Magibrillio')");
            db.execSQL("INSERT INTO HasUlti VALUES (65, 'Psichico')");
            db.execSQL("INSERT INTO HasUlti VALUES (65, 'Palla Ombra')");
            db.execSQL("INSERT INTO HasUlti VALUES (66, 'Incrocolpo')");
            db.execSQL("INSERT INTO HasUlti VALUES (66, 'Breccia')");
            db.execSQL("INSERT INTO HasUlti VALUES (66, 'Calciobasso')");
            db.execSQL("INSERT INTO HasUlti VALUES (67, 'Incrocolpo')");
            db.execSQL("INSERT INTO HasUlti VALUES (67, 'Breccia')");
            db.execSQL("INSERT INTO HasUlti VALUES (67, 'Sottomissione')");
            db.execSQL("INSERT INTO HasUlti VALUES (68, 'Pietrataglio')");
            db.execSQL("INSERT INTO HasUlti VALUES (68, 'Incrocolpo')");
            db.execSQL("INSERT INTO HasUlti VALUES (68, 'Sottomissione')");
            db.execSQL("INSERT INTO HasUlti VALUES (69, 'Vigorcolpo')");
            db.execSQL("INSERT INTO HasUlti VALUES (69, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (69, 'Avvolgibotta')");
            db.execSQL("INSERT INTO HasUlti VALUES (70, 'Vigorcolpo')");
            db.execSQL("INSERT INTO HasUlti VALUES (70, 'Semebomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (70, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (71, 'Solarraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (71, 'Fendifoglia')");
            db.execSQL("INSERT INTO HasUlti VALUES (71, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (72, 'Idropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (72, 'Bollaraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (72, 'Avvolgibotta')");
            db.execSQL("INSERT INTO HasUlti VALUES (73, 'Bora')");
            db.execSQL("INSERT INTO HasUlti VALUES (73, 'Idropompa')");
            db.execSQL("INSERT INTO HasUlti VALUES (73, 'Fangonda')");
            db.execSQL("INSERT INTO HasUlti VALUES (74, 'Fossa')");
            db.execSQL("INSERT INTO HasUlti VALUES (74, 'Frana')");
            db.execSQL("INSERT INTO HasUlti VALUES (74, 'Rocciotomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (75, 'Pietrataglio')");
            db.execSQL("INSERT INTO HasUlti VALUES (75, 'Frana')");
            db.execSQL("INSERT INTO HasUlti VALUES (75, 'Fossa')");
            db.execSQL("INSERT INTO HasUlti VALUES (76, 'Terremoto')");
            db.execSQL("INSERT INTO HasUlti VALUES (76, 'Pietrataglio')");
            db.execSQL("INSERT INTO HasUlti VALUES (76, 'Forzantica')");
            db.execSQL("INSERT INTO HasUlti VALUES (77, 'Fuocobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (77, 'Ruotafuoco')");
            db.execSQL("INSERT INTO HasUlti VALUES (77, 'Nitrocarica')");
            db.execSQL("INSERT INTO HasUlti VALUES (78, 'Fuocobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (78, 'Ondacalda')");
            db.execSQL("INSERT INTO HasUlti VALUES (78, 'Giravvita')");
            db.execSQL("INSERT INTO HasUlti VALUES (79, 'Psichico')");
            db.execSQL("INSERT INTO HasUlti VALUES (79, 'Psicoshock')");
            db.execSQL("INSERT INTO HasUlti VALUES (79, 'Idropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (80, 'Geloraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (80, 'Psichico')");
            db.execSQL("INSERT INTO HasUlti VALUES (80, 'Idropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (81, 'Fulmine')");
            db.execSQL("INSERT INTO HasUlti VALUES (81, 'Scarica')");
            db.execSQL("INSERT INTO HasUlti VALUES (81, 'Bombagnete')");
            db.execSQL("INSERT INTO HasUlti VALUES (82, 'Cannonflash')");
            db.execSQL("INSERT INTO HasUlti VALUES (82, 'Bombagnete')");
            db.execSQL("INSERT INTO HasUlti VALUES (82, 'Scarica')");
            db.execSQL("INSERT INTO HasUlti VALUES (83, 'Fendifoglia')");
            db.execSQL("INSERT INTO HasUlti VALUES (83, 'Aerasoio')");
            db.execSQL("INSERT INTO HasUlti VALUES (83, 'Aeroassalto')");
            db.execSQL("INSERT INTO HasUlti VALUES (84, 'Perforbecco')");
            db.execSQL("INSERT INTO HasUlti VALUES (84, 'Aeroassalto')");
            db.execSQL("INSERT INTO HasUlti VALUES (84, 'Comete')");
            db.execSQL("INSERT INTO HasUlti VALUES (85, 'Perforbecco')");
            db.execSQL("INSERT INTO HasUlti VALUES (85, 'Aeroassalto')");
            db.execSQL("INSERT INTO HasUlti VALUES (85, 'Aerasoio')");
            db.execSQL("INSERT INTO HasUlti VALUES (86, 'Idrondata')");
            db.execSQL("INSERT INTO HasUlti VALUES (86, 'Acquagetto')");
            db.execSQL("INSERT INTO HasUlti VALUES (86, 'Ventogelato')");
            db.execSQL("INSERT INTO HasUlti VALUES (87, 'Bora')");
            db.execSQL("INSERT INTO HasUlti VALUES (87, 'Ventogelto')");
            db.execSQL("INSERT INTO HasUlti VALUES (87, 'Acquagetto')");
            db.execSQL("INSERT INTO HasUlti VALUES (88, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (88, 'Fango')");
            db.execSQL("INSERT INTO HasUlti VALUES (88, 'Pantanobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (89, 'Fangonda')");
            db.execSQL("INSERT INTO HasUlti VALUES (89, 'Sporcolancio')");
            db.execSQL("INSERT INTO HasUlti VALUES (89, 'Neropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (90, 'Idropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (90, 'Bollaraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (90, 'Ventogelato')");
            db.execSQL("INSERT INTO HasUlti VALUES (91, 'Bora')");
            db.execSQL("INSERT INTO HasUlti VALUES (91, 'Ventogelato')");
            db.execSQL("INSERT INTO HasUlti VALUES (91, 'Idropompa')");
            db.execSQL("INSERT INTO HasUlti VALUES (92, 'Funestovento')");
            db.execSQL("INSERT INTO HasUlti VALUES (92, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (92, 'Neropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (93, 'Neropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (93, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (93, 'Palla Ombra')");
            db.execSQL("INSERT INTO HasUlti VALUES (94, 'Fangonda')");
            db.execSQL("INSERT INTO HasUlti VALUES (94, 'Neropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (94, 'Palla Ombra')");
            db.execSQL("INSERT INTO HasUlti VALUES (95, 'Pietrataglio')");
            db.execSQL("INSERT INTO HasUlti VALUES (95, 'Frana')");
            db.execSQL("INSERT INTO HasUlti VALUES (95, 'Metaltestata')");
            db.execSQL("INSERT INTO HasUlti VALUES (96, 'Psichico')");
            db.execSQL("INSERT INTO HasUlti VALUES (96, 'Psicoshock')");
            db.execSQL("INSERT INTO HasUlti VALUES (96, 'Psicoraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (97, 'Psichico')");
            db.execSQL("INSERT INTO HasUlti VALUES (97, 'Psicoshock')");
            db.execSQL("INSERT INTO HasUlti VALUES (97, 'Palla Ombra')");
            db.execSQL("INSERT INTO HasUlti VALUES (98, 'Idropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (98, 'Bollaraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (98, 'Presa')");
            db.execSQL("INSERT INTO HasUlti VALUES (99, 'Idropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (99, 'Forbice X')");
            db.execSQL("INSERT INTO HasUlti VALUES (99, 'Presa')");
            db.execSQL("INSERT INTO HasUlti VALUES (100, 'Fulmine')");
            db.execSQL("INSERT INTO HasUlti VALUES (100, 'Scarica')");
            db.execSQL("INSERT INTO HasUlti VALUES (100, 'Segnoraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (101, 'Iperraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (101, 'Fulmine')");
            db.execSQL("INSERT INTO HasUlti VALUES (101, 'Scarica')");
            db.execSQL("INSERT INTO HasUlti VALUES (102, 'Psichico')");
            db.execSQL("INSERT INTO HasUlti VALUES (102, 'Semebomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (102, 'Forzantica')");
            db.execSQL("INSERT INTO HasUlti VALUES (103, 'Solarraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (103, 'Semebomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (103, 'Psichico')");
            db.execSQL("INSERT INTO HasUlti VALUES (104, 'Fossa')");
            db.execSQL("INSERT INTO HasUlti VALUES (104, 'Battiterra')");
            db.execSQL("INSERT INTO HasUlti VALUES (104, 'Ossoclava')");
            db.execSQL("INSERT INTO HasUlti VALUES (105, 'Terremoto')");
            db.execSQL("INSERT INTO HasUlti VALUES (105, 'Fossa')");
            db.execSQL("INSERT INTO HasUlti VALUES (105, 'Ossoclava')");
            db.execSQL("INSERT INTO HasUlti VALUES (106, 'Pietrataglio')");
            db.execSQL("INSERT INTO HasUlti VALUES (106, 'Calciobasso')");
            db.execSQL("INSERT INTO HasUlti VALUES (106, 'Pestone')");
            db.execSQL("INSERT INTO HasUlti VALUES (107, 'Gelopugno')");
            db.execSQL("INSERT INTO HasUlti VALUES (107, 'Tuonopugno')");
            db.execSQL("INSERT INTO HasUlti VALUES (107, 'Fuocopugno')");
            db.execSQL("INSERT INTO HasUlti VALUES (107, 'Breccia')");
            db.execSQL("INSERT INTO HasUlti VALUES (108, 'Iperraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (108, 'Pestone')");
            db.execSQL("INSERT INTO HasUlti VALUES (108, 'Presa')");
            db.execSQL("INSERT INTO HasUlti VALUES (109, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (109, 'Fango')");
            db.execSQL("INSERT INTO HasUlti VALUES (109, 'Neropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (110, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (110, 'Neropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (110, 'Palla Ombra')");
            db.execSQL("INSERT INTO HasUlti VALUES (111, 'Battiterra')");
            db.execSQL("INSERT INTO HasUlti VALUES (111, 'Pestone')");
            db.execSQL("INSERT INTO HasUlti VALUES (111, 'Incornata')");
            db.execSQL("INSERT INTO HasUlti VALUES (112, 'Terremoto')");
            db.execSQL("INSERT INTO HasUlti VALUES (112, 'Pietrataglio')");
            db.execSQL("INSERT INTO HasUlti VALUES (112, 'Megacorno')");
            db.execSQL("INSERT INTO HasUlti VALUES (113, 'Psichico')");
            db.execSQL("INSERT INTO HasUlti VALUES (113, 'Psicoraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (113, 'Magibrillio')");
            db.execSQL("INSERT INTO HasUlti VALUES (114, 'Solarraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (114, 'Vigorcolpo')");
            db.execSQL("INSERT INTO HasUlti VALUES (114, 'Fangobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (115, 'Terremoto')");
            db.execSQL("INSERT INTO HasUlti VALUES (115, 'Breccia')");
            db.execSQL("INSERT INTO HasUlti VALUES (115, 'Pestone')");
            db.execSQL("INSERT INTO HasUlti VALUES (116, 'Dragopulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (116, 'Cannonflash')");
            db.execSQL("INSERT INTO HasUlti VALUES (116, 'Bollaraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (117, 'Bora')");
            db.execSQL("INSERT INTO HasUlti VALUES (117, 'Idropompa')");
            db.execSQL("INSERT INTO HasUlti VALUES (117, 'Dragoopulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (118, 'Idrondata')");
            db.execSQL("INSERT INTO HasUlti VALUES (118, 'Idropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (118, 'Incornata')");
            db.execSQL("INSERT INTO HasUlti VALUES (119, 'Megacorno')");
            db.execSQL("INSERT INTO HasUlti VALUES (119, 'Giravvita')");
            db.execSQL("INSERT INTO HasUlti VALUES (119, 'Ventogelato')");
            db.execSQL("INSERT INTO HasUlti VALUES (120, 'Gemmoforza')");
            db.execSQL("INSERT INTO HasUlti VALUES (120, 'Bollaraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (120, 'Comete')");
            db.execSQL("INSERT INTO HasUlti VALUES (121, 'Idropompa')");
            db.execSQL("INSERT INTO HasUlti VALUES (121, 'Gemmoforza')");
            db.execSQL("INSERT INTO HasUlti VALUES (121, 'Psicoraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (122, 'Psichico')");
            db.execSQL("INSERT INTO HasUlti VALUES (122, 'Psicoraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (122, 'Palla Ombra')");
            db.execSQL("INSERT INTO HasUlti VALUES (123, 'Ronzio')");
            db.execSQL("INSERT INTO HasUlti VALUES (123, 'Forbice X')");
            db.execSQL("INSERT INTO HasUlti VALUES (123, 'Nottesferza')");
            db.execSQL("INSERT INTO HasUlti VALUES (124, 'Gelopugno')");
            db.execSQL("INSERT INTO HasUlti VALUES (124, 'Psicoshock')");
            db.execSQL("INSERT INTO HasUlti VALUES (124, 'Assorbibacio')");
            db.execSQL("INSERT INTO HasUlti VALUES (125, 'Tuono')");
            db.execSQL("INSERT INTO HasUlti VALUES (125, 'Fulmine')");
            db.execSQL("INSERT INTO HasUlti VALUES (125, 'Tuonopugno')");
            db.execSQL("INSERT INTO HasUlti VALUES (126, 'Fuocobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (126, 'Lanciafiamme')");
            db.execSQL("INSERT INTO HasUlti VALUES (126, 'Fuocopugno')");
            db.execSQL("INSERT INTO HasUlti VALUES (127, 'Virgocolpo')");
            db.execSQL("INSERT INTO HasUlti VALUES (127, 'Forbice X')");
            db.execSQL("INSERT INTO HasUlti VALUES (127, 'Sottomissione')");
            db.execSQL("INSERT INTO HasUlti VALUES (128, 'Terremoto')");
            db.execSQL("INSERT INTO HasUlti VALUES (128, 'Metaltestata')");
            db.execSQL("INSERT INTO HasUlti VALUES (128, 'Incornata')");
            db.execSQL("INSERT INTO HasUlti VALUES (129, 'Scontro')");
            db.execSQL("INSERT INTO HasUlti VALUES (130, 'Idropompa')");
            db.execSQL("INSERT INTO HasUlti VALUES (130, 'Dragopulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (130, 'Tornado')");
            db.execSQL("INSERT INTO HasUlti VALUES (131, 'Bora')");
            db.execSQL("INSERT INTO HasUlti VALUES (131, 'Geloraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (131, 'Dragopulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (133, 'Fossa')");
            db.execSQL("INSERT INTO HasUlti VALUES (133, 'Corposcontro')");
            db.execSQL("INSERT INTO HasUlti VALUES (133, 'Comete')");
            db.execSQL("INSERT INTO HasUlti VALUES (134, 'Idropompa')");
            db.execSQL("INSERT INTO HasUlti VALUES (134, 'Idrondata')");
            db.execSQL("INSERT INTO HasUlti VALUES (134, 'Idropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (135, 'Tuono')");
            db.execSQL("INSERT INTO HasUlti VALUES (135, 'Fulmine')");
            db.execSQL("INSERT INTO HasUlti VALUES (135, 'Scarica')");
            db.execSQL("INSERT INTO HasUlti VALUES (136, 'Fuocobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (136, 'Ondacalda')");
            db.execSQL("INSERT INTO HasUlti VALUES (136, 'Lanciafiamme')");
            db.execSQL("INSERT INTO HasUlti VALUES (137, 'Segnoraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (137, 'Psicoraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (137, 'Scarica')");
            db.execSQL("INSERT INTO HasUlti VALUES (138, 'Forzantica')");
            db.execSQL("INSERT INTO HasUlti VALUES (138, 'Rocciotomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (138, 'Acquadisale')");
            db.execSQL("INSERT INTO HasUlti VALUES (139, 'Idropompa')");
            db.execSQL("INSERT INTO HasUlti VALUES (139, 'Frana')");
            db.execSQL("INSERT INTO HasUlti VALUES (139, 'Forzantica')");
            db.execSQL("INSERT INTO HasUlti VALUES (140, 'Forzantica')");
            db.execSQL("INSERT INTO HasUlti VALUES (140, 'Rocciotomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (140, 'Acquagetto')");
            db.execSQL("INSERT INTO HasUlti VALUES (141, 'Pietrataglio')");
            db.execSQL("INSERT INTO HasUlti VALUES (141, 'Forzantica')");
            db.execSQL("INSERT INTO HasUlti VALUES (141, 'Idropulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (142, 'Iperraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (142, 'Forzantica')");
            db.execSQL("INSERT INTO HasUlti VALUES (142, 'Metaltestata')");
            db.execSQL("INSERT INTO HasUlti VALUES (143, 'Iperraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (143, 'Corposcontro')");
            db.execSQL("INSERT INTO HasUlti VALUES (143, 'Terremoto')");
            db.execSQL("INSERT INTO HasUlti VALUES (144, 'Bora')");
            db.execSQL("INSERT INTO HasUlti VALUES (144, 'Geloraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (144, 'Ventogelato')");
            db.execSQL("INSERT INTO HasUlti VALUES (145, 'Tuono')");
            db.execSQL("INSERT INTO HasUlti VALUES (145, 'Fulmine')");
            db.execSQL("INSERT INTO HasUlti VALUES (145, 'Scarica')");
            db.execSQL("INSERT INTO HasUlti VALUES (146, 'Fuocobomba')");
            db.execSQL("INSERT INTO HasUlti VALUES (146, 'Ondacalda')");
            db.execSQL("INSERT INTO HasUlti VALUES (146, 'Lanciafiamme')");
            db.execSQL("INSERT INTO HasUlti VALUES (147, 'Idrondata')");
            db.execSQL("INSERT INTO HasUlti VALUES (147, 'Avvolgibotta')");
            db.execSQL("INSERT INTO HasUlti VALUES (147, 'Tornado')");
            db.execSQL("INSERT INTO HasUlti VALUES (148, 'Dragopulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (149, 'Idrondata')");
            db.execSQL("INSERT INTO HasUlti VALUES (148, 'Avvolgibotta')");
            db.execSQL("INSERT INTO HasUlti VALUES (149, 'Iperraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (149, 'Dragopulsar')");
            db.execSQL("INSERT INTO HasUlti VALUES (149, 'Dragartigli')");
            db.execSQL("INSERT INTO HasUlti VALUES (150, 'Iperraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (150, 'Psicobotta')");
            db.execSQL("INSERT INTO HasUlti VALUES (150, 'Psichico')");
            db.execSQL("INSERT INTO HasUlti VALUES (150, 'Palla Ombra')");
            db.execSQL("INSERT INTO HasUlti VALUES (151, 'Iperraggio')");
            db.execSQL("INSERT INTO HasUlti VALUES (151, 'Forza Lunare')");
            db.execSQL("INSERT INTO HasUlti VALUES (151, 'Psichico')");


            Toast.makeText(context, "Creazione del Database\neseguita correttamente", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            /*db = this.getWritableDatabase();
            context.deleteDatabase(DATABASE_NAME);*/
            db.execSQL("DROP TABLE IF EXISTS " + SETTINGS);
            db.execSQL("DROP TABLE IF EXISTS " + HASATTACK);
            db.execSQL("DROP TABLE IF EXISTS " + HASULTI);
            db.execSQL("DROP TABLE IF EXISTS " + HASSTRENGHT);
            db.execSQL("DROP TABLE IF EXISTS " + HASWEAKNESS);
            db.execSQL("DROP TABLE IF EXISTS " + ATTACK);
            db.execSQL("DROP TABLE IF EXISTS " + ULTI);
            db.execSQL("DROP TABLE IF EXISTS " + TYPE);
            db.execSQL("DROP TABLE IF EXISTS " + POKEMON);
            db.execSQL("DROP TABLE IF EXISTS " + CATCHES);
            db.execSQL("DROP TABLE IF EXISTS " + HASTYPE);
            db.execSQL("DROP TABLE IF EXISTS " + COPY);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_SHORT).show();
            onCreate(db);
        }
    }
}