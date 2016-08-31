package com.nikitakozlov.pury_example;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.nikitakozlov.pury.async.StartProfiling;
import com.nikitakozlov.pury.async.StartProfilings;
import com.nikitakozlov.pury.async.StopProfiling;
import com.nikitakozlov.pury.async.StopProfilings;
import com.nikitakozlov.pury.method.ProfileMethod;
import com.nikitakozlov.pury.method.ProfileMethods;
import com.nikitakozlov.pury_example.profilers.StartApp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @ProfileMethod(methodId = StartApp.METHOD_ID, stageName = StartApp.MAIN_ACTIVITY_CREATE,
            stageOrder = StartApp.MAIN_ACTIVITY_CREATE_ORDER)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate");
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
    }

    @ProfileMethod(methodId = StartApp.METHOD_ID, stageName = StartApp.MAIN_ACTIVITY_START,
            stageOrder = StartApp.MAIN_ACTIVITY_START_ORDER)
    @Override
    protected void onStart() {
        super.onStart();
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @StopProfiling(methodId = StartApp.METHOD_ID, stageName = StartApp.TOP_STAGE)
    @Override
    protected void onResume() {
        super.onResume();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
