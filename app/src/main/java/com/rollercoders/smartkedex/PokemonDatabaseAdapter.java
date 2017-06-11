package com.rollercoders.smartkedex;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by TheFe on 19/10/2016.
 */

class PokemonDatabaseAdapter implements WebServicesAsyncResponse {

    private PokemonHelper helper;
    private BackgroundWorker backgroundWorker;

    PokemonDatabaseAdapter(Context context) {
        //context.deleteDatabase("PokemonDatabase.db"); //PERICOLOSISSIMA!!! USARE CON CAUTELA ESTREMA
        helper = new PokemonHelper(context);
    }

    ////////////////////////////////////////////////////////////////////INSERTS////////////////////////////////////////////////////////////////////////////////////

    void insertSettingsData(String owner, String smartkedex, int pokemonGO) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Settings SET Owner = '"+owner+"', SmartkedexName = '"+smartkedex+"', PokemonGO = '"+pokemonGO+"'");
        db.close();
    }

    String registration (String email, String username, String password, int appversion) {
        backgroundWorker = new BackgroundWorker("registration", email, username, password, appversion);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        String result = "";
        try {
            result = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    void insertCatches (int pokeID) {
        backgroundWorker = new BackgroundWorker("insertCatches", pokeID, getLocalUsername());
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
//        try {
//            String error = backgroundWorker.get();
//            System.err.println(error);
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
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

    void setRememberME (String username, String password) {
        if (getLocalRows("Settings") > 1) {
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("DELETE FROM Settings");
            db.close();
        }
        if (getLocalRows("Settings") == 0) {
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("INSERT INTO Settings (Username, Password, RememberME) VALUES ('" + username + "', '" + password + "', 1)");
            db.close();
        }
        else if (getLocalRows("Settings") == 1) {
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("UPDATE Settings SET Username = '" + username + "', Password = '" + password + "', RememberME = 1");
            db.close();
        }
    }

    void setNotRememberME (String username, String password) {
        if (getLocalRows("Settings") > 1) {
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("DELETE FROM Settings");
            db.close();
        }
        if (getLocalRows("Settings") == 0) {
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("INSERT INTO Settings (Username, Password, RememberME) VALUES ('" + username + "', '" + password + "', 0)");
            db.close();
        }
        else if (getLocalRows("Settings") == 1) {
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("UPDATE Settings SET Username = '" + username + "', Password = '" + password + "', RememberME = 0");
            db.close();
        }
    }

    void erase (String username) {
        backgroundWorker = new BackgroundWorker("erase", username);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
    }

    void localErase () {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM Settings");
        db.close();
    }

    ////////////////////////////////////////////////////////////////////GETTERS////////////////////////////////////////////////////////////////////////////////////

    boolean getAppVersion () {
        backgroundWorker = new BackgroundWorker("getAppVersion", getLocalUsername());
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        String result = "";
        try {
            result = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String[] cleaner = result.split("\n");
        return cleaner[0].equals("1");
    }

    String getLocalUsername () {
        String username = "";
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {"Username"};
        Cursor cursor = db.query(PokemonHelper.SETTINGS, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            username = cursor.getString(0); //va bene 0 perchè io seleziono SEMPRE una colonna alla volta, quindi ha per forza indice 0
        }

        db.close();
        return username;
    }

    int getRememberME () {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {"RememberME"};
        Cursor cursor = db.query(PokemonHelper.SETTINGS, columns, null, null, null, null, null);
        int rm = 0;
        while (cursor.moveToNext()) {
            rm = cursor.getInt(0);
        }
        db.close();
        return rm;
    }

    String[] getLoginData() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {"Username", "Password"};
        Cursor cursor = db.query(PokemonHelper.SETTINGS, columns, null, null, null, null, null);
        String[] data = {"", ""};
        while (cursor.moveToNext()) {
            data[0] = cursor.getString(0);
            data[1] = cursor.getString(1);
        }
        db.close();
        return data;
    }

    int tryLogin (String username, String password) {
        int result;
        backgroundWorker = new BackgroundWorker("login", username, password);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        String temp = "";
        try {
            temp = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String[] correct = temp.split("\n");
        result = Integer.parseInt(correct[0]);
        return result;
    }

    private int getLocalRows (String tableName) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        int rows = 0;
        while (cursor.moveToNext()) {
            rows++;
        }

        db.close();
        return rows;
    }

    int getRows (String tableName) {
        int rows = 0;
        backgroundWorker = new BackgroundWorker("getRows", tableName);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        try {
            rows = Integer.parseInt(backgroundWorker.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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
        List<String> types = new ArrayList<>();
        backgroundWorker = new BackgroundWorker("getPokeTypes", pokeID);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        String temp = "";
        try {
            temp = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String[] division = temp.split("\n");
        Collections.addAll(types, division);
        return types;
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
        String temp = "";
        backgroundWorker = new BackgroundWorker("getCatched", pokeID, getLocalUsername());
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        try {
            temp = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException | NullPointerException e) {
            e.printStackTrace();
        }
        String[] cleaner = temp.split("\n");
        int rows = Integer.parseInt(cleaner[0]);
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

    List<String> getWeaknesses (int pokeID) {
        List<String> weaknesses = new ArrayList<>();
        backgroundWorker = new BackgroundWorker("getWeakness", pokeID);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        String temp = "";
        try {
            temp = backgroundWorker.get();
            System.err.println(temp);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String[] division = temp.split("\n");
        Collections.addAll(weaknesses, division);
        return weaknesses;
    }

    List<String> getStrenghts (int pokeID) {
        List<String> strengths = new ArrayList<>();
        backgroundWorker = new BackgroundWorker("getStrenghts", pokeID);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        String temp = "";
        try {
            temp = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String[] division = temp.split("\n");
        Collections.addAll(strengths, division);
        return strengths;
    }

    int getTotalCatches () {
        int totalCatches;
        String temp = "";
        backgroundWorker = new BackgroundWorker("getConditionedRows", "Catches", getLocalUsername());
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        try {
            temp = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String[] cleaner = temp.split("\n");
        totalCatches = Integer.parseInt(cleaner[0]);
        return totalCatches;
    }

    int getTotalCopies () {
        int totalCopies;
        String temp = "";
        backgroundWorker = new BackgroundWorker("getTotalCopies", getLocalUsername());
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        try {
            temp = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String[] cleaner = temp.split("\n");
        totalCopies = Integer.parseInt(cleaner[0]);
        return totalCopies;
    }

    ////////////////////////////////////////////////////////////////////UPDATERS////////////////////////////////////////////////////////////////////////////////////

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

    void updatePokeAttack (String attack, int pokeID) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Copy SET AttackName = '"+attack+"' WHERE ID = "+pokeID);
        db.close();
    }

    void updatePokeUlti (String ulti, int pokeID) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Copy SET UltiName = '"+ulti+"' WHERE ID = "+pokeID);
        db.close();
    }

    void updatePokeName (String name, int pokeID) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Copy SET PokemonName = '"+name+"' WHERE ID = "+pokeID);
        db.close();
    }

    void updateDisclaimer () {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Disclaimer_OK SET Disclaimer_OK = 1");
        db.close();
    }

    @Override
    public void processFinish(String output) {
        backgroundWorker.delegate = this;
    }

    private static class PokemonHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "PokemonDatabase.db";
        private static final int DATABASE_VERSION = 14;

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
        private static final String DISCLAIMER_OK = "Disclaimer_OK";



        //CREATE TABLE Statements
        private static final String CREATE_SETTINGS = "CREATE TABLE IF NOT EXISTS " + SETTINGS + "(" +
                                                       "Username VARCHAR(20) PRIMARY KEY, " +
                                                       "Password VARCHAR(20), " +
                                                       "RememberME INT(1), " +
                                                       OWNER + VARCHAR + "20), " +
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

        private static final String CREATE_DISCLAIMER_TABLE = "CREATE TABLE IF NOT EXISTS " + DISCLAIMER_OK + "(" + DISCLAIMER_OK + VARCHAR + "1) PRIMARY KEY)";

        private Context context;

        PokemonHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_SETTINGS);
            /*db.execSQL(CREATE_TYPE);
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
            db.execSQL(CREATE_DISCLAIMER_TABLE);

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
            db.execSQL("INSERT INTO HasType VALUES (122, 'Folletto')");
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
            db.execSQL("INSERT INTO HasAttack VALUES (37, 'Attacco Rapido')");
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

            //Populating HasStrenght
            db.execSQL("INSERT INTO HasStrenght VALUES (1, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (1, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (1, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (1, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (2, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (2, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (2, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (2, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (3, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (3, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (3, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (3, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (4, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (4, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (4, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (5, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (5, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (5, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (6, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (6, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (6, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (6, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (7, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (7, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (7, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (8, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (8, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (8, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (9, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (9, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (9, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (10, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (10, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (10, 'Psico')");
            db.execSQL("INSERT INTO HasStrenght VALUES (11, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (11, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (11, 'Psico')");
            db.execSQL("INSERT INTO HasStrenght VALUES (12, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (12, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (12, 'Psico')");
            db.execSQL("INSERT INTO HasStrenght VALUES (14, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (14, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (14, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (15, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (15, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (15, 'Psico')");
            db.execSQL("INSERT INTO HasStrenght VALUES (15, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (16, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (16, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (16, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (17, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (17, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (17, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (18, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (18, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (18, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (21, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (21, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (21, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (22, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (22, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (22, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (23, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (23, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (24, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (24, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (25, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (25, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (26, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (26, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (27, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (27, 'Elettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (27, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (27, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (28, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (28, 'Elettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (28, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (28, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (29, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (26, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (30, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (30, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (31, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (31, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (31, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (31, 'Elettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (32, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (32, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (33, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (33, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (34, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (34, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (34, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (34, 'Elettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (35, 'Drago')");
            db.execSQL("INSERT INTO HasStrenght VALUES (35, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (35, 'Buio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (36, 'Drago')");
            db.execSQL("INSERT INTO HasStrenght VALUES (36, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (36, 'Buio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (37, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (37, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (37, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (38, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (38, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (38, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (39, 'Drago')");
            db.execSQL("INSERT INTO HasStrenght VALUES (39, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (39, 'Buio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (40, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (40, 'Drago')");
            db.execSQL("INSERT INTO HasStrenght VALUES (40, 'Buio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (41, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (41, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (41, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (42, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (42, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (42, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (43, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (43, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (43, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (44, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (44, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (44, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (45, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (45, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (45, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (46, 'Psico')");
            db.execSQL("INSERT INTO HasStrenght VALUES (46, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (46, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (46, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (47, 'Psico')");
            db.execSQL("INSERT INTO HasStrenght VALUES (47, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (47, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (47, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (48, 'Psico')");
            db.execSQL("INSERT INTO HasStrenght VALUES (48, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (48, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (48, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (49, 'Psico')");
            db.execSQL("INSERT INTO HasStrenght VALUES (49, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (49, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (49, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (50, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (50, 'Elettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (50, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (50, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (51, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (51, 'Elettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (51, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (51, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (54, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (54, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (54, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (55, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (55, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (55, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (56, 'Normale')");
            db.execSQL("INSERT INTO HasStrenght VALUES (56, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (56, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (57, 'Normale')");
            db.execSQL("INSERT INTO HasStrenght VALUES (57, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (57, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (58, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (58, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (58, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (59, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (59, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (59, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (60, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (60, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (60, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (61, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (61, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (61, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (62, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (62, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (62, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (62, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (63, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (63, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (64, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (64, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (65, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (65, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (66, 'Normale')");
            db.execSQL("INSERT INTO HasStrenght VALUES (66, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (66, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (67, 'Normale')");
            db.execSQL("INSERT INTO HasStrenght VALUES (67, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (67, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (68, 'Normale')");
            db.execSQL("INSERT INTO HasStrenght VALUES (68, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (68, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (69, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (69, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (69, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (69, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (70, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (70, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (70, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (70, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (71, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (71, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (71, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (71, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (72, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (72, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (72, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (72, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (73, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (73, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (73, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (73, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (74, 'Elettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (74, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (74, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (74, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (75, 'Elettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (75, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (75, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (75, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (76, 'Elettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (76, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (76, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (76, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (77, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (77, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (77, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (78, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (78, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (78, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (79, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (79, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (79, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (79, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (80, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (80, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (80, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (80, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (81, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (81, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (82, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (82, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (83, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (83, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (83, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (84, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (84, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (84, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (85, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (85, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (85, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (86, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (86, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (86, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (87, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (87, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (87, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (87, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (88, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (88, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (89, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (89, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (90, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (90, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (90, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (91, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (91, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (91, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (91, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (92, 'Spettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (92, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (92, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (93, 'Spettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (93, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (93, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (94, 'Spettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (94, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (94, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (95, 'Elettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (95, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (95, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (95, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (96, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (96, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (97, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (97, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (98, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (98, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (98, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (99, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (99, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (99, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (100, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (100, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (101, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (101, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (102, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (102, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (102, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (102, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (103, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (103, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (103, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (103, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (104, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (104, 'Elettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (104, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (104, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (105, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (105, 'Elettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (105, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (105, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (106, 'Normale')");
            db.execSQL("INSERT INTO HasStrenght VALUES (106, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (106, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (107, 'Normale')");
            db.execSQL("INSERT INTO HasStrenght VALUES (107, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (107, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (109, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (109, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (110, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (110, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (111, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (111, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (111, 'Elettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (111, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (112, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (112, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (112, 'Elettro')");
            db.execSQL("INSERT INTO HasStrenght VALUES (112, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (114, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (114, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (114, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (116, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (116, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (116, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (117, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (117, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (117, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (118, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (118, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (118, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (119, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (119, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (119, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (120, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (120, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (120, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (121, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (121, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (121, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (121, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (122, 'Drago')");
            db.execSQL("INSERT INTO HasStrenght VALUES (122, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (122, 'Buio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (122, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (123, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (123, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (123, 'Psico')");
            db.execSQL("INSERT INTO HasStrenght VALUES (123, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (124, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (124, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (124, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (124, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (125, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (125, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (126, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (126, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (126, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (127, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (127, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (127, 'Psico')");
            db.execSQL("INSERT INTO HasStrenght VALUES (129, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (129, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (129, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (130, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (130, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (130, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (130, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (131, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (131, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (131, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (131, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (134, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (134, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (134, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (135, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (135, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (136, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (136, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (136, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (138, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (138, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (138, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (138, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (139, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (139, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (139, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (139, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (140, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (140, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (140, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (140, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (141, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (141, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (141, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (141, 'Roccia')");
            db.execSQL("INSERT INTO HasStrenght VALUES (142, 'Fuoco')");
            db.execSQL("INSERT INTO HasStrenght VALUES (142, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (142, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (142, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (144, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (144, 'Terra')");
            db.execSQL("INSERT INTO HasStrenght VALUES (144, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (144, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (145, 'Acqua')");
            db.execSQL("INSERT INTO HasStrenght VALUES (145, 'Volante')");
            db.execSQL("INSERT INTO HasStrenght VALUES (145, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (145, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (146, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (146, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasStrenght VALUES (146, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (146, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (147, 'Drago')");
            db.execSQL("INSERT INTO HasStrenght VALUES (148, 'Drago')");
            db.execSQL("INSERT INTO HasStrenght VALUES (149, 'Drago')");
            db.execSQL("INSERT INTO HasStrenght VALUES (149, 'Erba')");
            db.execSQL("INSERT INTO HasStrenght VALUES (149, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (149, 'Coleottero')");
            db.execSQL("INSERT INTO HasStrenght VALUES (150, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (150, 'Veleno')");
            db.execSQL("INSERT INTO HasStrenght VALUES (151, 'Lotta')");
            db.execSQL("INSERT INTO HasStrenght VALUES (151, 'Veleno')");

            //Populating HasWeakness
            db.execSQL("INSERT INTO HasWeakness VALUES (1, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (1, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (1, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (1, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (2, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (2, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (2, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (2, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (3, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (3, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (3, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (3, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (4, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (4, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (4, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (5, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (5, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (5, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (6, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (6, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (6, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (7, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (7, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (8, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (8, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (9, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (9, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (10, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (10, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (10, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (11, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (11, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (11, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (12, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (12, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (12, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (12, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (13, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (13, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (13, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (13, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (14, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (14, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (14, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (14, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (15, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (15, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (15, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (15, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (16, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (16, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (16, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (17, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (17, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (17, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (18, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (18, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (18, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (19, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (20, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (21, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (21, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (21, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (22, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (22, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (22, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (23, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (23, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (24, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (24, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (25, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (26, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (27, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (27, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (27, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (28, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (28, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (28, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (29, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (29, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (30, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (30, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (31, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (31, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (31, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (31, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (32, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (32, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (33, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (33, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (34, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (34, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (34, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (34, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (35, 'Acciaio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (35, 'Veleno')");
            db.execSQL("INSERT INTO HasWeakness VALUES (36, 'Acciaio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (36, 'Veleno')");
            db.execSQL("INSERT INTO HasWeakness VALUES (37, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (37, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (37, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (38, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (38, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (38, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (39, 'Acciaio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (39, 'Veleno')");
            db.execSQL("INSERT INTO HasWeakness VALUES (40, 'Acciaio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (40, 'Veleno')");
            db.execSQL("INSERT INTO HasWeakness VALUES (41, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (41, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (41, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (41, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (42, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (42, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (42, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (42, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (43, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (43, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (43, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (43, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (44, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (44, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (44, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (44, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (45, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (45, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (45, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (45, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (46, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (46, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (46, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (46, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (47, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (47, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (47, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (47, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (48, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (48, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (48, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (48, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (49, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (49, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (49, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (49, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (50, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (50, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (50, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (51, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (51, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (51, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (52, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (53, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (54, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (54, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (55, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (55, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (56, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (56, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (56, 'Folletto')");
            db.execSQL("INSERT INTO HasWeakness VALUES (57, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (57, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (57, 'Folletto')");
            db.execSQL("INSERT INTO HasWeakness VALUES (58, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (58, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (58, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (59, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (59, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (59, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (60, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (60, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (61, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (61, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (62, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (62, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (62, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (62, 'Folletto')");
            db.execSQL("INSERT INTO HasWeakness VALUES (63, 'Coleottero')");
            db.execSQL("INSERT INTO HasWeakness VALUES (63, 'Buio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (63, 'Spettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (64, 'Coleottero')");
            db.execSQL("INSERT INTO HasWeakness VALUES (64, 'Buio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (64, 'Spettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (65, 'Coleottero')");
            db.execSQL("INSERT INTO HasWeakness VALUES (65, 'Buio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (65, 'Spettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (66, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (66, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (66, 'Folletto')");
            db.execSQL("INSERT INTO HasWeakness VALUES (67, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (67, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (67, 'Folletto')");
            db.execSQL("INSERT INTO HasWeakness VALUES (68, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (68, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (68, 'Folletto')");
            db.execSQL("INSERT INTO HasWeakness VALUES (69, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (69, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (69, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (69, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (70, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (70, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (70, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (70, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (71, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (71, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (71, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (71, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (72, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (72, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (72, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (73, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (73, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (73, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (74, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (74, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (74, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (74, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (75, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (75, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (75, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (75, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (76, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (76, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (76, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (76, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (77, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (77, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (77, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (78, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (78, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (78, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (79, 'Coleottero')");
            db.execSQL("INSERT INTO HasWeakness VALUES (79, 'Buio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (79, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (79, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (80, 'Coleottero')");
            db.execSQL("INSERT INTO HasWeakness VALUES (80, 'Buio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (80, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (80, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (81, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (81, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (81, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (82, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (82, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (82, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (83, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (83, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (83, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (84, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (84, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (84, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (85, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (85, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (85, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (86, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (86, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (87, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (87, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (87, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (87, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (88, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (88, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (89, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (89, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (90, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (90, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (91, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (91, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (91, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (91, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (92, 'Buio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (92, 'Spettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (92, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (93, 'Buio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (93, 'Spettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (93, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (94, 'Buio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (94, 'Spettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (94, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (95, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (95, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (95, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (95, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (96, 'Coleottero')");
            db.execSQL("INSERT INTO HasWeakness VALUES (96, 'Buio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (96, 'Spettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (97, 'Coleottero')");
            db.execSQL("INSERT INTO HasWeakness VALUES (97, 'Buio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (97, 'Spettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (98, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (98, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (99, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (99, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (100, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (101, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (102, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (102, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (102, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (102, 'Veleno')");
            db.execSQL("INSERT INTO HasWeakness VALUES (103, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (103, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (103, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (103, 'Veleno')");
            db.execSQL("INSERT INTO HasWeakness VALUES (104, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (104, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (104, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (105, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (105, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (105, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (106, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (106, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (106, 'Folletto')");
            db.execSQL("INSERT INTO HasWeakness VALUES (107, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (107, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (107, 'Folletto')");
            db.execSQL("INSERT INTO HasWeakness VALUES (108, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (109, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (110, 'Psico')");
            db.execSQL("INSERT INTO HasWeakness VALUES (111, 'Acciaio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (111, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (111, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (111, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (112, 'Acciaio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (112, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (112, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (112, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (113, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (114, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (114, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (114, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (114, 'Coleottero')");
            db.execSQL("INSERT INTO HasWeakness VALUES (115, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (116, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (116, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (117, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (117, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (118, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (118, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (119, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (119, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (120, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (120, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (121, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (121, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (121, 'Coleottero')");
            db.execSQL("INSERT INTO HasWeakness VALUES (121, 'Buio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (122, 'Spettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (122, 'Acciaio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (122, 'Veleno')");
            db.execSQL("INSERT INTO HasWeakness VALUES (123, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (123, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (123, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (123, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (124, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (124, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (124, 'Buio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (124, 'Spettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (125, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (126, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (126, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (126, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (127, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (127, 'Volante')");
            db.execSQL("INSERT INTO HasWeakness VALUES (127, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (128, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (129, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (129, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (130, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (130, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (131, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (131, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (131, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (131, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (132, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (133, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (134, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (134, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (135, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (136, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (136, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (136, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (137, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (138, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (138, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (138, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (138, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (139, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (139, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (139, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (139, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (140, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (140, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (140, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (140, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (141, 'Erba')");
            db.execSQL("INSERT INTO HasWeakness VALUES (141, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (141, 'Terra')");
            db.execSQL("INSERT INTO HasWeakness VALUES (141, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (142, 'Acciaio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (142, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (142, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (142, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (143, 'Lotta')");
            db.execSQL("INSERT INTO HasWeakness VALUES (144, 'Acciaio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (144, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (144, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (144, 'Fuoco')");
            db.execSQL("INSERT INTO HasWeakness VALUES (145, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (145, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (146, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (146, 'Elettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (146, 'Acqua')");
            db.execSQL("INSERT INTO HasWeakness VALUES (147, 'Drago')");
            db.execSQL("INSERT INTO HasWeakness VALUES (147, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (147, 'Folletto')");
            db.execSQL("INSERT INTO HasWeakness VALUES (148, 'Drago')");
            db.execSQL("INSERT INTO HasWeakness VALUES (148, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (148, 'Folletto')");
            db.execSQL("INSERT INTO HasWeakness VALUES (149, 'Drago')");
            db.execSQL("INSERT INTO HasWeakness VALUES (149, 'Ghiaccio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (149, 'Folletto')");
            db.execSQL("INSERT INTO HasWeakness VALUES (149, 'Roccia')");
            db.execSQL("INSERT INTO HasWeakness VALUES (150, 'Coleottero')");
            db.execSQL("INSERT INTO HasWeakness VALUES (150, 'Buio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (150, 'Spettro')");
            db.execSQL("INSERT INTO HasWeakness VALUES (151, 'Coleottero')");
            db.execSQL("INSERT INTO HasWeakness VALUES (151, 'Buio')");
            db.execSQL("INSERT INTO HasWeakness VALUES (151, 'Spettro')");

            db.execSQL("INSERT INTO "+DISCLAIMER_OK+" VALUES (\"0\")");*/

            Toast.makeText(context, "Creazione del Database\neseguita correttamente", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE Settings");
            db.execSQL(CREATE_SETTINGS);
        }
    }
}