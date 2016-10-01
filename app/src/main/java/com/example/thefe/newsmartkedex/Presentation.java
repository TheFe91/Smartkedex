package com.example.thefe.newsmartkedex;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

 /*
  * Created by TheFe on 01/10/2016.
  */

public class Presentation {

    private Button b1, b2;
    private Context context;
    private TextToSpeech t1;

    //metodo costruttore dell'oggetto Presentation
    public Presentation (Button b1, Button b2, Context nContext) {
        this.b1 = b1;
        this.b2 = b2;
        this.context = nContext;
    }

    public int presentati () {

        t1=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ITALIAN);
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
        return 0;
    }
}