package com.example.distributedsystems2023.requests;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import com.example.distributedsystems2023.MainActivity;
import com.example.distributedsystems2023.UserStatsActivity;
import com.example.distributedsystems2023.databinding.ActivityTotalStatsBinding;
import com.example.distributedsystems2023.databinding.ActivityUserStatsBinding;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
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
            GPXStatistics totalStats = res.get("totalAverageStats");
            assert totalStats != null;
            assert userTotal != null;
            double[] statComparison = userTotal.compare(totalStats);

            this.activity.runOnUiThread(
                    new Runnable()
                    {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run()
                        {
                            activity.getBinding().UserName.setText(userTotal.getUser());
                            activity.getBinding().DistValue.setText(String.valueOf(
                                    (double) Math.round(userTotal.getTotalDistance()) / 1000
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
                            activity.getBinding().UserDistPerc.setText((statComparison[0])+"%");
                            if (statComparison[0] > 0.0) {
                                activity.getBinding().UserDistPerc.setTextColor(Color.GREEN);
                            }
                            if (statComparison[0] < 0.0) {
                                activity.getBinding().UserDistPerc.setTextColor(Color.RED);
                            }
                            activity.getBinding().UserTimePerc.setText((statComparison[1])+"%");
                            if (statComparison[1] > 0.0) {
                                activity.getBinding().UserTimePerc.setTextColor(Color.GREEN);
                            }
                            if (statComparison[1] < 0.0) {
                                activity.getBinding().UserTimePerc.setTextColor(Color.RED);
                            }
                            activity.getBinding().UserElePerc.setText((statComparison[2])+"%");
                            if (statComparison[2] > 0.0) {
                                activity.getBinding().UserElePerc.setTextColor(Color.GREEN);
                            }
                            if (statComparison[2] < 0.0) {
                                activity.getBinding().UserElePerc.setTextColor(Color.RED);
                            }
                            activity.getBinding().UserSpeedPerc.setText((statComparison[3])+"%");
                            if (statComparison[3] > 0.0) {
                                activity.getBinding().UserSpeedPerc.setTextColor(Color.GREEN);
                            }
                            if (statComparison[3] < 0.0) {
                                activity.getBinding().UserSpeedPerc.setTextColor(Color.RED);
                            }
                        }
                    }
            );

        }  catch (UnknownHostException | ConnectException unknownHost) {
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
                                    .setMessage("Could not fetch data from the server")
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
