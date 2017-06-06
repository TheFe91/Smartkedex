package com.rollercoders.smartkedex;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by TheFe on 15/05/2017.
 */

public class InitialLogin extends Activity {

    private Context context = this;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final PokemonDatabaseAdapter pokemonDatabaseAdapter = new PokemonDatabaseAdapter(this);

        if (pokemonDatabaseAdapter.getRememberME() == 1) {
            String[] loginData = pokemonDatabaseAdapter.getLoginData();
            if (pokemonDatabaseAdapter.tryLogin(loginData[0], loginData[1]) == 1) {
                Intent i = new Intent(getApplicationContext(), Welcome.class);
                startActivity(i);
                finish();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(getResources().getString(getResources().getIdentifier("loginFail", "string", getPackageName())))
                       .setMessage(getResources().getString(getResources().getIdentifier("loginFailMessage", "string", getPackageName())))
                       .setPositiveButton(getResources().getString(getResources().getIdentifier("OK", "string", getPackageName())), new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {

                           }
                       })
                       .setIcon(android.R.drawable.ic_dialog_alert)
                       .show();

            }
        }
        else {
            setContentView(R.layout.initiallogin);
            TextView textView = (TextView)findViewById(R.id.insertusername);
            textView.setText(getResources().getString(getResources().getIdentifier("insertUsername", "string", getPackageName())));
            textView = (TextView)findViewById(R.id.insertpassword);
            textView.setText(getResources().getString(getResources().getIdentifier("insertPassword", "string", getPackageName())));
            final CheckBox checkBox = (CheckBox)findViewById(R.id.remeberMe);
            checkBox.setText(getResources().getString(getResources().getIdentifier("rememberME", "string", getPackageName())));
            Button enter = (Button)findViewById(R.id.confirmRegistration);
            final Button registration = (Button)findViewById(R.id.register);
            registration.setText(getResources().getString(getResources().getIdentifier("register", "string", getPackageName())));
            enter.setText(getResources().getString(getResources().getIdentifier("confirmLogin", "string", getPackageName())));
            enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText editText = (EditText)findViewById(R.id.setusername);
                    String username = editText.getText().toString();
                    editText = (EditText)findViewById(R.id.setpassword);
                    String password = editText.getText().toString();
                    if (pokemonDatabaseAdapter.tryLogin(username, password) == 1) {
                        if (checkBox.isChecked()) {
                            pokemonDatabaseAdapter.setRememberME(username, password);
                        }
                        else {
                            pokemonDatabaseAdapter.setNotRememberME(username, password);
                        }
                        Intent i = new Intent(getApplicationContext(), Welcome.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        AlertDialog.Builder builder;
                        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            builder = new AlertDialog.Builder(context, android.R.style.Theme_Dialog);
                        else
                            builder = new AlertDialog.Builder(context);
                        builder.setTitle(getResources().getString(getResources().getIdentifier("loginFail", "string", getPackageName())))
                                .setMessage(getResources().getString(getResources().getIdentifier("loginFailMessage", "string", getPackageName())))
                                .setPositiveButton(getResources().getString(getResources().getIdentifier("OK", "string", getPackageName())), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                }
            });
            registration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), Registration.class);
                    startActivity(i);
                    finish();
                }
            });
        }

    }
}
