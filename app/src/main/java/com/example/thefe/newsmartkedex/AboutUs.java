package com.example.thefe.newsmartkedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by TheFe on 17/10/2016.
 */

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        getActionBar();

        Button contact = (Button)findViewById(R.id.contact_us);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mailClient = new Intent();
                mailClient.setAction(Intent.ACTION_SEND);
                mailClient.putExtra(Intent.EXTRA_EMAIL, "test@example.com");
                mailClient.setType("text/plain");
                startActivity(mailClient);
            }
        });
    }
}
