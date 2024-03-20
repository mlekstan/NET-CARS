package com.example.shop_app;

import static java.lang.Thread.*;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.Duration;
import java.time.Instant;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        splashScreen.setKeepOnScreenCondition(() -> {
            try {
                Thread.sleep(0);
                return false;
            } catch (Exception e) {
                return false;
            }
        });

        /*

        splashScreen.setKeepOnScreenCondition(() -> {
            Instant startTime = Instant.now();
            long endTime = 2;

            while((Duration.between(startTime,Instant.now())).getSeconds() < endTime){}
            return false;
        });

        */

        splashScreen.setOnExitAnimationListener(splashScreenViewProvider -> {
            ObjectAnimator endScreenAnimation = ObjectAnimator.ofFloat(splashScreenViewProvider.getView(), "alpha", 1f, 0f);
            endScreenAnimation.setDuration(500);


            ValueAnimator firstEndAnimation = ValueAnimator.ofFloat(1f,0f);
            firstEndAnimation.setDuration(500);

            firstEndAnimation.addUpdateListener(anim -> {
                splashScreenViewProvider.getIconView().setAlpha((Float) firstEndAnimation.getAnimatedValue());
            });


            ValueAnimator secondEndAnimation = ValueAnimator.ofFloat(1f,0.0f);
            secondEndAnimation.setDuration(500);
            secondEndAnimation.setInterpolator(new OvershootInterpolator());

            secondEndAnimation.addUpdateListener(anim -> {
                splashScreenViewProvider.getIconView().setScaleX((Float) secondEndAnimation.getAnimatedValue());
            });


            ValueAnimator thirdEndAnimation = ValueAnimator.ofFloat(1f,0.0f);
            thirdEndAnimation.setDuration(500);
            thirdEndAnimation.setInterpolator(new OvershootInterpolator());

            thirdEndAnimation.addUpdateListener(anim -> {
                splashScreenViewProvider.getIconView().setScaleY((Float) thirdEndAnimation.getAnimatedValue());
            });



            AnimatorSet endAnimations = new AnimatorSet();
            /*
            endAnimations.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    splashScreenViewProvider.remove();
                }
            });
            */

            endAnimations.play(firstEndAnimation).with(secondEndAnimation);
            endAnimations.play(firstEndAnimation).with(thirdEndAnimation);
            endAnimations.play(secondEndAnimation).with(thirdEndAnimation);
            //endAnimations.play(endScreenAnimation).after(thirdEndAnimation);
            endAnimations.start();
            endScreenAnimation.start();
        });


        /*
        splashScreen.setOnExitAnimationListener(splashScreenViewProvider -> {
            final ObjectAnimator slideUp = ObjectAnimator.ofFloat(splashScreenViewProvider.getView(),(View.ALPHA), 1f, 0f);
            slideUp.setInterpolator(new AnticipateInterpolator());
            slideUp.setDuration(200L);

            // Call SplashScreenView.remove at the end of your custom animation.
            slideUp.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    splashScreenViewProvider.remove();
                }
            });

            // Run your animation.
            slideUp.start();
        });
        */

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}