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

    public ReadFileFacade (Context nContext, String filename) {
        this.filename = filename;
        this.context = nContext;
    }

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
}
