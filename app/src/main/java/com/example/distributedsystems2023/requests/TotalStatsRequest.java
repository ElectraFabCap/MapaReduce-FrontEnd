package com.example.distributedsystems2023.requests;

import com.example.distributedsystems2023.TotalStatsActivity;
import com.example.distributedsystems2023.databinding.ActivityTotalStatsBinding;
import utils.GPXStatistics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class TotalStatsRequest extends Thread{

    private TotalStatsActivity activity;
    private String ip;

    public TotalStatsRequest(TotalStatsActivity activity, String ip){
        this.activity = activity;
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

            this.activity.runOnUiThread(
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            activity.getBinding().DistValue.setText(String.valueOf(
                                    (double) Math.round(totalStats.getTotalDistance()) / 1000
                            ));
                            activity.getBinding().ElevationValue.setText(String.valueOf(
                                    (int) totalStats.getTotalElevation()
                            ));
                            activity.getBinding().TimeValue.setText(String.valueOf(
                                    totalStats.getTotalExerciseTime()
                            ));
                            activity.getBinding().SpeedValue.setText(String.valueOf(
                                    (double) Math.round(totalStats.getAverageSpeed() * 100) / 100
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
