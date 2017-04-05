package com.rollercoders.smartkedex;

import android.content.Context;
import android.speech.tts.TextToSpeech;

 /*
  * Created by TheFe on 01/10/2016.
  */

class Speaking {

    private Context context;
    private TextToSpeech tts;
    private String owner = "";
    private String smartkedex = "";
    private String toSpeak = "";

    //metodo costruttore dell'oggetto Speaking
    Speaking(Context nContext) {
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
                    if (!smartkedex.equals("")) //l'utente ha settato sia il suo nome che quello dello smartkedex
                        toSpeak = context.getResources().getString(context.getResources().getIdentifier("full_presentation", "string", context.getPackageName()),smartkedex, owner);
                    else //l'utente non ha settato il nome dello Smartkédex, ma il suo sì
                        toSpeak = context.getResources().getString(context.getResources().getIdentifier("partial_presentation", "string", context.getPackageName()), owner);
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                } //end if
            } //end method onInit()
        }); //end TextToSpeech.OnInitListener
    } //end method presentati()
} //end class Speaking