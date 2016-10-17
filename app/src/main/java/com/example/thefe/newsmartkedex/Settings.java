package com.example.thefe.newsmartkedex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by TheFe on 17/10/2016.
 */

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        getActionBar();
    }
}
