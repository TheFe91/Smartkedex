package com.example.thefe.newsmartkedex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

        lingua = ((GlobalVariables) this.getApplication()).getLanguage();

        getActionBar();

        TextView language = (TextView)findViewById(R.id.lingua);
        TextView name = (TextView)findViewById(R.id.nomeSmartkedex);
        TextView proprietario = (TextView)findViewById(R.id.proprietario);
        final EditText inputNome = (EditText)findViewById(R.id.inputNomeSmartkedex);
        final EditText inputProprietario = (EditText)findViewById(R.id.inputProprietario);
        Button presentazione = (Button)findViewById(R.id.presentazione);
        Button conferma = (Button)findViewById(R.id.conferma);

        inputNome.setText(((GlobalVariables) this.getApplication()).getSmartkedex());
        inputProprietario.setText(((GlobalVariables) this.getApplication()).getOwner());

        switch (lingua) {
            case "":
                break;
            case "ENG":
                language.setText("Language");
                name.setText("Smartkédex Name");
                proprietario.setText("This Smartkédex is property of:");
                presentazione.setText("Presentation");
                conferma.setText("Apply changes");
                RadioButton radioEng = (RadioButton)findViewById(R.id.lang_eng);
                radioEng.setChecked(true);
                break;
            case "ITA":
                language.setText("Lingua dell'App");
                name.setText("Nome dello Smartkédex");
                proprietario.setText("Questo Smartkédex è di proprietà di:");
                presentazione.setText("Presentazione");
                conferma.setText("Applica");
                RadioButton radioIta = (RadioButton)findViewById(R.id.lang_ita);
                radioIta.setChecked(true);
                break;
        }

        presentazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Presentation presentation = new Presentation(lingua, getApplicationContext()); //passo la lingua corrente e il context
                presentation.presentati();
            }
        });

        final GlobalVariables cast = ((GlobalVariables) this.getApplication());

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton)findViewById(selectedId);

                String smartkedex = inputNome.getText().toString();
                String owner = inputProprietario.getText().toString();

                cast.setLanguage((String) radioButton.getText());
                cast.setOwner(owner);
                cast.setSmartkedex(smartkedex);
            }
        });
    }

}
