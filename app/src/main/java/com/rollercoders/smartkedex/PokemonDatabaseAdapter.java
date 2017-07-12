package com.rollercoders.smartkedex;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ProgressBar;
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

    String registration (String email, String username, String password, int appversion, Context context) {
        backgroundWorker = new BackgroundWorker("registration_big", email, username, password, appversion, context);
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

    void insertCatches (int pokeID, Context context) {
        backgroundWorker = new BackgroundWorker("insertCatches", pokeID, getLocalUsername(), context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
    }

    void insertCopy (String attackName, String ultiName, String pokeName, int pokeID, int ivmin, int ivmax, Context context) {
        backgroundWorker = new BackgroundWorker("insertCopy", pokeID, ivmin, ivmax, attackName, ultiName, pokeName, getLocalUsername(), context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
    }

    void deleteCopy (int pokeId, Context context) {
        backgroundWorker = new BackgroundWorker("deleteCopy", pokeId, context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
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

    void doLogout () {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Settings SET RememberME = 0");
        db.close();
    }

    void resetRememberME (String username) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE Settings SET RememberME = 0 WHERE Username = '"+username+"'");
        db.close();
    }

    void erase (String username, Context context) {
        backgroundWorker = new BackgroundWorker("erase", username, context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
    }

    void localErase () {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM Settings");
        db.close();
    }

    void delete (int pokeID, Context context) {
        backgroundWorker = new BackgroundWorker("remove", pokeID, getLocalUsername(), context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
    }

    void doBackupAndUpdateDB (Context context) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {"ID"};
        Cursor cursor = db.query("Catches", columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            insertCatches(cursor.getInt(0), context);
        }
        db.close();

        db = helper.getReadableDatabase();
        String[] columnsCopy = {"AttackName", "UltiName", "PokemonName", "PokemonID"};
        cursor = db.query("Copy", columnsCopy, null, null, null, null, null);
        while (cursor.moveToNext()) {
            insertCopy(cursor.getString(cursor.getColumnIndex("AttackName")), cursor.getString(cursor.getColumnIndex("UltiName")), cursor.getString(cursor.getColumnIndex("PokemonName")), cursor.getInt(cursor.getColumnIndex("PokemonID")), -1, -1, context);
        }
        db.close();

        db = helper.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS Copy");
        db.execSQL("DROP TABLE IF EXISTS Catches");
        db.execSQL("DROP TABLE IF EXISTS HasAttack");
        db.execSQL("DROP TABLE IF EXISTS HasUlti");
        db.execSQL("DROP TABLE IF EXISTS HasWeakness");
        db.execSQL("DROP TABLE IF EXISTS HasType");
        db.execSQL("DROP TABLE IF EXISTS HasStrenght");
        db.execSQL("DROP TABLE IF EXISTS Pokemon");
        db.execSQL("DROP TABLE IF EXISTS Attack");
        db.execSQL("DROP TABLE IF EXISTS Ulti");
        db.execSQL("DROP TABLE IF EXISTS Type");
        db.execSQL("DROP TABLE IF EXISTS Disclaimer_OK");

        db.close();

        Toast.makeText(context, "Backup Eseguito Correttamente", Toast.LENGTH_SHORT).show();
    }

    ////////////////////////////////////////////////////////////////////GETTERS////////////////////////////////////////////////////////////////////////////////////

    boolean getAppVersion (int appVersion, Context context) {
        backgroundWorker = new BackgroundWorker("getAppVersion", appVersion, context);
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

    boolean getNumberOfTables () {
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "SELECT * FROM sqlite_master WHERE type = 'table' AND name != 'android_metadata' AND name != 'sqlite_sequence'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount() == 1;
    }

    int[] getAllCatched (Context context) {
        backgroundWorker = new BackgroundWorker("getAllCatched", getLocalUsername(), context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        String tmp = "";
        try {
            tmp = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        int[] allCatched = {};
        List<Integer> tmpAllCatched = new ArrayList<>();
        String[] cleaner;
        if (tmp.equals("\n")) {
            allCatched[0] = 0;
        }
        else {
            cleaner = tmp.split("\n");
            for (String aCleaner : cleaner)
                tmpAllCatched.add(Integer.parseInt(aCleaner));
            allCatched = new int[tmpAllCatched.size()];
            for (int i =0; i< tmpAllCatched.size(); i++) {
                allCatched[i] = tmpAllCatched.get(i);
            }
        }
        return allCatched;
    }

    String getPokeName(int pokeID, Context context) {
        backgroundWorker = new BackgroundWorker("getPokeName", pokeID, context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        String tmp = "";
        try {
            tmp = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String[] cleaner = tmp.split("\n");
        return cleaner[0];
    }

    String getIVs (int pokeID, Context context) {
        backgroundWorker = new BackgroundWorker("getPokeIVs", pokeID, context);
        backgroundWorker.delegate=this;
        backgroundWorker.execute();
        String tmp = "";
        try {
            tmp = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String[] cleaner = tmp.split("\n");
        return cleaner[0]+"%-"+cleaner[1]+"%";
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

    int tryLogin (String username, String password, Context context) {
        int result;
        backgroundWorker = new BackgroundWorker("login", username, password, context);
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

    String getCopyName (int copyID, Context context) {
        backgroundWorker = new BackgroundWorker("getCopyName", copyID, context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        String result = "";
        try {
            result = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String[] cleaner;
        if (!result.equals("\n")) {
            cleaner = result.split("\n");
            return cleaner[0];
        }
        else
            return "";
    }

    String getMovesType (String name, String type, Context context) {
        backgroundWorker = new BackgroundWorker("getMovesType", name, type, context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        String temp = "";
        try {
            temp = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String[] cleaner = temp.split("\n");
        return cleaner[0];
    }

    List<String> getPokeTypes(int pokeID, Context context) {
        List<String> types = new ArrayList<>();
        backgroundWorker = new BackgroundWorker("getPokeTypes", pokeID, context);
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

    Map<String, String> getAttacksStuff(String name, String table, Context context) {
        backgroundWorker = new BackgroundWorker("getAttacksStuff", name, table, context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        Map<String, String> map = new HashMap<>();
        String result = "";
        try {
            result = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String[] cleaner = result.split("\n");
        map.put("damage", cleaner[0]);
        map.put("duration", cleaner[1]);
        map.put("type", cleaner[2]);
        if (table.equals("Ulti"))
            map.put("critical", cleaner[3]);

        return map;
    }

    int getCatched (int pokeID, Context context) {
        String temp = "";
        backgroundWorker = new BackgroundWorker("getCatched", pokeID, getLocalUsername(), context);
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

    List<Integer> getIdsFromPokeID (int pokeID, Context context) {
        backgroundWorker = new BackgroundWorker("getIdsFromPokeID", pokeID, getLocalUsername(), context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        String tmp = "";
        try {
            tmp = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String[] stringIds = tmp.split("\n");
        List<Integer> ids = new ArrayList<>();
        if (!stringIds[0].equals("0"))
            for (String stringId : stringIds) {
                ids.add(Integer.parseInt(stringId));
            }

        return ids;
    }

    String[] getPokeAttacks (int pokeCopy, Context context) {
        backgroundWorker = new BackgroundWorker("getPokeAttacks", pokeCopy, context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        String tmp = "";
        try {
            tmp = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return tmp.split("\n");
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

    List<String> getMoves (int pokeID, String table, Context context) {
        backgroundWorker = new BackgroundWorker("getMoves", pokeID, table, context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        List<String> moves = new ArrayList<>();
        String tmp = "";
        try {
            tmp = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String[] cleaner = tmp.split("\n");
        Collections.addAll(moves, cleaner);

        return moves;
    }

    List<String> getWeaknesses (int pokeID, Context context) {
        List<String> weaknesses = new ArrayList<>();
        backgroundWorker = new BackgroundWorker("getWeakness", pokeID, context);
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

    List<String> getStrenghts (int pokeID, Context context) {
        List<String> strengths = new ArrayList<>();
        backgroundWorker = new BackgroundWorker("getStrenghts", pokeID, context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
        String temp = "";
        try {
            temp = backgroundWorker.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (!temp.equals("")) {
            String[] division = temp.split("\n");
            Collections.addAll(strengths, division);
        }
        return strengths;
    }

    int getTotalCatches (Context context) {
        int totalCatches;
        String temp = "";
        backgroundWorker = new BackgroundWorker("getConditionedRows", "Catches", getLocalUsername(), context);
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

    int getTotalCopies (Context context) {
        int totalCopies;
        String temp = "";
        backgroundWorker = new BackgroundWorker("getTotalCopies", getLocalUsername(), context);
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

    void updatePokeAttack (String attack, int pokeID, Context context) {
        backgroundWorker = new BackgroundWorker("updatePokeAttack", pokeID, attack, context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
    }

    void updatePokeUlti (String ulti, int pokeID, Context context) {
        backgroundWorker = new BackgroundWorker("updatePokeUlti", pokeID, ulti, context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
    }

    void updatePokeIVs (int ivMin, int ivMax, int pokeID, Context context) {
        backgroundWorker = new BackgroundWorker("updatePokeIVs", ivMin, ivMax, pokeID, context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
    }

    void updatePokeName (String name, int pokeID, Context context) {
        backgroundWorker = new BackgroundWorker("updatePokeName", pokeID, name, context);
        backgroundWorker.delegate = this;
        backgroundWorker.execute();
    }

    @Override
    public void processFinish(String output) {
        backgroundWorker.delegate = this;
    }

    private static class PokemonHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "PokemonDatabase.db";
        private static final int DATABASE_VERSION = 15;

        //Types Declaration
        private static final String VARCHAR = " VARCHAR(";
        private static final String INT = " INT(";

        //Tables Declaration
        private static final String SETTINGS = "Settings";

        //Columns Declaration
        private static final String OWNER = "Owner";
        private static final String SMARTKEDEXNAME = "SmartkedexName";
        private static final String POKEMONGO = "PokemonGO";



        //CREATE TABLE Statements
        private static final String CREATE_SETTINGS = "CREATE TABLE IF NOT EXISTS " + SETTINGS + "(" +
                                                       "Username VARCHAR(20) PRIMARY KEY, " +
                                                       "Password VARCHAR(20), " +
                                                       "RememberME INT(1), " +
                                                       OWNER + VARCHAR + "20), " +
                                                       SMARTKEDEXNAME + VARCHAR + "20), " +
                                                       POKEMONGO + INT + "1))";

        private Context context;

        PokemonHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_SETTINGS);
            db.execSQL("INSERT INTO Settings (PokemonGO) VALUES(2)");
            Toast.makeText(context, "Creazione del Database\neseguita correttamente", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String[] columns = {PokemonHelper.POKEMONGO, PokemonHelper.SMARTKEDEXNAME};
            Cursor cursor = db.query(PokemonHelper.SETTINGS, columns, null, null, null, null, null);
            String[] backupData = {"", ""};
            while (cursor.moveToNext()) {
                backupData[0] = cursor.getString(cursor.getColumnIndex(PokemonHelper.SMARTKEDEXNAME));
                backupData[1] = String.valueOf(cursor.getInt(cursor.getColumnIndex(PokemonHelper.POKEMONGO)));
            }

            db.execSQL("DROP TABLE Settings");
            db.execSQL(CREATE_SETTINGS);
            db.execSQL("INSERT INTO Settings (SmartkedexName, PokemonGO, RememberME) VALUES ('"+backupData[0]+"', '"+Integer.parseInt(backupData[1])+"', 0)");
            Toast.makeText(context, "onUpgrade", Toast.LENGTH_SHORT).show();
        }
    }
}