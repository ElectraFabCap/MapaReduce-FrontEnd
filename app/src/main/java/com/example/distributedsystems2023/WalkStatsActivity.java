package com.example.distributedsystems2023;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    private Handler handler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String ip = ((GPXApplication) this.getApplication()).getMasterIP();
        String username = ((GPXApplication) this.getApplication()).getUsername();

        binding = ActivityWalkStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.assignBackButtonListener();

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        //hopefully this will never be null

        this.loadValues(ip, path, username);

    }

    private void loadValues(String ip, String path, String username) {
        //TODO: MAYBE SHOW ERROR IN UI?
        //TODO: DIAFORA ME MESO ORO
        //TODO: CHECK IF FILE USERNAME IS SAME WITH THE CURRENT LOGGED IN USER

        FileStatsRequest request = new FileStatsRequest(WalkStatsActivity.this, ip, path , username);
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

    public ActivityWalkStatsBinding getBinding(){
        return binding;
    }
}
