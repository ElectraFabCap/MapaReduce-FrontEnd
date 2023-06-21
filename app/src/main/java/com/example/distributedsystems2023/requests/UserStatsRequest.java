package com.example.distributedsystems2023.requests;

import com.example.distributedsystems2023.UserStatsActivity;
import com.example.distributedsystems2023.databinding.ActivityTotalStatsBinding;
import com.example.distributedsystems2023.databinding.ActivityUserStatsBinding;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;

import utils.GPXStatistics;

public class UserStatsRequest extends Thread{
    private UserStatsActivity activity;
    private String ip;
    private String username;

    public UserStatsRequest(UserStatsActivity activity, String ip, String username){
        this.activity = activity;
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

            this.activity.runOnUiThread(
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            activity.getBinding().UserName.setText(userTotal.getUser());
                            activity.getBinding().DistValue.setText(String.valueOf(
                                    Math.round(userTotal.getTotalDistance()) / 1000
                            ));
                            activity.getBinding().ElevationValue.setText(String.valueOf(
                                    (int) userTotal.getTotalElevation()
                            ));
                            activity.getBinding().TimeValue.setText(String.valueOf(
                                    userTotal.getTotalExerciseTime()
                            ));
                            activity.getBinding().SpeedValue.setText(String.valueOf(
                                    (double) Math.round(userTotal.getAverageSpeed() * 100) / 100
                            ));
                        }
                    }
            );

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
