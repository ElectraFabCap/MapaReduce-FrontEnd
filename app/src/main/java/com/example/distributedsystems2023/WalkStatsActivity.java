package com.example.distributedsystems2023;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distributedsystems2023.databinding.ActivityWalkStatsBinding;
import com.example.distributedsystems2023.requests.FileStatsRequest;

import utils.GPXFile;
import utils.GPXStatistics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class WalkStatsActivity extends AppCompatActivity {

    private ActivityWalkStatsBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String ip = ((GPXApplication) this.getApplication()).getMasterIP();
        String file = getIntent().getStringExtra("fileURI");

        binding = ActivityWalkStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.assignBackButtonListener();
        this.loadValues(ip, file);
    }

    private void loadValues(String ip, String file) {
        //TODO: MAYBE SHOW ERROR IN UI?
        //TODO: DIAFORA ME MESO ORO
        //TODO: CHECK IF FILE USERNAME IS SAME WITH THE CURRENT LOGGED IN USER

        FileStatsRequest request = new FileStatsRequest(this.binding, ip, file);
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
