package com.example.distributedsystems2023;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distributedsystems2023.databinding.ActivityWalkStatsBinding;
import com.example.distributedsystems2023.utils.GPXFile;
import com.example.distributedsystems2023.utils.GPXStatistics;

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

        binding = ActivityWalkStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.assignBackButtonListener();
        this.loadValues();
    }

    private void loadValues() {
        //TODO: GET IP, GPX PATH FROM APP
        //TODO: MAYBE SHOW ERROR IN UI?
        //TODO: DIAFORA ME MESO ORO

        ObjectOutputStream out= null ;
        ObjectInputStream in = null ;
        Socket requestSocket= null ;

        //TODO: CHECK IF FILE USERNAME IS SAME WITH THE CURRENT LOGGED IN USER
        try {
            /* Create socket for contacting the server on port 60000*/
            requestSocket = new Socket("localhost",60000);

            /* Create the streams to send and receive data from server */
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            GPXFile file = new GPXFile("src\\gpxs\\route1.gpx"); //MIGHT NOT BE CORRECT
            out.writeObject(file);
            out.flush();

            HashMap<String, GPXStatistics> res = (HashMap<String,GPXStatistics>) in.readObject();
            GPXStatistics currentWalkStats = res.get("currentRun");
//            GPXStatistics userAverage = res.get("userAverage");
//            GPXStatistics totalAverage = res.get("totalAverage");
//            System.out.println("Current Walk: " + currentWalkStats.toString());
//            System.out.println("User Average: " + userAverage.toString());
//            System.out.println("Total Average: " + totalAverage.toString() + "\n");

            binding.DistValue.setText(String.valueOf(currentWalkStats.getTotalDistance()));
            binding.ElevationValue.setText(String.valueOf(currentWalkStats.getTotalElevation()));
            binding.TimeValue.setText(String.valueOf(currentWalkStats.getTotalExerciseTime()));
            binding.SpeedValue.setText(String.valueOf(currentWalkStats.getAverageSpeed()));

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");

        }catch(ClassNotFoundException e){

            throw new RuntimeException(e);
        } catch (IOException ioException) {
            ioException.printStackTrace();
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
