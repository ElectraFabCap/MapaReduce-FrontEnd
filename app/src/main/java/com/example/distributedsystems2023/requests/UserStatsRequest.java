package com.example.distributedsystems2023.requests;

import com.example.distributedsystems2023.databinding.ActivityTotalStatsBinding;
import com.example.distributedsystems2023.databinding.ActivityUserStatsBinding;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;

import utils.GPXStatistics;

public class UserStatsRequest extends Thread{
    private ActivityUserStatsBinding binding;
    private String ip;
    private String username;

    public UserStatsRequest(ActivityUserStatsBinding binding, String ip, String username){
        this.binding = binding;
        this.ip = ip;
        this.username = username;
    }

    public void run() {
        DataOutputStream out= null ;
        ObjectInputStream in = null ;
        Socket requestSocket= null ;

        try {
            requestSocket = new Socket(this.ip,60001);
            out = new DataOutputStream(requestSocket.getOutputStream());

            out.writeUTF(this.username);
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
        } catch (Exception e) {
                e.printStackTrace();
        }finally {
            try {
                in.close();	out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
