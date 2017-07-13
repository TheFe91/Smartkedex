package com.rollercoders.smartkedex;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by TheFe on 13/07/2017.
 */

public class Destroyer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destroyer);

        final Context context = this;

        TextView title = (TextView)findViewById(R.id.warning);
        TextView text = (TextView)findViewById(R.id.warningtext);
        final Button confirm = (Button)findViewById(R.id.proceed);
        Button back = (Button)findViewById(R.id.back);
        final Switch mSwitch = (Switch)findViewById(R.id.activator);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/cmss12.otf");

        title.setTypeface(typeface);
        text.setTypeface(typeface);

        title.setText(getResources().getString(getResources().getIdentifier("warning", "string", getPackageName())));
        text.setText(getResources().getString(getResources().getIdentifier("warningText", "string", getPackageName())));
        mSwitch.setText(getResources().getString(getResources().getIdentifier("switchText", "string", getPackageName())));
        back.setText(getResources().getString(getResources().getIdentifier("back", "string", getPackageName())));

        confirm.setText(getResources().getString(getResources().getIdentifier("erase", "string", getPackageName())));
        confirm.setEnabled(false);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Settings.class);
                startActivity(i);
                finish();
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mSwitch.isChecked())
                    confirm.setEnabled(true);
                else
                    confirm.setEnabled(false);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(getResources().getString(getResources().getIdentifier("lastWarningTitle", "string", getPackageName())))
                        .setMessage(getResources().getString(getResources().getIdentifier("lastWarningText", "string", getPackageName())))
                        .setPositiveButton(getResources().getString(getResources().getIdentifier("confirm", "string", getPackageName())), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PokemonDatabaseAdapter pokemonDatabaseAdapter = new PokemonDatabaseAdapter(context);
                                pokemonDatabaseAdapter.erase(pokemonDatabaseAdapter.getLocalUsername(), context);
                                pokemonDatabaseAdapter.localErase();
                                Intent k = new Intent(context, InitialLogin.class);
                                startActivity(k);
                                finish();
                            }
                        })
                        .setNegativeButton(getResources().getString(getResources().getIdentifier("cornerSave", "string", getPackageName())), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //DISMISS, DO NOTHING
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
}
