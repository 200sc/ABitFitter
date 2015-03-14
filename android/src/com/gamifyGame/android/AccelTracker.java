package com.gamifyGame.android;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.List;
import java.util.ArrayList;
import java.lang.Exception;

/**
 * Created by Stephen on 11/21/2014.
 */
public class AccelTracker extends IntentService implements SensorEventListener {

    String writeData;
    int linecount;
    int activity;

    public AccelTracker() {
        super("Tracker");
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        linecount = 0;
        writeData = "";
        String GAMIFY_VERSION = intent.getStringExtra("VERSION");
        String userID = intent.getStringExtra("userID");
        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //SystemClock.sleep(30000);
        SystemClock.sleep(10000);
        mSensorManager.unregisterListener(this);
        String completeData = writeData.substring(0);
        activity = Classify(completeData);

        String[] preCoords = writeData.split(System.getProperty("line.separator"));
        String[][] Coords = new String[preCoords.length][4];
        for(int i=0; i < preCoords.length; i++){
            Coords[i] = preCoords[i].split(",");
        }
        String[] actThing = new String [3];
        actThing[0] = Integer.toString(activity);
        actThing[1] = Coords[0][3];
        actThing[2] = GAMIFY_VERSION;

        Intent newIntent = new Intent(this, AccelSender.class);
        newIntent.putExtra("writeData", writeData);
        newIntent.putExtra("activity", actThing);
        newIntent.putExtra("userID", userID);
        //ComponentName c = this.startService(newIntent);
        Intent updateIntent = new Intent(this, GameUpdater.class);
        updateIntent.putExtra("curActivity",intToStringActivity(activity));
        ComponentName d = this.startService(updateIntent);
        //if (Math.random() < .025) {
        //    sendNotification("What activity have you been doing recently?");
        //}
    }

    protected int Classify(String completeData){
        //find peaks
        //find time between peaks
        //find Average Resultant Acceleration
        //find average accelerations

        float xSum = 0;
        float ySum = 0;
        float zSum = 0;

        float xSumAbs = 0;
        float ySumAbs = 0;
        float zSumAbs = 0;

        float xTemp;
        float yTemp;
        float zTemp;

        float[] xPeaks;
        float[] yPeaks;
        float[] zPeaks;

        float xRandPeak;
        float yRandPeak;
        float zRandPeak;

        float xAvgPeakTimeDiff;
        float yAvgPeakTimeDiff;
        float zAvgPeakTimeDiff;

        float xAverage = 0;
        float yAverage = 0;
        float zAverage = 0;

        long xPeakAvg = 0;
        long yPeakAvg = 0;
        long zPeakAvg = 0;

        double resultantAccel = 0;
        double magnitude = 0;


        String line;

        ArrayList<ArrayList<String>> masLines = new ArrayList<ArrayList<String>>();

        String[] lines = completeData.split(System.getProperty("line.separator"));

        for(int i = 0; i < lines.length; i++){
            ArrayList<String>coords = new ArrayList<String>();
            line = new String(lines[i]);
            for (String retval: line.split(",")){
                coords.add(retval);
            }
            masLines.add(coords);


            xTemp = Float.parseFloat(coords.get(0));
            yTemp = Float.parseFloat(coords.get(1));
            zTemp = Float.parseFloat(coords.get(2));

            xSum += xTemp;
            ySum += yTemp;
            zSum += zTemp;
        }

        for(int i = 0; i < lines.length; i++){
            ArrayList<String>coords = new ArrayList<String>();
            line = new String(lines[i]);
            for (String retval: line.split(",")){
                coords.add(retval);
            }
            masLines.add(coords);


            xTemp = Float.parseFloat(coords.get(0));
            yTemp = Float.parseFloat(coords.get(1));
            zTemp = Float.parseFloat(coords.get(2));

            xSumAbs += Math.abs(xTemp);
            ySumAbs += Math.abs(yTemp);
            zSumAbs += Math.abs(zTemp);
        }


        xAverage = Math.abs(xSum/lines.length);
        yAverage = Math.abs(ySum/lines.length);
        zAverage = Math.abs(zSum/lines.length);

        xSumAbs = xSumAbs - (xAverage * lines.length);
        ySumAbs = ySumAbs - (yAverage * lines.length);
        zSumAbs = zSumAbs - (zAverage * lines.length);

        float yMag = ySumAbs * ySumAbs;



        resultantAccel = Math.sqrt((xSum * xSum) + (ySum * ySum) + (zSum * zSum));
        magnitude      = Math.sqrt((xSumAbs * xSumAbs) + (ySumAbs * ySumAbs) + (zSumAbs * zSumAbs));


        ArrayList<List<Long>> peakLists;

        peakLists = findPeaks(masLines);

        List<Long> xPeakList = new ArrayList<Long>();
        List<Long> yPeakList = new ArrayList<Long>();
        List<Long> zPeakList = new ArrayList<Long>();
        List<Long> avgPeakList = new ArrayList<Long>();

        xPeakList = peakLists.get(0);
        yPeakList = peakLists.get(1);
        zPeakList = peakLists.get(2);
        avgPeakList = peakLists.get(3);

        xAvgPeakTimeDiff = getAvgPeakDiff(xPeakList);
        yAvgPeakTimeDiff = getAvgPeakDiff(yPeakList);
        zAvgPeakTimeDiff = getAvgPeakDiff(zPeakList);

        xPeakAvg = avgPeakList.get(0);
        yPeakAvg = avgPeakList.get(1);
        zPeakAvg = avgPeakList.get(2);

        File toRead = new File(getApplicationContext().getFilesDir(), "sleepFile");
        //sendNotification("Fail");
        //sendNotification("ACCELTRACK:sleepFile" + toRead.getName());
        try{
            BufferedReader reader = new BufferedReader(new FileReader(toRead));
            boolean isSleeping = false;
            //sendNotification("not read");
            while ((line = reader.readLine()) != null) {
                //sendNotification(line);
                System.out.print(line);
                if(Integer.parseInt(line)==1){
                    isSleeping = true;
                }
                System.out.println(" END");
                //sendNotification("all read");
            }
            //sendNotification("Correctly read stuff");
            if(isSleeping){
                return sleepAnalysis(yMag, magnitude, xPeakAvg, yPeakAvg, zPeakAvg, resultantAccel, xAverage, yAverage, zAverage, xAvgPeakTimeDiff, yAvgPeakTimeDiff, zAvgPeakTimeDiff);
            }
            reader.close();
        }catch(Exception e){
            //Nothing

        }



        return activityAnalysis(yMag, magnitude, xPeakAvg, yPeakAvg, zPeakAvg, resultantAccel, xAverage, yAverage, zAverage, xAvgPeakTimeDiff, yAvgPeakTimeDiff, zAvgPeakTimeDiff);
    }


    protected ArrayList<List<Long>> findPeaks(ArrayList<ArrayList<String>> myList) {
        int xCounter = 0;
        int yCounter = 0;
        int zCounter = 0;

        float xPeakSum = 0;
        float yPeakSum = 0;
        float zPeakSum = 0;

        List<Long> peakAvgs = new ArrayList<Long>();

        List<String> currentPeak;
        List<String> tempNode;
        List<String> prevNeigh;
        List<String> nextNeigh;

        List<Long> xPeaks = new ArrayList<Long>();
        List<Long> yPeaks = new ArrayList<Long>();
        List<Long> zPeaks = new ArrayList<Long>();

        ArrayList<List<Long>> peakLists = new ArrayList<List<Long>>();


        float xTempCurrent = 0;
        float yTempCurrent = 0;
        float zTempCurrent = 0;

        float xTempPreNeigh = 0;
        float yTempPreNeigh = 0;
        float zTempPreNeigh = 0;

        float xTempNextNeigh = 0;
        float yTempNextNeigh = 0;
        float zTempNextNeigh = 0;


        for (int i = 1; i < myList.size() - 1; i++) {
            tempNode = myList.get(i);
            prevNeigh = myList.get(i - 1);
            nextNeigh = myList.get(i + 1);

            xTempCurrent = Float.parseFloat(tempNode.get(0));
            yTempCurrent = Float.parseFloat(tempNode.get(1));
            zTempCurrent = Float.parseFloat(tempNode.get(2));

            xTempPreNeigh = Float.parseFloat(prevNeigh.get(0));
            yTempPreNeigh = Float.parseFloat(prevNeigh.get(1));
            zTempPreNeigh = Float.parseFloat(prevNeigh.get(2));

            xTempNextNeigh = Float.parseFloat(nextNeigh.get(0));
            yTempNextNeigh = Float.parseFloat(nextNeigh.get(1));
            zTempNextNeigh = Float.parseFloat(nextNeigh.get(2));


            //check node if peak for x
            //System.out.println(xTempCurrent + xTempPreNeigh + xTempNextNeigh);
            if ((xTempCurrent > xTempPreNeigh) && (xTempCurrent > xTempNextNeigh)) {
                currentPeak = tempNode;
                xPeaks.add(Long.parseLong(currentPeak.get(3)));
                xPeakSum = xPeakSum + Float.parseFloat(currentPeak.get(0));
                //System.out.println("adding x peak");
                i++;
            } else {
                i++;
            }

            if ((yTempCurrent > yTempPreNeigh) && (yTempCurrent > yTempNextNeigh)) {
                currentPeak = tempNode;
                yPeaks.add(Long.parseLong(currentPeak.get(3)));
                yPeakSum = yPeakSum + Float.parseFloat(currentPeak.get(1));
                //System.out.println("adding y peak");
                i++;
            } else {
                i++;
            }

            if ((zTempCurrent > zTempPreNeigh) && (zTempCurrent > zTempNextNeigh)) {
                currentPeak = tempNode;
                zPeaks.add(Long.parseLong(currentPeak.get(3)));
                zPeakSum = zPeakSum + Float.parseFloat(currentPeak.get(2));
                //System.out.println("adding z peak");
                i++;
            } else {
                i++;
            }
        }

            peakAvgs.add((long)(xPeakSum/xPeaks.size()));
            peakAvgs.add((long)(yPeakSum/yPeaks.size()));
            peakAvgs.add((long)(zPeakSum/zPeaks.size()));

            peakLists.add(xPeaks);
            peakLists.add(yPeaks);
            peakLists.add(zPeaks);
            peakLists.add(peakAvgs);

            return peakLists;
        }

    protected float getAvgPeakDiff(List<Long>list){
        float sum = 0;
        int listSize = list.size();

        for (int i = 0; i < list.size() - 1; i++){
            sum += list.get(i + 1) - list.get(i);
        }

        return sum/listSize;
    }

    protected int sleepAnalysis(float yMag, double mag, long xAvgPeak, long yAvgPeak, long zAvgPeak, double rawr, float xAvg, float yAvg, float zAvg, float xAvgPTD, float yAvgPTD, float zAvgPTD){
        sendNotification("Sleep is enabled");

        //Check if actually asleep, currently checks that you arent walking
        if((xAvg < 3 && xAvg > 0) && (yAvg < 1 && yAvg > 0) && (zAvg > 9 && zAvg < 10)){
            Log.d("Salubrity","FalseSleep: peaks: " + Float.toString(xAvgPeak) + " " + Float.toString(yAvgPeak) + " " + Float.toString(zAvgPeak));
            Log.d("Salubrity","averages: " + Float.toString(xAvg) + " " + Float.toString(yAvg) + " " + Float.toString(zAvg));
            Log.d("Salubrity","PTD: " + Float.toString(xAvgPTD) + " " + Float.toString(yAvgPTD) + " " + Float.toString(zAvgPTD));
            Log.d("Salubrity","rawr: " + Double.toString(rawr));
            return 8;
        }

        //is sleeeeping
        Log.d("Salubrity","TrueSleep: peaks: " + Float.toString(xAvgPeak) + " " + Float.toString(yAvgPeak) + " " + Float.toString(zAvgPeak));
        Log.d("Salubrity","averages: " + Float.toString(xAvg) + " " + Float.toString(yAvg) + " " + Float.toString(zAvg));
        Log.d("Salubrity","PTD: " + Float.toString(xAvgPTD) + " " + Float.toString(yAvgPTD) + " " + Float.toString(zAvgPTD));
        Log.d("Salubrity","rawr: " + Double.toString(rawr));
        //Is sleeping!
        return 0;
    }

    protected int activityAnalysis(float yMag, double mag, long xAvgPeak, long yAvgPeak, long zAvgPeak, double rawr, float xAvg, float yAvg, float zAvg, float xAvgPTD, float yAvgPTD, float zAvgPTD){
        //sendNotification(xAvgPeak + " " + yAvgPeak + " " + zAvgPeak);
        if(((xAvgPeak < 10 && xAvgPeak > -5)) && ((yAvgPeak > -10 && yAvgPeak < 20)) && ((zAvgPeak < 15 && zAvgPeak > -10))){
            if ((yAvgPTD > 0 && yAvgPTD < 400)){
                //sendNotification(Double.toString(Math.round(rawr)) + " " + Float.toString(xAvgPTD) + " " + Float.toString(yAvgPTD) + " " + Float.toString(zAvgPTD));
                Log.d("Salubrity","SITTING: peaks: " + Float.toString(xAvgPeak) + " " + Float.toString(yAvgPeak) + " " + Float.toString(zAvgPeak));
                Log.d("Salubrity","averages: " + Float.toString(xAvg) + " " + Float.toString(yAvg) + " " + Float.toString(zAvg));
                Log.d("Salubrity","PTD: " + Float.toString(xAvgPTD) + " " + Float.toString(yAvgPTD) + " " + Float.toString(zAvgPTD));
                Log.d("Salubrity","rawr: " + Double.toString(rawr));
                return 6;
            }
        }

        if(((xAvgPeak < 5 && xAvgPeak > -2.5)) && ((yAvgPeak > -5 && yAvgPeak < 10)) && ((zAvgPeak < 7 && zAvgPeak > -5))){
            if ((yAvgPTD > 0 && yAvgPTD < 200)){
                //sendNotification(Double.toString(Math.round(rawr)) + " " + Float.toString(xAvgPTD) + " " + Float.toString(yAvgPTD) + " " + Float.toString(zAvgPTD));
                Log.d("Salubrity","SITTING: peaks: " + Float.toString(xAvgPeak) + " " + Float.toString(yAvgPeak) + " " + Float.toString(zAvgPeak));
                Log.d("Salubrity","averages: " + Float.toString(xAvg) + " " + Float.toString(yAvg) + " " + Float.toString(zAvg));
                Log.d("Salubrity","PTD: " + Float.toString(xAvgPTD) + " " + Float.toString(yAvgPTD) + " " + Float.toString(zAvgPTD));
                Log.d("Salubrity","rawr: " + Double.toString(rawr));
                return 3;
            }
        }

        if((xAvg < 10 && xAvg > -10) && (yAvg < 20 && yAvg > -10) && (zAvg > -5 && zAvg < 10 )){
            if ((yAvgPTD > 300)){
                //sendNotification(Double.toString(Math.round(rawr)) + " " + Float.toString(xAvgPTD) + " " + Float.toString(yAvgPTD) + " " + Float.toString(zAvgPTD));
                Log.d("Salubrity","WALKING: peaks: " + Float.toString(xAvgPeak) + " " + Float.toString(yAvgPeak) + " " + Float.toString(zAvgPeak));
                Log.d("Salubrity","averages: " + Float.toString(xAvg) + " " + Float.toString(yAvg) + " " + Float.toString(zAvg));
                Log.d("Salubrity","PTD: " + Float.toString(xAvgPTD) + " " + Float.toString(yAvgPTD) + " " + Float.toString(zAvgPTD));
                Log.d("Salubrity","rawr: " + Double.toString(rawr));
                return 5;
            }
        }

        if((xAvg < 5 && xAvg > 0) && (yAvg < 10 && yAvg > 5) && (zAvg > 0 && zAvg < 5)){
            if (rawr < 5000 && rawr > 1000) {
                //sendNotification("Walking" + " " + xAvg + " " + yAvg + " " + zAvg + " " + yMag + " " + mag);
                //sendNotification("Walking" + Double.toString(Math.round(rawr)) + " " + Float.toString(xAvgPTD) + " " + Float.toString(yAvgPTD) + " " + Float.toString(zAvgPTD));
                Log.d("Salubrity", "WALKING: peaks: " + Float.toString(xAvgPeak) + " " + Float.toString(yAvgPeak) + " " + Float.toString(zAvgPeak));
                Log.d("Salubrity", "averages: " + Float.toString(xAvg) + " " + Float.toString(yAvg) + " " + Float.toString(zAvg));
                Log.d("Salubrity", "PTD: " + Float.toString(xAvgPTD) + " " + Float.toString(yAvgPTD) + " " + Float.toString(zAvgPTD));
                Log.d("Salubrity", "rawr: " + Double.toString(rawr));
                return 5;
            }
        }

        //sendNotification(Double.toString(Math.round(rawr)) + " " + Float.toString(xAvgPTD) + " " + Float.toString(yAvgPTD) + " " + Float.toString(zAvgPTD));
        if((xAvg < 3 && xAvg > 0) && (yAvg < 1 && yAvg > 0) && (zAvg > 9 && zAvg < 10)){
            //if ((yAvgPTD >= 0 && yAvgPTD <= 500) || (yAvgPTD == Double.NaN)) {
            //sendNotification("On Table" + " " + xAvg + " " + yAvg + " " + zAvg + " " + yMag + " " + mag);
            //sendNotification("Walking" + Double.toString(Math.round(rawr)) + " " + Float.toString(xAvgPTD) + " " + Float.toString(yAvgPTD) + " " + Float.toString(zAvgPTD));
            Log.d("Salubrity", "WALKING: peaks: " + Float.toString(xAvgPeak) + " " + Float.toString(yAvgPeak) + " " + Float.toString(zAvgPeak));
            Log.d("Salubrity", "averages: " + Float.toString(xAvg) + " " + Float.toString(yAvg) + " " + Float.toString(zAvg));
            Log.d("Salubrity", "PTD: " + Float.toString(xAvgPTD) + " " + Float.toString(yAvgPTD) + " " + Float.toString(zAvgPTD));
            Log.d("Salubrity", "rawr: " + Double.toString(rawr));
            return 0;
            //}
        }

        //sendNotification(xAvg + " " + yAvg + " " + zAvg + " " + yAvgPTD);
        //sendNotification(Double.toString(Math.round(rawr)) + " " + Float.toString(xAvgPTD) + " " + Float.toString(yAvgPTD) + " " + Float.toString(zAvgPTD));
        //sendNotification("Inactive" + " " + xAvg + " " + yAvg + " " + zAvg + " " + yMag + " " + mag);
        Log.d("Salubrity","INACTIVE: peaks: " + Float.toString(xAvgPeak) + " " + Float.toString(yAvgPeak) + " " + Float.toString(zAvgPeak));
        Log.d("Salubrity","averages: " + Float.toString(xAvg) + " " + Float.toString(yAvg) + " " + Float.toString(zAvg));
        Log.d("Salubrity","PTD: " + Float.toString(xAvgPTD) + " " + Float.toString(yAvgPTD) + " " + Float.toString(zAvgPTD));
        Log.d("Salubrity","rawr: " + Double.toString(rawr));
        return 0;

        }

    private void sendNotification(String msg) {
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AndroidLauncher.class);
        String curActivity = "inactive";
        curActivity = intToStringActivity(activity);
        intent.putExtra("curActivity", curActivity);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Hello Tester!")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            //sendNotification("No directory!");
        }
        return file;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        writeData = writeData + String.valueOf(event.values[0]) + ',' + String.valueOf(event.values[1]) + ',' +
                String.valueOf(event.values[2]) + ',' + String.valueOf(System.currentTimeMillis()) + "\n";
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private String intToStringActivity(int activity){ // TODO: Replace this with enum
        switch (activity){
            case 0: return "inactive";
            case 1: return "active";
            case 2: return "running";
            case 3: return "cycling";
            case 4: return "dancing";
            case 5: return "walking";
            case 6: return "sitting";
            case 7: return "standing";
            case 8: return "sleeping";
            default: return "nothing";
        }
    }
}
