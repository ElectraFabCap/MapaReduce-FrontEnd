package com.example.distributedsystems2023;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distributedsystems2023.databinding.ActivityTotalStatsBinding;
import com.example.distributedsystems2023.requests.TotalStatsRequest;

public class TotalStatsActivity extends AppCompatActivity {

    private ActivityTotalStatsBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String ip = ((GPXApplication) this.getApplication()).getMasterIP();

        binding = ActivityTotalStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.assignBackButtonListener();
        this.loadValues(ip);
    }

    private void loadValues(String ip) {
        TotalStatsRequest request = new TotalStatsRequest( this.binding, ip);
        request.start();
    }

    private void assignBackButtonListener() {
        binding.BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
