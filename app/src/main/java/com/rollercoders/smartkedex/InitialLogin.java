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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
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

        if (!checkConnection()) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
            builder.setContentTitle(getResources().getString(getResources().getIdentifier("InternetFail", "string", getPackageName())));
            builder.setContentText(getResources().getString(getResources().getIdentifier("InternetFailMessage", "string", getPackageName())));

            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 113, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(contentIntent);
            builder.setAutoCancel(true);

            builder.setFullScreenIntent(contentIntent, true);

            NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(115, builder.build());

            startActivity(homeIntent);
        }

        else {
            final PokemonDatabaseAdapter pokemonDatabaseAdapter = new PokemonDatabaseAdapter(this);

            PackageInfo packageInfo;
            int appversion = 0;
            try {
                packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                appversion = packageInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (!pokemonDatabaseAdapter.getAppVersion(appversion, context)) {
                Toast.makeText(this, getResources().getString(getResources().getIdentifier("versionFailMessage", "string", getPackageName())), Toast.LENGTH_LONG).show();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.smartkedex_ic)
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
            else {
                if (pokemonDatabaseAdapter.getRememberME() == 1) {
                    String[] loginData = pokemonDatabaseAdapter.getLoginData();
                    if (pokemonDatabaseAdapter.tryLogin(loginData[0], loginData[1], context) == 1) {
                        Intent i = new Intent(getApplicationContext(), Welcome.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        String username = pokemonDatabaseAdapter.getLocalUsername();
                        pokemonDatabaseAdapter.resetRememberME(username);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(getResources().getString(getResources().getIdentifier("loginFail", "string", getPackageName())))
                                .setMessage(getResources().getString(getResources().getIdentifier("loginFailAutoMessage", "string", getPackageName())))
                                .setPositiveButton(getResources().getString(getResources().getIdentifier("OK", "string", getPackageName())), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //DO NOTHING
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        recreate();
                    }
                }
                else {
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int dpi = displayMetrics.densityDpi;

                    if (dpi <= 420) { //Nexus 5X et simila
                        setContentView(R.layout.initiallogin_big);
                    }
                    else if (dpi == 480) { //Nexus 5 et simila
                        setContentView(R.layout.initiallogin);
                    }

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
                            if (pokemonDatabaseAdapter.tryLogin(username, password, getApplicationContext()) == 1) {
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
                        }
                    });
                }
            }
        }
    }

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }
}
