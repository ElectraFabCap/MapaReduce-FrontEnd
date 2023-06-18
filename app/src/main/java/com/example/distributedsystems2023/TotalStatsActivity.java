package com.example.distributedsystems2023;

import android.os.Bundle;
import android.widget.Button;

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
    }

    private void assignBackButtonListener() {

    }

}
