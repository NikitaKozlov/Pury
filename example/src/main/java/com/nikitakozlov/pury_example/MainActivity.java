package com.nikitakozlov.pury_example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.nikitakozlov.pury.async.StartProfiling;
import com.nikitakozlov.pury.async.StopProfiling;
import com.nikitakozlov.pury.method.ProfileMethod;
import com.nikitakozlov.pury.method.ProfileMethods;

public class MainActivity extends AppCompatActivity {

    @ProfileMethods(value = {
            @ProfileMethod(runsCounter = 10, methodId = "onCreate")
    })
    @ProfileMethod(methodId = "onCreate")
    @StartProfiling(runsCounter = 3, methodId = "launch Activity")
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


    @Override
    @StopProfiling(runsCounter = 3, methodId = "launch Activity")
    protected void onResume() {
        super.onResume();
    }
}
