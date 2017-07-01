package com.rollercoders.smartkedex;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by TheFe on 04/11/2016.
 */

public class Welcome extends Activity {

    private final PokemonDatabaseAdapter pokemonHelper = new PokemonDatabaseAdapter(this);
    private Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int dpi = displayMetrics.densityDpi;

        String dbSmartkedex = pokemonHelper.getSmartkedex();

        if (dbSmartkedex == null || dbSmartkedex.equals("")) { //it's the first app launch ever
            TextView textView = (TextView)findViewById(R.id.welcome);
            textView.setText(getResources().getString(getResources().getIdentifier("welcome", "string", getPackageName())));
            textView = (TextView)findViewById(R.id.smartkedex);
            textView.setText(getResources().getString(getResources().getIdentifier("smartkedexname", "string", getPackageName())));
            textView = (TextView)findViewById(R.id.tips);
            if (dpi == 480)
                textView.setTextSize(13);
            textView.setText(getResources().getString(getResources().getIdentifier("edit_settings", "string", getPackageName())));
            final Switch pkmnGO = (Switch)findViewById(R.id.POGO);
            pkmnGO.setText(getResources().getString(getResources().getIdentifier("playpokego", "string", getPackageName())));

            Button button = (Button)findViewById(R.id.enter);
            button.setText(getResources().getString(getResources().getIdentifier("confirmRegistration", "string", getPackageName())));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = (EditText)findViewById(R.id.setSmartkedex);
                    String smartkedex = editText.getText().toString();
                        if (pkmnGO.isChecked())
                            pokemonHelper.insertSettingsData(pokemonHelper.getLocalUsername(), smartkedex, 1);
                        else
                            pokemonHelper.insertSettingsData(pokemonHelper.getLocalUsername(), smartkedex, 0);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                }
            });
        }
        else {
            TextView textView = (TextView)findViewById(R.id.welcome);
            textView.setText(getResources().getString(getResources().getIdentifier("welcome_back", "string", getPackageName()), pokemonHelper.getLocalUsername()));
            if (pokemonHelper.getPokemonGO() == 1) {
                int total = pokemonHelper.getTotalCatches(context);
                int copies = pokemonHelper.getTotalCopies(context);
                textView.append(getResources().getString(getResources().getIdentifier("pokemongo_recap", "string", getPackageName()), total, copies));
            }

            View remove;
            ViewGroup parent;

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
            button.setText(getResources().getString(getResources().getIdentifier("enterapp", "string", getPackageName())));

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
