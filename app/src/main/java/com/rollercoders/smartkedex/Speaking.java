package com.rollercoders.smartkedex;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

 /*
  * Created by TheFe on 01/10/2016.
  */

class Speaking {

    private String lang;
    private Context context;
    private TextToSpeech tts;
    private String owner = "";
    private String smartkedex = "";
    private String toSpeak = "";

    //metodo costruttore dell'oggetto Speaking
    Speaking(String lang, Context nContext) {
        this.lang = lang;
        this.context = nContext;
        PokemonDatabaseAdapter pokemonHelper = new PokemonDatabaseAdapter(nContext);
        owner = pokemonHelper.getOwner();
        smartkedex = pokemonHelper.getSmartkedex();
    }

    void presentati () {
        tts =new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    switch (lang) {
                        case "ITA":
                            tts.setLanguage(Locale.ITALIAN);
                            if (!owner.equals(""))
                                if (!smartkedex.equals("")) { //l'utente ha settato sia il suo nome che quello dello smartkedex
                                    System.err.println("sun chi");
                                    toSpeak = "Sono " + smartkedex + ", e sono uno Smàrtchedex in versione beta. Sono proprietà di " + owner;
                                }
                                else //l'utente non ha settato il nome dello Smartkédex, ma il suo sì
                                    toSpeak = "Sono uno Smàrtchedex in versione beta. Non ho nome. Sono proprietà di " + owner;
                            else
                                if (!smartkedex.equals("")) //l'utente ha settato il nome dello Smartkédex ma non il suo
                                    toSpeak = "Sono " + smartkedex + "e sono uno Smàrtchedex in versione beta.";
                                else //l'utente non ha settato niente
                                    toSpeak = "Sono uno Smàrtchedex in versione beta";
                            tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case "ENG":
                            tts.setLanguage(Locale.ENGLISH);
                            if (!owner.equals(""))
                                if (!smartkedex.equals("")) //l'utente ha settato sia il suo nome che quello dello smartkedex
                                    toSpeak = "I'm " + smartkedex + ", and I'm a Smarktkèdex in beta version. I'm property of " + owner;
                                else //l'utente non ha settato il nome dello Smartkédex, ma il suo sì
                                    toSpeak = "I'm a Smarktkèdex in beta version. I'm property of " + owner;
                            else
                            if (!smartkedex.equals("")) //l'utente ha settato il nome dello Smartkédex ma non il suo
                                toSpeak = "I'm " + smartkedex + ", and I'm a Smarktkèdex in beta version.";
                            else //l'utente non ha settato niente
                                toSpeak = "I'm a Smartkèdex in beta version";
                            tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                            break;
                    } //end switch
                } //end if
            } //end method onInit()
        }); //end TextToSpeech.OnInitListener
    } //end method presentati()
} //end class Speaking