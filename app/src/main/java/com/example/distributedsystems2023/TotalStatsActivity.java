package com.example.distributedsystems2023;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distributedsystems2023.databinding.ActivityTotalStatsBinding;

public class TotalStatsActivity extends AppCompatActivity {

    private ActivityTotalStatsBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTotalStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.assignBackButtonListener();
        this.loadValues();
    }

    private void loadValues() {
        //TODO: create socket connection and fetch the 4 float values from the server, then put them as arguments for the 4 setText() methods and uncomment them
        //binding.DistValue.setText();
        //binding.ElevationValue.setText();
        //binding.TimeValue.setText();
        //binding.SpeedValue.setText();
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
