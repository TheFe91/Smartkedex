package com.example.thefe.newsmartkedex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by TheFe on 17/10/2016.
 */

public class Settings extends AppCompatActivity {

    public String lingua = "";
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        getActionBar();

        TextView language = (TextView)findViewById(R.id.lingua);
        TextView name = (TextView)findViewById(R.id.nomeSmartkedex);

        radioListener();

        switch (lingua) {
            case "ENG":
                language.setText("Language");
                name.setText("Smartkédex Name");
                radioListener();
                break;
            case "ITA":
                language.setText("Lingua dell'App");
                name.setText("Nome dello Smartkédex");
                radioListener();
                break;
        }
    }

    public void radioListener() {

        System.out.println("sun chi");

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);

        int selectedId = radioGroup.getCheckedRadioButtonId();

        radioButton = (RadioButton)findViewById(selectedId);

        System.out.println(radioButton.toString());
    }
}
