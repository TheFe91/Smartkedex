package com.rollercoders.smartkedex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TheFe on 20/10/2016.
 */

public class EditPokeDetails extends AppCompatActivity {

    int pokeID, counter = 0;
    Spinner attackSpinner, ultiSpinner, ivMinSpinner, ivMaxSpinner;
    EditText editText;
    CheckBox checkBox;
    Context context;
    TableRow.LayoutParams params;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_poke_details);

        AdView mAdView = (AdView) findViewById(R.id.AdView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("01658E55E3FE332C522AEF747FD402FF")
                .build();
        mAdView.loadAd(adRequest);

        context = this;

        typeface = Typeface.createFromAsset(getAssets(), "fonts/cmss12.otf");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int dpi = displayMetrics.densityDpi;

        Intent i = getIntent();
        pokeID = i.getExtras().getInt("id");
        final PokemonDatabaseAdapter pokemonHelper = new PokemonDatabaseAdapter(this);
        final String pokeName = pokemonHelper.getPokeName(pokeID, getApplicationContext());

        getActionBar();

        TextView textView = (TextView)findViewById(R.id.pkmnName);
        textView.setText(getResources().getString(getResources().getIdentifier("edit_your_pokemons", "string", getPackageName()), pokeName));
        textView = (TextView)findViewById(R.id.number);
        textView.setText(getResources().getString(getResources().getIdentifier("number_insert_pokemons", "string", getPackageName()), pokeName));
        textView = (TextView)findViewById(R.id.oldcopies);
        textView.setText(getResources().getString(getResources().getIdentifier("already_inserted", "string", getPackageName())));
        textView = (TextView)findViewById(R.id.newcopies);
        textView.setText(getResources().getString(getResources().getIdentifier("pokemon_to_insert", "string", getPackageName())));


        //defining and setting up the SpinnerNumber
        final Spinner spinner = (Spinner)findViewById(R.id.spinnerNumber);
        List<Integer> list = new ArrayList<>();
        for (int j = 0; j < 11; j++) {
            list.add(j);
        }

        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        final List<Integer> copyIds = pokemonHelper.getIdsFromPokeID(pokeID, getApplicationContext());

        boolean insertHeader = true;

        //defining and Setting-up the already in the database Pokémons
        for (int pokeId:copyIds) {
            //get a reference for the TableLayout
            TableLayout table = (TableLayout)findViewById(R.id.copies);

            if (insertHeader) {
                TableRow header = new TableRow(context);
                header.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                TextView tv = new TextView(context);
                tv.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                tv.setText(getResources().getString(getResources().getIdentifier("name", "string", getPackageName())));
                tv.setGravity(Gravity.CENTER);
                tv.setTypeface(typeface);
                header.addView(tv);
                tv = new TextView(context);
                tv.setText(getResources().getString(getResources().getIdentifier("mivs", "string", getPackageName())));
                tv.setGravity(Gravity.CENTER);
                tv.setTypeface(typeface);
                header.addView(tv);
                tv = new TextView(context);
                tv.setText(getResources().getString(getResources().getIdentifier("del", "string", getPackageName())));
                tv.setGravity(Gravity.CENTER);
                tv.setTypeface(typeface);
                header.addView(tv);

                table.addView(header, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                insertHeader = false;
            }

            //create a new TableLayout
            TableLayout internaltable = new TableLayout(getApplicationContext());

            // create a new TableRow
            TableRow row = new TableRow(context);
            TableRow attackRow = new TableRow(context);
            TableRow ultiRow = new TableRow(context);

            params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            attackSpinner = new Spinner(getApplicationContext());
            attackSpinner.setLayoutParams(params);
            attackSpinner.setId(counter+1);
            System.err.println(attackSpinner.getId());
            if (pokeID != 132) {
                ultiSpinner = new Spinner(getApplicationContext());
                ultiSpinner.setLayoutParams(params);
                ultiSpinner.setId(counter+2);
                System.err.println(ultiSpinner.getId());
            }

            String[] moves = pokemonHelper.getPokeAttacks(pokeId, getApplicationContext()); //0 is attack, 1 is ulti

            List<String> attacks = pokemonHelper.getMoves(pokeID, "HasAttack", getApplicationContext());

            ArrayAdapter<String>attacksAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, attacks);
            attacksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            attackSpinner.setAdapter(attacksAdapter);
            attackSpinner.setSelection(attacksAdapter.getPosition(moves[0]));
            attackSpinner.setBackgroundColor(getResources().getColor(getResources().getIdentifier(pokemonHelper.getMovesType(moves[0], "Attack", context).toLowerCase(), "color", getPackageName())));

            //this is necessary because there can be Pokémons like Ditto who don't have a ulti, otherwise the app crashes
            if (pokeID != 132) {
                List<String> ultis = pokemonHelper.getMoves(pokeID, "HasUlti", getApplicationContext());

                ArrayAdapter<String> ultiAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, ultis);
                attacksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ultiSpinner.setAdapter(ultiAdapter);
                ultiSpinner.setSelection(ultiAdapter.getPosition(moves[1]));
                ultiSpinner.setBackgroundColor(getResources().getColor(getResources().getIdentifier(pokemonHelper.getMovesType(moves[1], "Ulti", context).toLowerCase(), "color", getPackageName())));
            }

            editText = new EditText(context);
            if (dpi == 480) {
                editText.setWidth(250);
                editText.setTextSize(13);
            }
            else if (dpi == 420)
                editText.setWidth(300);

            editText.setHint(getResources().getString(getResources().getIdentifier("hint_name", "string", getPackageName())));
            editText.setHintTextColor(getResources().getColor(R.color.acciaio));
            editText.setTextColor(getResources().getColor(android.R.color.black));
            editText.setId(counter+3);
            System.err.println(editText.getId());
            editText.setText(pokemonHelper.getCopyName(pokeId, context));

            ivMinSpinner = new Spinner(context);
            ivMinSpinner.setLayoutParams(params);
            ivMinSpinner.setId(counter+4);
            System.err.println(ivMinSpinner.getId());

            ivMaxSpinner = new Spinner(context);
            ivMaxSpinner.setLayoutParams(params);
            ivMaxSpinner.setId(counter+5);
            System.err.println(ivMaxSpinner.getId());

            for (int j=0; j<101; j++) {
                list.add(j);
            }

            dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ivMinSpinner.setAdapter(dataAdapter);
            ivMaxSpinner.setAdapter(dataAdapter);

            checkBox = new CheckBox(getApplicationContext());
            checkBox.setButtonDrawable(R.drawable.checkbox_selector);
            checkBox.setId(counter+6);

            // add the TextView  to the new TableRow
            params.gravity = Gravity.CENTER_VERTICAL;
            params.setMargins(15,0,0,0);
            editText.setLayoutParams(params);
            row.addView(editText);
            attackRow.addView(attackSpinner);
            if (pokeID != 132)
                ultiRow.addView(ultiSpinner);
            else
                ultiRow.addView(new TextView(context));
            attackRow.addView(ivMinSpinner);
            ultiRow.addView(ivMaxSpinner);
            internaltable.addView(attackRow);
            internaltable.addView(ultiRow);
            internaltable.setLayoutParams(params);
            row.addView(internaltable);
            checkBox.setLayoutParams(params);
            row.addView(checkBox);

            // add the TableRow to the TableLayout
            table.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            counter += 6;
        }

        //defining and setting-up the new Pokémons
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //get a reference for the TableLayout
                TableLayout table = (TableLayout)findViewById(R.id.internalcopies);
                table.removeAllViews(); //this is to remove all previous views created from an eventual previous tap

                for (int j = 1+copyIds.size(); j <= (int)spinner.getSelectedItem()+copyIds.size(); j++) {

                    if (j == 1+copyIds.size()) {
                        TableRow header = new TableRow(context);
                        header.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                        TextView tv = new TextView(context);
                        tv.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        tv.setText(getResources().getString(getResources().getIdentifier("name", "string", getPackageName())));
                        tv.setGravity(Gravity.CENTER);
                        tv.setTypeface(typeface);
                        header.addView(tv);
                        tv = new TextView(context);
                        tv.setText(getResources().getString(getResources().getIdentifier("mivs", "string", getPackageName())));
                        tv.setGravity(Gravity.CENTER);
                        tv.setTypeface(typeface);
                        header.addView(tv);

                        table.addView(header, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    }

                    //create a new TableLayout
                    TableLayout internaltable = new TableLayout(getApplicationContext());

                    // create a new TableRow
                    TableRow row = new TableRow(getApplicationContext());
                    TableRow attackRow = new TableRow(getApplicationContext());
                    TableRow ultiRow = new TableRow(getApplicationContext());

                    attackSpinner = new Spinner(getApplicationContext());
                    ultiSpinner = new Spinner(getApplicationContext());
                    attackSpinner.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    ultiSpinner.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    attackSpinner.setLayoutParams(params);
                    ultiSpinner.setLayoutParams(params);
                    attackSpinner.setId(counter+1);
                    System.err.println(attackSpinner.getId());
                    ultiSpinner.setId(counter+2);
                    System.err.println(ultiSpinner.getId());

                    editText = new EditText(getApplicationContext());
                    if (dpi == 480) {
                        editText.setWidth(250);
                        editText.setTextSize(13);
                    }
                    else if (dpi == 420)
                        editText.setWidth(300);
                    editText.setHint(getResources().getString(getResources().getIdentifier("hint_name", "string", getPackageName())));
                    editText.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.acciaio));
                    editText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                    editText.setId(counter+3);
                    System.err.println(editText.getId());

                    List<String> attacks = pokemonHelper.getMoves(pokeID, "HasAttack", getApplicationContext());

                    ArrayAdapter<String>attacksAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, attacks);
                    attacksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    attackSpinner.setAdapter(attacksAdapter);

                    List<String> ultis = pokemonHelper.getMoves(pokeID, "HasUlti", getApplicationContext());

                    ArrayAdapter<String>ultiAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, ultis);
                    attacksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ultiSpinner.setAdapter(ultiAdapter);

                    ivMinSpinner = new Spinner(context);
                    ivMinSpinner.setLayoutParams(params);
                    ivMinSpinner.setId(counter+4);
                    System.err.println(ivMinSpinner.getId());

                    ivMaxSpinner = new Spinner(context);
                    ivMaxSpinner.setLayoutParams(params);
                    ivMaxSpinner.setId(counter+5);
                    System.err.println(ivMaxSpinner.getId());

                    List<Integer> list = new ArrayList<>();

                    for (int k=0; k<101; k++) {
                        list.add(k);
                    }

                    ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ivMinSpinner.setAdapter(dataAdapter);
                    ivMaxSpinner.setAdapter(dataAdapter);

                    // add the TextView to the new TableRow
                    params.gravity = Gravity.CENTER_VERTICAL;
                    params.setMargins(15,0,0,0);
                    editText.setLayoutParams(params);
                    row.addView(editText);
                    attackRow.addView(attackSpinner);
                    ultiRow.addView(ultiSpinner);
                    attackRow.addView(ivMinSpinner);
                    ultiRow.addView(ivMaxSpinner);
                    internaltable.addView(attackRow);
                    internaltable.addView(ultiRow);
                    internaltable.setLayoutParams(params);
                    row.addView(internaltable);

                    // add the TableRow to the TableLayout
                    table.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                    counter += 5;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        Button apply = (Button)findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                counter = 0;

                //for loop to enter modifications
                for (int pokeId:copyIds) {
                    attackSpinner = (Spinner)findViewById(counter+1);
                    String attack = (String) attackSpinner.getSelectedItem();
                    String ulti = "";
                    if (pokeID != 132) {
                        ultiSpinner = (Spinner) findViewById(counter+2);
                        ulti = (String) ultiSpinner.getSelectedItem();
                    }
                    editText = (EditText)findViewById(counter+3);
                    String copyName = editText.getText().toString();
                    ivMinSpinner = (Spinner)findViewById(counter+4);
                    int ivMin = (int) ivMinSpinner.getSelectedItem();
                    ivMaxSpinner = (Spinner)findViewById(counter+5);
                    int ivMax = (int) ivMaxSpinner.getSelectedItem();
                    String[] attacks = pokemonHelper.getPokeAttacks(pokeId, getApplicationContext()); //attacks[0] contains the attack, attack[1] contains the ulti
                    if (!attack.equals(attacks[0])) {
                        pokemonHelper.updatePokeAttack(attack, pokeId, getApplicationContext());
                    }
                    if (!ulti.equals(attacks[1])) {
                        pokemonHelper.updatePokeUlti(ulti, pokeId, getApplicationContext());
                    }
                    if (!copyName.equals(pokemonHelper.getCopyName(pokeId, context))) {
                        pokemonHelper.updatePokeName(copyName, pokeId, getApplicationContext());
                    }
                    if (ivMax >= ivMin) {
                        pokemonHelper.updatePokeIVs(ivMin, ivMax, pokeId, context);
                    }
                    else {
                        Toast.makeText(context, getResources().getString(getResources().getIdentifier("failIVs", "string", getPackageName())), Toast.LENGTH_LONG).show();
                    }
                    checkBox = (CheckBox)findViewById(counter+6);
                    if (checkBox.isChecked()) {
                        pokemonHelper.deleteCopy(pokeId, getApplicationContext());
                    }

                    counter += 6;
                }

                //for loop to enter new Pokémons
                for (int j = 1+copyIds.size(); j <= (int)spinner.getSelectedItem()+copyIds.size(); j++) {
                    attackSpinner = (Spinner)findViewById(counter+1);
                    String attack = (String) attackSpinner.getSelectedItem();
                    ultiSpinner = (Spinner)findViewById(counter+2);
                    String ulti = (String) ultiSpinner.getSelectedItem();
                    editText = (EditText)findViewById(counter+3);
                    String copyName = editText.getText().toString();
                    ivMinSpinner = (Spinner)findViewById(counter+4);
                    int ivMin = (int) ivMinSpinner.getSelectedItem();
                    ivMaxSpinner = (Spinner)findViewById(counter+5);
                    int ivMax = (int) ivMaxSpinner.getSelectedItem();
                    pokemonHelper.insertCopy(attack, ulti, copyName, pokeID, ivMin, ivMax, getApplicationContext());

                    counter += 5;
                }

                Intent i = new Intent (getApplicationContext(), MyPokeDetails.class);
                i.putExtra("id", pokeID);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent (getApplicationContext(), MyPokeDetails.class);
        i.putExtra("id", pokeID);
        startActivity(i);
        finish();
        return true;
    }
}