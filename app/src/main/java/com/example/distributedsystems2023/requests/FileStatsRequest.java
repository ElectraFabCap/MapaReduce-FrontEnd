package com.example.distributedsystems2023.requests;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.example.distributedsystems2023.MainActivity;
import com.example.distributedsystems2023.SelectFilesActivity;
import com.example.distributedsystems2023.WalkStatsActivity;
import com.example.distributedsystems2023.databinding.ActivityTotalStatsBinding;
import com.example.distributedsystems2023.databinding.ActivityWalkStatsBinding;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
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
    public FileStatsRequest(WalkStatsActivity activity,  String ip, String path, String username){
        this.activity = activity;
        this.ip = ip;
        this.path = path;
        this.username = username;
    }

    public void run() {
        ObjectOutputStream out= null ;
        ObjectInputStream in = null ;
        Socket requestSocket= null ;

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
            String creatorString = "creator=\"" + username + "\"";
            if (!content.contains(creatorString)){
                //i'll leave you electra to error handle :)
                this.activity.runOnUiThread(
                        new Runnable()
                        {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run()
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                builder.setTitle("Warning!")
                                        .setMessage("Selected file does not match the user.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int which) {
                                                activity.finish();
                                                dialog.dismiss();
                                            }
                                        });

                                AlertDialog alertDialog = builder.create();

                                if(!activity.isFinishing())
                                    alertDialog.show();
                            }
                        }
                );
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
                                (double) Math.round(currentWalkStats.getTotalDistance()) / 1000
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

        } catch (UnknownHostException | ConnectException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");

            this.activity.runOnUiThread(
                    new Runnable()
                    {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run()
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("Warning!")
                                    .setMessage("Unable to connect to server.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(activity.getApplicationContext(),  MainActivity.class);
                                            activity.startActivity(intent);

                                            dialog.dismiss();
                                        }
                                    });

                            AlertDialog alertDialog = builder.create();

                            if(!activity.isFinishing())
                                alertDialog.show();
                        }
                    }
            );

        } catch (Exception e) {

            this.activity.runOnUiThread(
                    new Runnable()
                    {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run()
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("Warning!")
                                    .setMessage("Could not fetch data from the server.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(activity.getApplicationContext(),  MainActivity.class);
                                            activity.startActivity(intent);

                                            dialog.dismiss();
                                        }
                                    });

                            AlertDialog alertDialog = builder.create();

                            if(!activity.isFinishing())
                                alertDialog.show();
                        }
                    }
            );

            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
                if (requestSocket != null)
                    requestSocket.close();
            }
            catch(Exception e){
                Log.e(TAG, "Error message", e);
                e.printStackTrace();
            }
        }
    }
}
