package com.example.distributedsystems2023.requests;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.example.distributedsystems2023.MainActivity;
import com.example.distributedsystems2023.TotalStatsActivity;
import com.example.distributedsystems2023.databinding.ActivityTotalStatsBinding;
import utils.GPXStatistics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
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

        } catch (UnknownHostException | ConnectException unknownHost) {
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
                                    .setMessage("Could not fetch data from server.")
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
