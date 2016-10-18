package com.example.thefe.newsmartkedex;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

 /*
  * Created by TheFe on 01/10/2016.
  */

public class Presentation extends Activity {

    private String lang;
    private Context context;
    private TextToSpeech t1;
    private String owner = ((GlobalVariables) this.getApplication()).getOwner();
    private String smartkedex = ((GlobalVariables) this.getApplication()).getSmartkedex();
    private String toSpeak = "";

    //metodo costruttore dell'oggetto Presentation
    public Presentation (String lang, Context nContext) {
        this.lang = lang;
        this.context = nContext;
    }

    public void presentati () {

        t1=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    switch (lang) {
                        case "ITA":
                            t1.setLanguage(Locale.ITALIAN);
                            if (owner != "")
                                if (smartkedex != "") //l'utente ha settato sia il suo nome che quello dello smartkedex
                                    toSpeak = "Sono " + smartkedex + ", e sono uno Smàrtchedex in versione beta. Sono proprietà di " + owner;
                                else //l'utente non ha settato il nome dello Smartkédex, ma il suo sì
                                    toSpeak = "Sono uno Smàrtchedex in versione beta. Non ho nome. Sono proprietà di " + owner;
                            else
                                if (smartkedex != "") //l'utente ha settato il nome dello Smartkédex ma non il suo
                                    toSpeak = "Sono " + smartkedex + "e sono uno Smàrtchedex in versione beta.";
                                else //l'utente non ha settato niente
                                    toSpeak = "Sono uno Smàrtchedex in versione beta";
                            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case "ENG":
                            t1.setLanguage(Locale.ENGLISH);
                            if (owner != "")
                                if (smartkedex != "") //l'utente ha settato sia il suo nome che quello dello smartkedex
                                    toSpeak = "I'm " + smartkedex + ", and I'm a Smarktkèdex in beta version. I'm property of " + owner;
                                else //l'utente non ha settato il nome dello Smartkédex, ma il suo sì
                                    toSpeak = "I'm a Smarktkèdex in beta version. I'm property of " + owner;
                            else
                            if (smartkedex != "") //l'utente ha settato il nome dello Smartkédex ma non il suo
                                toSpeak = "I'm " + smartkedex + ", and I'm a Smarktkèdex in beta version.";
                            else //l'utente non ha settato niente
                                toSpeak = "I'm a Smartkèdex in beta version";
                            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                            break;
                    } //end switch
                } //end if
            } //end method onInit()
        }); //end TextToSpeech.OnInitListener
    } //end method presentati()
} //end class Presentation