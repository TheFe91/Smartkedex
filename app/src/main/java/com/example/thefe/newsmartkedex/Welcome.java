package com.example.thefe.newsmartkedex;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by TheFe on 04/11/2016.
 */

public class Welcome extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        final PokemonDatabaseAdapter pokemonHelper = new PokemonDatabaseAdapter(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int dpi = displayMetrics.densityDpi;

        if (pokemonHelper.getRows("Settings") == 0) { //it's the first app launch ever
            TextView textView = (TextView)findViewById(R.id.welcome);
            textView.setText("Benvenuto!");
            textView = (TextView)findViewById(R.id.owner);
            textView.setText("Inserisci il tuo nome*");
            textView = (TextView)findViewById(R.id.smartkedex);
            textView.setText("Inserisci il nome dello Smartkédex");
            textView = (TextView)findViewById(R.id.tips);
            if (dpi == 480)
                textView.setTextSize(13);
            textView.setText("Puoi modificare queste impostazioni in ogni momento\naccedendo al menu delle impostazioni in alto a destra");
            final Switch pkmnGO = (Switch)findViewById(R.id.POGO);
            pkmnGO.setText("Giochi a Pokémon GO?");

            Button button = (Button)findViewById(R.id.enter);
            button.setText("Conferma ed Entra");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = (EditText)findViewById(R.id.setOwner);
                    String owner = editText.getText().toString();
                    editText = (EditText)findViewById(R.id.setSmartkedex);
                    String smartkedex = editText.getText().toString();
                    if (owner.equals("")) {
                        Toast.makeText(getApplicationContext(), "Inserisci il tuo nome\nper accedere all'App", Toast.LENGTH_LONG).show();
                    }
                    else {
                        if (pkmnGO.isChecked())
                            pokemonHelper.insertSettingsData(owner, smartkedex, "ITA", 1);
                        else
                            pokemonHelper.insertSettingsData(owner, smartkedex, "ITA", 0);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }
        else {
            String owner = pokemonHelper.getOwner();
            TextView textView = (TextView)findViewById(R.id.welcome);
            textView.setText("Bentornato " + owner + "!");
            if (pokemonHelper.getPokemonGO() == 1) {
                int total = pokemonHelper.getRows("Catches");
                int copies = pokemonHelper.getRows("Copy");
                textView.append("\n\nGiocando a PokémonGO hai catturato " + total + " Pokémon,\nper un totale di " + copies + " esemplari.");
            }

            View remove = findViewById(R.id.owner);
            ViewGroup parent = (ViewGroup)remove.getParent();
            parent.removeView(remove);
            remove = findViewById(R.id.setOwner);
            parent = (ViewGroup)remove.getParent();
            parent.removeView(remove);
            remove = findViewById(R.id.setSmartkedex);
            parent = (ViewGroup)remove.getParent();
            parent.removeView(remove);
            remove = findViewById(R.id.smartkedex);
            parent = (ViewGroup)remove.getParent();
            parent.removeView(remove);
            remove = findViewById(R.id.POGO);
            parent = (ViewGroup)remove.getParent();
            parent.removeView(remove);
            remove = findViewById(R.id.tips);
            parent = (ViewGroup)remove.getParent();
            parent.removeView(remove);

            Button button = (Button)findViewById(R.id.enter);
            button.setText("Entra nell ' App");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }

    }
}
