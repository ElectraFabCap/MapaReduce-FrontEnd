package com.example.distributedsystems2023.requests;

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
    private ActivityWalkStatsBinding binding;
    private String ip;

    private String path;

    public FileStatsRequest(ActivityWalkStatsBinding binding, String ip, String path){
        this.binding = binding;
        this.ip = ip;
        this.path = path;
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
            //GPXFile file = new GPXFile("/sdcard/Download/route1.gpx"); //MIGHT NOT BE CORRECT

            System.out.println("FILE CONTENT: " + file.getContentAsString());
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

//        } catch (UnknownHostException unknownHost) {
//            System.err.println("You are trying to connect to an unknown host!");
//        }catch(ClassNotFoundException e){
//            throw new RuntimeException(e);
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
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
