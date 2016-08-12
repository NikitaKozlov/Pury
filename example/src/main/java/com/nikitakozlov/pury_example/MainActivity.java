package com.nikitakozlov.pury_example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.nikitakozlov.pury.ProfileMethod;

public class MainActivity extends AppCompatActivity {

    @ProfileMethod
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate");
    }
}
