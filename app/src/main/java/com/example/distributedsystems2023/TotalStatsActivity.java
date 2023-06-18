package com.example.distributedsystems2023;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distributedsystems2023.databinding.ActivityTotalStatsBinding;
import com.example.distributedsystems2023.utils.GPXStatistics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

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
        //TODO: GET IP FROM APP
        //TODO: SELECT IF YOU WANT TOTAL STATS OR TOTAL AVERAGE STATS
        //TODO: MAYBE SHOW ERROR IN UI?

        ObjectInputStream in = null ;
        ObjectOutputStream out = null;
        Socket requestSocket= null ;


        try {
            requestSocket = new Socket("localhost",60002);


            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.writeObject(null);
            out.flush();

            in = new ObjectInputStream(requestSocket.getInputStream());
            HashMap<String, GPXStatistics> res = (HashMap<String,GPXStatistics>) in.readObject();
            //GPXStatistics totalAverageStats = res.get("totalAverageStats");
            GPXStatistics totalStats = res.get("totalStats");

            //System.out.println("Total Average: " + totalAverageStats.toString());
            //System.out.println("Total: " + totalStats.toString());

            binding.DistValue.setText(String.valueOf(totalStats.getTotalDistance()));
            binding.ElevationValue.setText(String.valueOf(totalStats.getTotalElevation()));
            binding.TimeValue.setText(String.valueOf(totalStats.getTotalExerciseTime()));
            binding.SpeedValue.setText(String.valueOf(totalStats.getAverageSpeed()));

        } catch (IOException ioException) {
            ioException.printStackTrace();

            binding.DistValue.setText("N/A");
            binding.ElevationValue.setText("N/A");
            binding.TimeValue.setText("N/A");
            binding.SpeedValue.setText("N/A");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
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
