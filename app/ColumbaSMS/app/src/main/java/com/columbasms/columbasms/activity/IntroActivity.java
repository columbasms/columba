package com.columbasms.columbasms.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.columbasms.columbasms.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Federico on 04/02/16.
 */
public class IntroActivity extends AppIntro {

    // Please DO NOT override onCreate. Use init.
    @Override
    public void init(Bundle savedInstanceState) {

        addSlide(AppIntroFragment.newInstance("Scegli", "Seleziona le campagne che vuoi supportare e"
                + " decidi chi rendere partecipe!", R.drawable.colomba_icns,
                Color.parseColor("#009688")));
        addSlide(AppIntroFragment.newInstance("Dona", "Diffondi iniziative benifiche donando i tuoi "
                + "SMS inutilizzati!", R.drawable.colomba_icns, Color.parseColor("#009688")));
        addSlide(AppIntroFragment.newInstance("Condividi", "Sii social! Nella schermata principale "
                + "potrai trovare e mostrare tutte le campagne e le iniziative promosse e molto altro!",
                R.drawable.colomba_icns, Color.parseColor("#009688")));
        addSlide(AppIntroFragment.newInstance("Competi!", "Mostra a tutti chi è il migliore! "
                + "Diventa il miglior Columber della tua zona e scala la classifica!",
                R.drawable.colomba_icns, Color.parseColor("#009688")));

        setBarColor(Color.parseColor("#00796B"));
        setSeparatorColor(Color.parseColor("#B2DFDB"));


        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        /*
        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
        */

    }

    @Override
    public void onSkipPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity ( intent );
        finish();
    }

    @Override
    public void onDonePressed() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity ( intent );
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }


}
