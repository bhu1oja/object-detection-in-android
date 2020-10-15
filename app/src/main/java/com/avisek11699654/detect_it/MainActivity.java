package com.avisek11699654.detect_it;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.avisek11699654.detect_it.Activities.DetectActivity;
import com.avisek11699654.detect_it.Activities.HistoryActivity;

public class MainActivity extends AppCompatActivity {


private Button btnSelectImage, btnViewHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSelectImage = findViewById(R.id.btn_select_image);
        btnViewHistory = findViewById(R.id.btn_history);


        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          Intent intent = new Intent(MainActivity.this, DetectActivity.class);
          startActivity(intent);

            }
        });

        btnViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
    }





}