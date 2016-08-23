package com.nikitakozlov.pury_example;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.nikitakozlov.pury.async.StartProfiling;
import com.nikitakozlov.pury.async.StopProfiling;
import com.nikitakozlov.pury.method.ProfileMethod;
import com.nikitakozlov.pury.method.ProfileMethods;

public class MainActivity extends AppCompatActivity {

//    @ProfileMethods(value = {
//            @ProfileMethod(runsCounter = 10, methodId = "onCreate")
//    })
//    @ProfileMethod(methodId = "onCreate")

    @StartProfiling(runsCounter = 1, stageName = "full", methodId = "launch Activity")
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @ProfileMethod(runsCounter = 1, stageName = "create", stageOrder = 0, methodId = "launch Activity")
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

    @ProfileMethod(methodId = "launch Activity", stageName = "start", stageOrder = 2, runsCounter = 1)
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    @StopProfiling(runsCounter = 1, stageName = "full", methodId = "launch Activity")
    protected void onResume() {
        super.onResume();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
