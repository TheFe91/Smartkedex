package com.example.thefe.newsmartkedex;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by TheFe on 13/10/2016.
 * STA ROBA E' HARDCODED CHE PEGGIO NON SI PUO'! GOTTA DO SOMETHING!!!
 */

public class InsertIntoDB {

    Context context;

    public InsertIntoDB(Context context) {
        this.context = context;
    }

    public void insert () {
        PokemonDatabaseAdapter pokemonDatabaseAdapter = new PokemonDatabaseAdapter(context);

        long id = pokemonDatabaseAdapter.insertPokemon(1, "Bulbasaur", "È possibile vedere Bulbasàur mentre schiaccia un pisolino sotto il sole. Ha un seme piantato sulla schiena. Grazie ai raggi solari, il seme crescie, ingrandendosi progressivamente.");
        if (id<0) {
            System.err.println("Something went wrong");
        }
        else {
            Toast.makeText(context, "Bulbasaur inserito", Toast.LENGTH_SHORT).show();
        }

        id = pokemonDatabaseAdapter.insertPokemon(2, "Ivysaur", "C'è un germoglio piantato nella schiena di Ivysàur. Per sopportarne il peso, le zampe e il corpo crescono robusti. Quando inizia a passare più tempo esposto al sole, signìfica che il germoglio sboccerà présto in un grande fiore.");
        if (id<0) {
            System.err.println("Something went wrong");
        }
        else {
            Toast.makeText(context, "Ivysaur inserito", Toast.LENGTH_SHORT).show();
        }
        id = pokemonDatabaseAdapter.insertPokemon(3, "Venusaur", "C'è un grande fiore sulla schiena di Venusàur. Si dice che i colori diventino più vìvidi con il giusto nutrimento e i raggi solari. Il suo profumo calma le reazioni emotive delle persone.");
        if (id<0) {
            System.err.println("Something went wrong");
        }
        else {
            Toast.makeText(context, "Venusaur inserito", Toast.LENGTH_SHORT).show();
        }

        String[] tipi = {"acciaio", "acqua", "coleottero", "drago", "elettro", "erba", "fuoco", "ghiaccio", "lotta", "normale"};

        for (int i = 0; i < 10; i++) {
            id = pokemonDatabaseAdapter.insertTipo(tipi[i]);
            if (id<0) {
                System.err.println("Something went wrong at line " + i + "(" + tipi[i] + ")");
            }
            else {
                Toast.makeText(context, tipi[i] + " inserito", Toast.LENGTH_SHORT).show();
            }
        }

        id = pokemonDatabaseAdapter.insertTipiPokemon(1, "Erba");
        if (id<0) {
            System.err.println("Something went wrong");
        }
        else {
            Toast.makeText(context, "Bulbasaur-Erba inserito", Toast.LENGTH_SHORT).show();
        }

        id = pokemonDatabaseAdapter.insertTipiPokemon(1, "Veleno");
        if (id<0) {
            System.err.println("Something went wrong");
        }
        else {
            Toast.makeText(context, "Bulbasaur-Veleno inserito", Toast.LENGTH_SHORT).show();
        }

        id = pokemonDatabaseAdapter.insertTipiPokemon(2, "Erba");
        if (id<0) {
            System.err.println("Something went wrong");
        }
        else {
            Toast.makeText(context, "Ivysaur-Erba inserito", Toast.LENGTH_SHORT).show();
        }

        id = pokemonDatabaseAdapter.insertTipiPokemon(2, "Veleno");
        if (id<0) {
            System.err.println("Something went wrong");
        }
        else {
            Toast.makeText(context, "Ivysaur-Veleno inserito", Toast.LENGTH_SHORT).show();
        }

        id = pokemonDatabaseAdapter.insertTipiPokemon(3, "Erba");
        if (id<0) {
            System.err.println("Something went wrong");
        }
        else {
            Toast.makeText(context, "Venusaur-Erba inserito", Toast.LENGTH_SHORT).show();
        }

        id = pokemonDatabaseAdapter.insertTipiPokemon(3, "Veleno");
        if (id<0) {
            System.err.println("Something went wrong");
        }
        else {
            Toast.makeText(context, "Venusaur-Veleno inserito", Toast.LENGTH_SHORT).show();
        }
    }
}
