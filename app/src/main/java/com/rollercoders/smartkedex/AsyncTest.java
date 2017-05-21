package com.rollercoders.smartkedex;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

/**
 * Created by TheFe on 20/05/17.
 */

public class AsyncTest extends AppCompatActivity implements WebServicesAsyncResponse {
    BackgroundWorker backgroundWorker = new BackgroundWorker("getWeakness", 100);
    EditText editText;
    TextView textView, log;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        log = (TextView)findViewById(R.id.log);
        editText = (EditText)findViewById(R.id.editowner);
        backgroundWorker.delegate = this;
        Button button = (Button)findViewById(R.id.starttest);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundWorker.execute();
                textView = (TextView)findViewById(R.id.ownerresult);
                try {
                    log.append(backgroundWorker.get());
                } catch (InterruptedException | ExecutionException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void processFinish(String output) {
        backgroundWorker = new BackgroundWorker("getPokeTypes", 100);
        backgroundWorker.delegate = this;
    }
}
