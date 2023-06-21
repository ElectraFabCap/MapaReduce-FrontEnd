package com.example.distributedsystems2023.requests;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.example.distributedsystems2023.WalkStatsActivity;
import com.example.distributedsystems2023.databinding.ActivityTotalStatsBinding;
import com.example.distributedsystems2023.databinding.ActivityWalkStatsBinding;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import utils.GPXFile;
import utils.GPXStatistics;

public class FileStatsRequest extends Thread {
    private WalkStatsActivity activity;
    private String ip;

    private String path;

    private String username;
    public FileStatsRequest(WalkStatsActivity activity, String ip, String path, String username){
        this.activity = activity;
        this.ip = ip;
        this.path = path;
        this.username = username;
    }

    public void run() {
        ObjectOutputStream out= null ;
        ObjectInputStream in = null ;
        Socket requestSocket= null ;

        //TODO: CHECK IF FILE USERNAME IS SAME WITH THE CURRENT LOGGED IN USER
        try {
            /* Create socket for contacting the server on port 60000*/
            requestSocket = new Socket(this.ip,60000);

            /* Create the streams to send and receive data from server */
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            GPXFile file = new GPXFile(this.path); //MIGHT NOT BE CORRECT
            String content = file.getContentAsString();
            System.out.println("FILE CONTENT: " + file.getContentAsString());
            System.out.println("USER: " + username);
            if (!content.contains(this.username)){
                //i'll leave you electra to error handle :)
                return;
            }
            out.writeObject(file);
            out.flush();

            HashMap<String, GPXStatistics> res = (HashMap<String,GPXStatistics>) in.readObject();
            GPXStatistics currentWalkStats = res.get("currentRun");
            GPXStatistics averageWalkStats = res.get("totalAverage");
            assert currentWalkStats != null;
            assert averageWalkStats != null;
            double[] statComparison = currentWalkStats.compare(averageWalkStats);

            this.activity.runOnUiThread(
                new Runnable()
                {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run()
                    {
                        activity.getBinding().DistValue.setText(String.valueOf(
                                Math.round(currentWalkStats.getTotalDistance()) / 1000
                        ));
                        activity.getBinding().ElevationValue.setText(String.valueOf(
                                (int) currentWalkStats.getTotalElevation()
                        ));
                        activity.getBinding().TimeValue.setText(String.valueOf(
                                currentWalkStats.getTotalExerciseTime()
                        ));
                        activity.getBinding().SpeedValue.setText(String.valueOf(
                                (double) Math.round(currentWalkStats.getAverageSpeed() * 100) / 100
                        ));
                        if (currentWalkStats.getTotalDistance() != 0) {activity.getBinding().DistPerc.setText((statComparison[0])+"%");}
                        if (currentWalkStats.getTotalExerciseTimeInSeconds() != 0) {activity.getBinding().TimePerc.setText((statComparison[1])+"%");}
                        if (currentWalkStats.getTotalElevation() != 0) {activity.getBinding().ElePerc.setText((statComparison[2])+"%");}
                        if (currentWalkStats.getAverageSpeed() != 0) {activity.getBinding().SpeedPerc.setText((statComparison[3])+"%");}
                    }
                }
            );

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        }catch(ClassNotFoundException e){
            throw new RuntimeException(e);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();	out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
