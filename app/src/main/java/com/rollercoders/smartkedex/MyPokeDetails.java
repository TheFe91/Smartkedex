package com.rollercoders.smartkedex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
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
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_poke_details);

        getActionBar();

        context = this;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int dpi = displayMetrics.densityDpi;

        Intent i = getIntent();
        pokeID = i.getExtras().getInt("id");
        final PokemonDatabaseAdapter pokemonHelper = new PokemonDatabaseAdapter(this);
        final String pokeName = pokemonHelper.getPokeName(pokeID, getApplicationContext());
        String owner = pokemonHelper.getOwner();

        TextView textView = (TextView)findViewById(R.id.pkmnName);
        textView.setText(getResources().getString(getResources().getIdentifier("pokemon_owned_by", "string", getPackageName()), pokeName, owner));

        Button button = (Button)findViewById(R.id.edit);
        button.setText(getResources().getString(getResources().getIdentifier("edit_details", "string", getPackageName()), pokeName));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditPokeDetails.class);
                i.putExtra("id", pokeID);
                startActivity(i);
            }
        });

        ImageAdapter imageAdapter = new ImageAdapter(this);

        List<Integer> ids = pokemonHelper.getIdsFromPokeID(pokeID, getApplicationContext());

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
            String[] attacks = pokemonHelper.getPokeAttacks(element, getApplicationContext()); //0 is attack, 1 is ulti

            Map<String, String> attackStuff = pokemonHelper.getAttacksStuff(attacks[0], "Attack", context); //keys are "duration", "type" and "damage"
            attack.setTypeface(null, Typeface.BOLD);
            if (dpi == 480)
                attack.setTextSize(13);
            attack.setText(attacks[0] + " - " + attackStuff.get("damage") + " ");
            List<String> pokeTypes = pokemonHelper.getPokeTypes(pokeID, getApplicationContext());
            for (String type:pokeTypes)
                if (type.equals(attackStuff.get("type"))) {
                    int stab = Integer.parseInt(attackStuff.get("damage"));
                    stab = stab + (stab / 4);
                    attack.append("(STAB: " + stab + ")");
                }
            attack.append(" - " + attackStuff.get("duration") + "s");
            attack.setTextColor(getResources().getColor(getResources().getIdentifier(attackStuff.get("type").toLowerCase(), "color", getPackageName())));

            if (pokeID != 132) {
                try {
                    Map<String, String> ultiStuff = pokemonHelper.getAttacksStuff(attacks[1], "Ulti", context); //keys are "duration", "critical", type" and "damage"
                    ulti.setTypeface(null, Typeface.BOLD);
                    if (dpi == 480)
                        ulti.setTextSize(13);
                    ulti.setText(attacks[1] + " - " + ultiStuff.get("damage") + " ");
                    for (String type : pokeTypes)
                        if (type.equals(ultiStuff.get("type"))) {
                            int stab = Integer.parseInt(ultiStuff.get("damage"));
                            stab = stab + (stab / 4);
                            ulti.append("(STAB: " + stab + ")");
                        }
                    ulti.append(" - " + ultiStuff.get("duration") + "s - " + ultiStuff.get("critical") + "%");
                    ulti.setTextColor(getResources().getColor(getResources().getIdentifier(ultiStuff.get("type").toLowerCase(), "color", getPackageName())));
                }
                catch (Exception e) {
                    System.err.println(e);
                }
            }

            trparams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            attack.setLayoutParams(trparams);
            try {
                ulti.setLayoutParams(trparams);
            }
            catch (Exception e){
                System.err.println(e);
            }

            String copyName = pokemonHelper.getCopyName(element, context);

            if (!copyName.equals("")) {
                name.setText(copyName);
            }
            else
                name.setText(pokemonHelper.getPokeName(pokeID, getApplicationContext()));

            trparams.gravity = Gravity.CENTER_VERTICAL;
            attacchi.setLayoutParams(trparams);

            tableRow.addView(imageView);
            nameRow.addView(name);
            attackRow.addView(attack);
            try {
                ultiRow.addView(ulti);
            }
            catch (Exception e){
                System.err.println(e);
            }
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