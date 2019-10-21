package com.alcpluralsight.aad_team20.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcpluralsight.aad_team20.R;

/**
 * Activity that handles all splash related logic.
 */
public class SplashActivity extends AppCompatActivity {

    private ConstraintLayout rootScene;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rootScene = findViewById(R.id.root_scene);
        shakeImage();
        loadNeedTipsText();
        loadGotYouText();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                startActivity(intent, options.toBundle());
                finish();
            }
        }, 2500);
    }

    /**
     * Performs an interpolator animation.
     * It sets an animation interpolator of type [CycleInterpolator] based on number of cycles
     * and then plays the animation.
     */
    private void shakeImage(){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        ImageView movieIcon = findViewById(R.id.img_movie_icon);
        animation.setInterpolator(new CycleInterpolator(5));
        movieIcon.startAnimation(animation);
    }

    /**
     * Performs an interpolator animation.
     * It sets an animation interpolator of type [AccelerateDecelerateInterpolator]
     * on txtNeed_Tips of type [TextView] and then plays the animation.
     */
    private void loadNeedTipsText(){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.item_animation_bottom_up);
        TextView firstText = findViewById(R.id.txtNeed_Tips);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        firstText.startAnimation(animation);
    }

    /**
     * Performs an interpolator animation.
     * It sets an animation interpolator of type [AccelerateDecelerateInterpolator]
     * on txtGot_You of type [TextView] and then plays the animation.
     */
    private void loadGotYouText(){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.item_animation_bottom_up);
        TextView secondText = findViewById(R.id.txtGot_You);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        secondText.startAnimation(animation);
    }
}
