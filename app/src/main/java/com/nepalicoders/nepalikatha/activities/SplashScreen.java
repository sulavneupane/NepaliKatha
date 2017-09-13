package com.nepalicoders.nepalikatha.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nineoldandroids.animation.Animator;
import com.nepalicoders.nepalikatha.R;
import com.nepalicoders.nepalikatha.constants.Names;

public class SplashScreen extends AppCompatActivity {

    ImageView logo;
    TextView description;
    CircularProgressView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPreferences = getSharedPreferences(Names.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        final boolean firstTime = sharedPreferences.getBoolean(Names.FIRST_TIME, true);

        ///Pushbots.sharedInstance().init(this);
        //Tracker mTracker = ((App)getApplication()).getDefaultTracker();
        //mTracker.setScreenName("SplashScreen");
        //mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        description = (TextView) findViewById(R.id.welcome);

        YoYo.with(Techniques.FadeIn).duration(2000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //if (firstTime) {
                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                        finish();
                                /*} else {
                                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                                    finish();
                                }*/
                    }
                }, 1000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(description);

        description.setVisibility(View.VISIBLE);

    }

}
