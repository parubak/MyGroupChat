package com.example.mychat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView image = findViewById(R.id.image);
        auth = FirebaseAuth.getInstance();

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim);
        image.startAnimation(animation);

        new Handler().postDelayed(() -> {
            startActivity();
            finish();
        },1500);
    }

    private void startActivity(){
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }else{
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }
}