package com.example.musica;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
public class loadingScreen extends AppCompatActivity {


    ImageView bg;
    TextView logoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        logoText = findViewById(R.id.logoText);
        bg = findViewById(R.id.bgImage);
        logoText.setAlpha(0f);

       // bg.animate().translationY(-3000f).setDuration(0).start();

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo_alpha1);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.logo_alpha2);


       // bg.animate().translationY(50f).setDuration(5000).start();
        bg.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logoText.setAlpha(1f);
                logoText.startAnimation(animation1);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Should run trackScreenMain
                        Intent intent = new Intent(loadingScreen.this, librariesMain.class);
                        loadingScreen.this.startActivity(intent);
                        loadingScreen.this.finish();
                    }
                }, 3000);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
