package com.devinsterling.courseregistrationwaitinglist;
/* 
    Devin Sterling
    2022 - 07 - 26
    Course Registration Waiting List
*/

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide(); // Hide Title Bar

        /* Set Version Text */
        TextView textView = findViewById(R.id.splash_version);
        textView.setText(getString(R.string.version, BuildConfig.VERSION_NAME));

        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, MainCourseActivity.class));
            finish();
        }, 2500);
    }
}