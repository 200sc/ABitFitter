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

import java.io.File;

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
        SystemClock.sleep(30000);
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
        ComponentName c = this.startService(newIntent);
        Intent updateIntent = new Intent(this, GameUpdater.class);
        updateIntent.putExtra("curActivity",intToStringActivity(activity));
        ComponentName d = this.startService(updateIntent);
        if (Math.random() < .025) {
            sendNotification("What activity have you been doing recently?");
        }
    }

    protected int Classify(String completeData){
        float xthreshold = (float)0.0;
        float ythreshold = (float)0.0;
        float zthreshold = (float)0.0;
        int xthresholdTotal = 0;
        int ythresholdTotal = 0;
        int zthresholdTotal = 0;
        int xbypass;
        int ybypass;
        int zbypass;
        int timer = 3000;
        long timestamp = 0;
        int inactiveTime = 0;
        String[] coords;
        String line;
        String[] lines = completeData.split(System.getProperty("line.separator"));
        for(int i = 0; i < lines.length; i++) {
            line = lines[i];
            coords = line.split(",");
            if (timestamp != 0){
                timer -= Long.valueOf(coords[3]) - timestamp;
            }
            timestamp = Long.valueOf(coords[3]);
            if (timer < 0){
                inactiveTime -= timer;
                timer = 0;
            }
            xthreshold = thresholdCheck(xthreshold,Float.valueOf(coords[0]));
            if (xthreshold < -4000) {
                xthreshold += 5000;
                xbypass = 1;
            }
            else xbypass = 0;
            xthresholdTotal += xthreshold;
            ythreshold = thresholdCheck(ythreshold,Float.valueOf(coords[1]));
            if (ythreshold < -4000) {
                ythreshold += 5000;
                ybypass = 1;
            }
            else ybypass = 0;
            ythresholdTotal += ythreshold;
            zthreshold = thresholdCheck(zthreshold,Float.valueOf(coords[2]));
            if (zthreshold < -4000) {
                zthreshold += 5000;
                zbypass = 1;
            }
            else zbypass = 0;
            zthresholdTotal += zthreshold;
            if (0 == xbypass || 0 == ybypass || 0 == zbypass){
                timer = 3000;
            }
        }
        float xaverage = xthresholdTotal / lines.length;
        float yaverage = ythresholdTotal / lines.length;
        float zaverage = zthresholdTotal / lines.length;
        return activityAnalysis(xaverage,yaverage,zaverage,inactiveTime);
    }

    protected int activityAnalysis(float x, float y, float z, int t){
        if ((x < 0) && (y < -10) && (z < -5) && t < 5000){
            // "active", or walking
            //sendNotification("x="+String.valueOf((int)x)+" y="+String.valueOf((int)y)+" z="+String.valueOf((int)z)+" t="+String.valueOf(t)+" 1");
            return 1;
        }
        //sendNotification("x="+String.valueOf((int)x)+" y="+String.valueOf((int)y)+" z="+String.valueOf((int)z)+" t="+String.valueOf(t)+" 0");
        // 0 currently means in-active
        return 0;
    }

    protected float thresholdCheck(float threshold,float curValue){
        float delta = (float)2.0;
        if (Math.abs(curValue - threshold) < delta){
            return curValue;
        }
        else if (threshold > curValue){
            return curValue - 5000;
        }
        return threshold - 5000;
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
            default: return "nothing";
        }
    }
}
