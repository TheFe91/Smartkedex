package com.rollercoders.smartkedex;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by TheFe on 15/05/2017.
 */

public class InitialLogin extends Activity {

    private Context context = this;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final PokemonDatabaseAdapter pokemonDatabaseAdapter = new PokemonDatabaseAdapter(this);

        PackageInfo packageInfo;
        int appversion = 0;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appversion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (!pokemonDatabaseAdapter.getAppVersion(appversion)) {
            Toast.makeText(this, getResources().getString(getResources().getIdentifier("versionFailMessage", "string", getPackageName())), Toast.LENGTH_LONG).show();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .setContentTitle(getResources().getString(getResources().getIdentifier("versionFail", "string", getPackageName())))
                    .setContentText(getResources().getString(getResources().getIdentifier("versionFailMessage", "string", getPackageName())));

            final String appPackageName = getPackageName();
            Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appPackageName));

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);
            NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());

            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }

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
                            System.err.println("sono if");
                        }
                        else {
                            pokemonDatabaseAdapter.setNotRememberME(username, password);
                            System.err.println("sono else");
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
