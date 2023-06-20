package com.example.distributedsystems2023.requests;

import com.example.distributedsystems2023.databinding.ActivityTotalStatsBinding;
import utils.GPXStatistics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class TotalStatsRequest extends Thread{

    private ActivityTotalStatsBinding binding;
    private String ip;

    public TotalStatsRequest(ActivityTotalStatsBinding binding, String ip){
        this.binding = binding;
        this.ip = ip;
    }

    @Override
    public void run() {
        ObjectInputStream in = null ;
        ObjectOutputStream out = null;
        Socket requestSocket= null ;


        try {
            requestSocket = new Socket(this.ip,60002);


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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
