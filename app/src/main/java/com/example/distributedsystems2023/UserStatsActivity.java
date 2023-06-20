package com.example.distributedsystems2023;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distributedsystems2023.databinding.ActivityUserStatsBinding;
import com.example.distributedsystems2023.requests.UserStatsRequest;

import utils.GPXStatistics;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;

public class UserStatsActivity extends AppCompatActivity {

    private ActivityUserStatsBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String username = ((GPXApplication) this.getApplication()).getUsername();
        String ip = ((GPXApplication) this.getApplication()).getMasterIP();
        binding = ActivityUserStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.assignBackButtonListener();
        this.loadValues(ip, username);
    }

    private void loadValues(String ip, String username) {
        //TODO: SELECT IF YOU WANT USER STATS OR USER AVERAGE STATS
        //TODO: MAYBE SHOW ERROR IN UI?
        System.out.println(ip);
        UserStatsRequest request = new UserStatsRequest(this.binding, ip, username);
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
