package com.example.thefe.newsmartkedex;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by TheFe on 30/09/16.
 */

public class ReadFileFacade {

    private String filename;
    private Context context;

    //costruttore dell'oggetto ReadFileFacade
    public ReadFileFacade (Context nContext, String filename) {
        this.filename = filename; //alle due variabili filename e context vengono assegnati i valori passati come argomenti
        this.context = nContext;  //tramite la parola chiave this, che indica che il metodo deve usare le variabili contenute in questa classe
    }

    /**********************************************************************************************************
    public String readFromFile () {

        String ret = "";

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));

            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ( (receiveString = bufferedReader.readLine()) != null ) {
                stringBuilder.append(receiveString);
            }

            ret = stringBuilder.toString();
        }
        catch (FileNotFoundException e) {
            Toast.makeText(context, "File not Found: " + e.getMessage() , Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            Toast.makeText(context, "Cannot read the file", Toast.LENGTH_SHORT).show();
        }
        return ret;
    }
    ***********************************************************************************************************/

    //metodo che da un ID restituisce una stringa letta
    public String[] getDescriptionFromId (String id) {

        String[] dati = new String[0]; //creo un array di array di stringhe VUOTO!

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(filename))); //apro il file 'filename' che è nella cartella assets di questo contesto

            String str = "";

            //controllo che il file non sia finito
            while ((str = br.readLine()) != null) {
                String[] linesplitted = str.split("-"); //divido la stringa dove trovo un '-': la prima parte avrà l'id, la seconda i tipi e la terza la descrizione
                if (linesplitted[0].equals(id)) { //se il numero è uguale al mio ID
                    dati[0] = linesplitted[1]; //il tipo che per ora è sempre 1 solo
                    dati[1] = linesplitted[2]; //la descrizione
                    return dati; //restituisco l'array con i dati
                }
            }
        }
        catch (FileNotFoundException e) {
            Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            Toast.makeText(context, "Cannot read the file", Toast.LENGTH_SHORT).show();
        }
        dati[0] = "";
        dati[1] = "Descrizione non ancora inserita";
        return dati;
    }
}