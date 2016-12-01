package com.example.thefe.newsmartkedex;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by TheFe on 01/12/16.
 */

public class ShareResult extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shareresult);

        getActionBar();

        TextView textView = (TextView)findViewById(R.id.titleshareresult);
        textView.setText("Ecco il risultato dei tuoi filtri");
    }
}
