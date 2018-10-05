package com.lolisapp.gametraductor.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.lolisapp.gametraductor.R;
import com.lolisapp.gametraductor.fragment.intro.FirstIntroFragment;

public class HowPlayActivity extends AppIntro2 {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        //addSlide(firstFragment);
        //addSlide(secondFragment);
        //addSlide(thirdFragment);
        //addSlide(fourthFragment);

        // Instead of fragments, you can also use our default slide.
        // Just create a `SliderPage` and provide title, description, background and image.
        // AppIntro will do the rest.

        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle("Ayudanos a preservar el idioma quechua ");
        //sliderPage3.setDescription("Grabate repitiendo los audios mostradps");
        sliderPage3.setImageDrawable(R.drawable.quechua);
        sliderPage3.setBgColor(Color.BLUE);
        addSlide(new FirstIntroFragment());



        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Reproduce los audios");
        sliderPage.setDescription("Reproduce uno a uno los audios que se te iran mostrando");
        sliderPage.setImageDrawable(R.drawable.play_how);
        sliderPage.setBgColor(Color.argb(255,0,167,163));
        addSlide(AppIntroFragment.newInstance(sliderPage));

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Grabate en quechua ");
        sliderPage2.setDescription("Grabate repitiendo los audios mostrados");
        sliderPage2.setImageDrawable(R.drawable.mic_how);
        sliderPage2.setBgColor(Color.argb(255,0,167,163));
        addSlide(AppIntroFragment.newInstance(sliderPage2));


        nextButton.setVisibility(View.INVISIBLE);




        showDoneButton(true);
        showSkipButton(false);
        baseProgressButtonEnabled=false;
     //   setProgressButtonEnabled(false);

        setButtonState(nextButton, false);



        // OPTIONAL METHODS
        // Override bar/separator color.
     //   setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.



    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        finish();
        Intent intent=new Intent(HowPlayActivity.this, LoginActivity.class);
        startActivity(intent);
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
