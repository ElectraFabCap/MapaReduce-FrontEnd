package com.example.distributedsystems2023;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distributedsystems2023.databinding.ActivityUserStatsBinding;
import com.example.distributedsystems2023.utils.GPXStatistics;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;

public class UserStatsActivity extends AppCompatActivity {

    private ActivityUserStatsBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.assignBackButtonListener();
        this.loadValues();
    }

    private void loadValues() {
        //TODO: GET IP AND USERNAME FROM APP
        //TODO: SELECT IF YOU WANT USER STATS OR USER AVERAGE STATS
        //TODO: MAYBE SHOW ERROR IN UI?

        DataOutputStream out= null ;
        ObjectInputStream in = null ;
        Socket requestSocket= null ;

        try {
            requestSocket = new Socket("localhost",60001);
            out = new DataOutputStream(requestSocket.getOutputStream());

            out.writeUTF("user1");
            out.flush();

            in = new ObjectInputStream(requestSocket.getInputStream());
            HashMap<String, GPXStatistics> res = (HashMap<String,GPXStatistics>) in.readObject();
            //GPXStatistics userAverages = res.get("userAverageStats");
            GPXStatistics userTotal = res.get("userTotalStats");

            //System.out.println("User Average: " + userAverages.toString());
            //System.out.println("User Total: " + userTotal.toString());

            binding.UserName.setText(userTotal.getUser());
            binding.DistValue.setText(String.valueOf(userTotal.getTotalDistance()));
            binding.ElevationValue.setText(String.valueOf(userTotal.getTotalElevation()));
            binding.TimeValue.setText(String.valueOf(userTotal.getTotalExerciseTime()));
            binding.SpeedValue.setText(String.valueOf(userTotal.getAverageSpeed()));

        } catch (IOException ioException) {
            ioException.printStackTrace();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();	out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
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
