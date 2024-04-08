package com.example.shop_app;

import static java.lang.Thread.*;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.Intent;




import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button buttonSignIn, buttonSignUp;
    private AnimatorSet orangeCircleSetOfAnimators, lightBlueCircleSetOfAnimators, blueCircleSetOfAnimators, redCircleSetOfAnimators;
    private View orangeCircle, lightBlueCircle, blueCircle, redCircle;
    private static AnimatorSet makeSetOfAnimators(View animatedViewObject, long fadeOutAnimatiorDuration, long fadeInAnimatorDuration, long setOfAnimatorsDelay) {

        final ValueAnimator fadeOutAnimator = ValueAnimator.ofFloat(1f, 0f);
        fadeOutAnimator.setDuration(fadeOutAnimatiorDuration);
        fadeOutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                animatedViewObject.setAlpha(alpha);
            }
        });


        final ValueAnimator fadeInAnimator = ValueAnimator.ofFloat(0f, 1f);
        fadeInAnimator.setDuration(fadeInAnimatorDuration);
        fadeInAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                animatedViewObject.setAlpha(alpha);
            }
        });


        AnimatorSet setOfAnimators = new AnimatorSet();
        setOfAnimators.setStartDelay(setOfAnimatorsDelay);
        setOfAnimators.play(fadeOutAnimator).before(fadeInAnimator);

        setOfAnimators.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setOfAnimators.start();
            }
        });


        return setOfAnimators;
    }


    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        splashScreen.setKeepOnScreenCondition(() -> {
            try {
                Thread.sleep(400);
                return false;
            } catch (Exception e) {
                return false;
            }
        });

        /*

        splashScreen.setKeepOnScreenCondition(() -> {
            Instant startTime = Instant.now();
            long endTime = 2;

            while((Duration.between(startTime,Instant.now())).getSeconds() < endTime){} // pojebane ale niech zostanie na pamiatkę - też działa ;)
            return false;
        });

        */

        splashScreen.setOnExitAnimationListener(splashScreenViewProvider -> {
            ObjectAnimator endScreenAnimation = ObjectAnimator.ofFloat(splashScreenViewProvider.getView(), "alpha", 1.0f, 0.0f);
            endScreenAnimation.setDuration(1000);


            ValueAnimator firstEndAnimation = ValueAnimator.ofFloat(1.0f, 0.0f);
            firstEndAnimation.setDuration(500);
            firstEndAnimation.setInterpolator(new AnticipateOvershootInterpolator());

            firstEndAnimation.addUpdateListener(anim -> {
                splashScreenViewProvider.getIconView().setScaleX((Float) firstEndAnimation.getAnimatedValue());
            });


            ValueAnimator secondEndAnimation = ValueAnimator.ofFloat(1.0f, 0.0f);
            secondEndAnimation.setDuration(400);
            secondEndAnimation.setInterpolator(new AnticipateOvershootInterpolator());

            secondEndAnimation.addUpdateListener(anim -> {
                splashScreenViewProvider.getIconView().setScaleY((Float) secondEndAnimation.getAnimatedValue());
            });


            AnimatorSet endAnimations = new AnimatorSet();
            endAnimations.play(firstEndAnimation).with(secondEndAnimation);

            endAnimations.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    splashScreenViewProvider.remove();
                }
            });



            endScreenAnimation.start();
            endAnimations.start();
        });

        orangeCircle = findViewById(R.id.first_orange_circle);
        lightBlueCircle = findViewById(R.id.first_light_blue_circle);
        blueCircle = findViewById(R.id.first_blue_circle);
        redCircle = findViewById(R.id.first_red_circle);

        orangeCircleSetOfAnimators = makeSetOfAnimators(orangeCircle,5000,5000,700);
        lightBlueCircleSetOfAnimators = makeSetOfAnimators(lightBlueCircle,5500,5500,700);
        blueCircleSetOfAnimators = makeSetOfAnimators(blueCircle,6000,6000,700);
        redCircleSetOfAnimators = makeSetOfAnimators(redCircle,6000,6000,700);

        orangeCircleSetOfAnimators.start();
        lightBlueCircleSetOfAnimators.start();
        blueCircleSetOfAnimators.start();
        redCircleSetOfAnimators.start();


        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getApplicationContext(),R.animator.button_animation));
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainPageActivity.class);
                startActivity(intent);
            }
        });

        buttonSignUp = findViewById(R.id.buttonSignUp1);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MainActivity", "I'm in onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        orangeCircleSetOfAnimators.pause();
        lightBlueCircleSetOfAnimators.pause();
        blueCircleSetOfAnimators.pause();
        redCircleSetOfAnimators.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        orangeCircleSetOfAnimators.resume();
        lightBlueCircleSetOfAnimators.resume();
        blueCircleSetOfAnimators.resume();
        redCircleSetOfAnimators.resume();
    }
}


