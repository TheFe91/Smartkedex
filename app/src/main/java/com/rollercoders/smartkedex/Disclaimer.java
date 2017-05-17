package com.rollercoders.smartkedex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by TheFe on 15/05/2017.
 */

public class Disclaimer extends Activity {

    PokemonDatabaseAdapter pokemonHelper;

    public Disclaimer() {pokemonHelper = new PokemonDatabaseAdapter(this);}

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disclaimer);

        int disclaimer = pokemonHelper.getDisclaimer();
        if (disclaimer == 1) {
            Intent i = new Intent(getApplicationContext(), Welcome.class);
            startActivity(i);
            finish();
        }

        final CheckBox checkBox = (CheckBox)findViewById(R.id.acceptDisclaimer);
        TextView textView = (TextView)findViewById(R.id.disclaimerText);
        Button button = (Button)findViewById(R.id.confirmDisclaimer);

        textView.setText(getResources().getString(getResources().getIdentifier("disclaimer", "string", getPackageName())));
        checkBox.setText(getResources().getString(getResources().getIdentifier("acceptDisclaimer", "string", getPackageName())));
        button.setText(getResources().getString(getResources().getIdentifier("confirmDisclaimer", "string", getPackageName())));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    pokemonHelper.updateDisclaimer();
                }
                Intent i = new Intent(getApplicationContext(), Welcome.class);
                startActivity(i);
                finish();
            }
        });

    }
}
