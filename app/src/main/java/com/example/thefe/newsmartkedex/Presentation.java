/*package com.example.thefe.newsmartkedex;

import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

 *
 * Created by TheFe on 01/10/2016.
 *

public class Presentation {

    public void presentati (Button b1, Button b2, final TextToSpeech t1) {
        b1=(Button)findViewById(R.id.button1);
        b2=(Button)findViewById(R.id.button2);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ITALIAN);
                    //t1.setPitch((float) 0.7);
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setLanguage(Locale.ITALIAN);
                String toSpeak = "Sono Caterina, e sono uno Smàrtchedex in versione beta";
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        b2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                t1.setLanguage(Locale.ENGLISH);
                String toSpeak = "I'm Lily and I'm a Smartkedex in Beta version";
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
}
*/