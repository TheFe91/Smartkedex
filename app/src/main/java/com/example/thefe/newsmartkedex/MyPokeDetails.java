package com.example.thefe.newsmartkedex;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by TheFe on 01/11/2016.
 */

public class MyPokeDetails extends AppCompatActivity {

    int pokeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_poke_details);

        getActionBar();

        Intent i = getIntent();
        pokeID = i.getExtras().getInt("id");
        final PokemonDetails pokemonDetails = new PokemonDetails();
        final PokemonDatabaseAdapter pokemonHelper = new PokemonDatabaseAdapter(this);
        final String pokeName = pokemonDetails.getName(pokeID);
        String owner = pokemonHelper.getOwner();

        TextView textView = (TextView)findViewById(R.id.pkmnName);
        textView.setText("I "+pokeName+" di "+owner);

        Button button = (Button)findViewById(R.id.edit);
        button.setText("Modifica i Dettagli dei tuoi "+pokeName);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditPokeDetails.class);
                i.putExtra("id", pokeID);
                startActivity(i);
            }
        });

        ImageAdapter imageAdapter = new ImageAdapter(this);

        List<Integer> ids = pokemonHelper.getIdsFromPokeID(pokeID);

        for (int element:ids) {
            TableLayout tableLayout = (TableLayout)findViewById(R.id.tableNumber);
            TableRow tableRow = new TableRow(getApplicationContext());
            TableLayout attacchi = new TableLayout(getApplicationContext());
            TableRow attackRow = new TableRow(getApplicationContext());
            TableRow ultiRow = new TableRow(getApplicationContext());
            TableRow nameRow = new TableRow(getApplicationContext());
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(imageAdapter.mThumbIds[pokeID-1]);
            TextView attack = new TextView(getApplicationContext());
            TextView ulti = new TextView(getApplicationContext());
            TextView name = new TextView(getApplicationContext());

            TableRow.LayoutParams trparams = new TableRow.LayoutParams(250,250);

            imageView.setLayoutParams(trparams);
            imageView.setId(element);
            String[] attacks = pokemonHelper.getPokeAttacks(element); //0 is attack, 1 is ulti

            Map<String, String> attackStuff = pokemonHelper.getAttacksStuff(attacks[0], "Attack"); //keys are "duration", "type" and "damage"
            attack.setTypeface(null, Typeface.BOLD);
            attack.setText(attacks[0] + " - " + attackStuff.get("damage") + " ");
            List<String> pokeTypes = pokemonHelper.getPokeTypes(pokeID);
            for (String type:pokeTypes)
                if (type.equals(attackStuff.get("type"))) {
                    int stab = Integer.parseInt(attackStuff.get("damage"));
                    stab = stab + (stab / 4);
                    attack.append("(STAB: " + stab + ")");
                }
            attack.append(" - " + attackStuff.get("duration") + "s");
            attack.setTextColor(getResources().getColor(getResources().getIdentifier(attackStuff.get("type").toLowerCase(), "color", getPackageName())));

            Map<String, String> ultiStuff = pokemonHelper.getAttacksStuff(attacks[1], "Ulti"); //keys are "duration", "critical", type" and "damage"
            ulti.setTypeface(null, Typeface.BOLD);
            ulti.setText(attacks[1] + " - " + ultiStuff.get("damage") + " ");
            for (String type:pokeTypes)
                if (type.equals(ultiStuff.get("type"))) {
                    int stab = Integer.parseInt(ultiStuff.get("damage"));
                    stab = stab + (stab / 4);
                    ulti.append("(STAB: " + stab + ")");
                }
            ulti.append(" - " + ultiStuff.get("duration") + "s - " + ultiStuff.get("critical") + "%");
            ulti.setTextColor(getResources().getColor(getResources().getIdentifier(ultiStuff.get("type").toLowerCase(), "color", getPackageName())));

            trparams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            attack.setLayoutParams(trparams);
            ulti.setLayoutParams(trparams);

            String copyName = pokemonHelper.getCopyName(element);

            if (!copyName.equals("")) {
                name.setText(copyName);
            }
            else
                name.setText(pokemonDetails.getName(pokeID));

            trparams.gravity = Gravity.CENTER_VERTICAL;
            attacchi.setLayoutParams(trparams);

            tableRow.addView(imageView);
            nameRow.addView(name);
            attackRow.addView(attack);
            ultiRow.addView(ulti);
            attacchi.addView(nameRow);
            attacchi.addView(attackRow);
            attacchi.addView(ultiRow);
            tableRow.addView(attacchi);

            tableLayout.addView(tableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent (getApplicationContext(), PokemonDetails.class);
        i.putExtra("id", pokeID-1);
        startActivity(i);
        return true;
    }
}