package com.rollercoders.smartkedex;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * Created by TheFe on 30/05/2017.
 */

public class Registration extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        final Context context = this;

        final PokemonDatabaseAdapter pokemonDatabaseAdapter = new PokemonDatabaseAdapter(this);

        getActionBar();

        TextView textView = (TextView)findViewById(R.id.registerusername);
        textView.setText(getResources().getString(getResources().getIdentifier("registerUsername", "string", getPackageName())));
        textView = (TextView)findViewById(R.id.registerpassword);
        textView.setText(getResources().getString(getResources().getIdentifier("registerPassword", "string", getPackageName())));
        textView = (TextView)findViewById(R.id.registermail);
        textView.setText(getResources().getString(getResources().getIdentifier("registerMail", "string", getPackageName())));
        final CheckBox checkBox = (CheckBox)findViewById(R.id.acceptCondition);
        textView = (TextView)findViewById(R.id.acceptConditionText1);
        textView.setText(getResources().getString(getResources().getIdentifier("acceptCondition1", "string", getPackageName())));
        textView = (TextView)findViewById(R.id.acceptConditionText2);
        textView.setText(getResources().getString(getResources().getIdentifier("acceptCondition2", "string", getPackageName())));
        Pattern pattern = Pattern.compile(getResources().getString(getResources().getIdentifier("acceptCondition2", "string", getPackageName())));
        Linkify.addLinks(textView, pattern, "http://smartkedexwebservices.altervista.org/termsandconditions.php");
        final Button button = (Button)findViewById(R.id.confirmRegistration);
        button.setText(getResources().getString(getResources().getIdentifier("confirmRegistration", "string", getPackageName())));
        button.setEnabled(false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBox.isChecked()) {
                    button.setEnabled(true);
                }
                else {
                    button.setEnabled(false);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText getUsername = (EditText)findViewById(R.id.setusername);
                EditText getPassword = (EditText)findViewById(R.id.setpassword);
                EditText getMail = (EditText)findViewById(R.id.setmail);
                String username = getUsername.getText().toString();
                String password = getPassword.getText().toString();
                String mail = getMail.getText().toString();
                String result;
                if (username.equals("") || password.equals("") || mail.equals(""))
                    result = "error";
                else {
                    PackageInfo packageInfo;
                    int appversion = 0;
                    try {
                        packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        appversion = packageInfo.versionCode;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    result = pokemonDatabaseAdapter.registration(mail, username, password, appversion, getApplicationContext());
                    String[] cleaner = result.split("\n");
                    result = cleaner[0];
                }

                AlertDialog.Builder builder;

                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Dialog);
                else
                    builder = new AlertDialog.Builder(context);

                switch (result) {
                    case "registration_success":
                        builder.setTitle(getResources().getString(getResources().getIdentifier("regSuccessTitle", "string", getPackageName())))
                                .setMessage(getResources().getString(getResources().getIdentifier("regSuccessText", "string", getPackageName())))
                                .setPositiveButton(getResources().getString(getResources().getIdentifier("OK", "string", getPackageName())), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();
                        break;
                    case "username_already_in_db":
                        builder.setTitle(getResources().getString(getResources().getIdentifier("regFailDupUNTitle", "string", getPackageName())))
                                .setMessage(getResources().getString(getResources().getIdentifier("regFailDupUNText", "string", getPackageName())))
                                .setPositiveButton(getResources().getString(getResources().getIdentifier("OK", "string", getPackageName())), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        break;
                    case "error":
                        builder.setTitle(getResources().getString(getResources().getIdentifier("FieldsNotNullTitle", "string", getPackageName())))
                                .setMessage(getResources().getString(getResources().getIdentifier("FieldsNotNullText", "string", getPackageName())))
                                .setPositiveButton(getResources().getString(getResources().getIdentifier("OK", "string", getPackageName())), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        break;
                    default:
                        builder.setTitle(getResources().getString(getResources().getIdentifier("regFailTitle", "string", getPackageName())))
                                .setMessage(getResources().getString(getResources().getIdentifier("regFailText", "string", getPackageName())))
                                .setPositiveButton(getResources().getString(getResources().getIdentifier("OK", "string", getPackageName())), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        break;
                }
            }
        });
    }
}
