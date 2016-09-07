package com.nikitakozlov.pury_example;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.nikitakozlov.pury.annotations.StopProfiling;
import com.nikitakozlov.pury.annotations.ProfileMethod;
import com.nikitakozlov.pury_example.profilers.StartApp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @ProfileMethod(profilerName = StartApp.PROFILER_NAME, stageName = StartApp.MAIN_ACTIVITY_CREATE,
            stageOrder = StartApp.MAIN_ACTIVITY_CREATE_ORDER)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate");
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ArticleListActivity.class));
            }
        });
    }

    @ProfileMethod(profilerName = StartApp.PROFILER_NAME, stageName = StartApp.MAIN_ACTIVITY_START,
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

    @StopProfiling(profilerName = StartApp.PROFILER_NAME, stageName = StartApp.TOP_STAGE)
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
