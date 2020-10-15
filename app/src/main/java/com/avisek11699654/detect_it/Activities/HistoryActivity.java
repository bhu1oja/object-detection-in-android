package com.avisek11699654.detect_it.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.avisek11699654.detect_it.Adapters.SQLAdapter;
import com.avisek11699654.detect_it.R;
import com.avisek11699654.detect_it.common.FruitModel;
import com.avisek11699654.detect_it.common.MySQLITE;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    // Linear Layout Manager
    LinearLayoutManager HorizontalLayout;

    // Layout Manager
    RecyclerView.LayoutManager   RecyclerViewHistoryLayoutManager;

    // adapter class object
    RecyclerView.Adapter adapter;

    //database
    private MySQLITE mySQLITE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mySQLITE = new MySQLITE(HistoryActivity.this);
        ArrayList<FruitModel> allFruits = mySQLITE.listFruits();

        RecyclerView rvHistory = findViewById(R.id.recyclerviewHistory);
        RecyclerViewHistoryLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvHistory.setLayoutManager(RecyclerViewHistoryLayoutManager);
        rvHistory.setHasFixedSize(true);

        //show db element codition
        if (allFruits.size() > 0) {
            rvHistory.setVisibility(View.VISIBLE);
            adapter = new SQLAdapter(this,allFruits);
            rvHistory.setAdapter(adapter);

        }
        else {
            rvHistory.setVisibility(View.GONE);
            Toast.makeText(this, "No fruits scanned yet.........", Toast.LENGTH_LONG).show();
        }
    }
}