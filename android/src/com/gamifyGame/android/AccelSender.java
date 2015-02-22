package com.gamifyGame.android;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

/**
 * Created by Stephen on 2/5/2015.
 */
public class AccelSender extends IntentService {

    int activity;
    String ID;

    public AccelSender() {
        super("Tracker");
        setIntentRedelivery(true);
    }

    protected void connectTry(String[][] coord, String[] actId){

        for(int i=0; i< coord.length; i++) {
            JSONObject toSend = new JSONObject();
            try {
                String tmpStr = coord[i][0]+","+coord[i][1]+","+coord[i][2]+","+coord[i][3];

                toSend.put("userID", ID);
                toSend.put("xyz", tmpStr);

            } catch (JSONException e) {
                e.printStackTrace();
                sendNotification(e.getMessage());
            }
            doJSONReq(toSend);
        }
        try {
            JSONObject toSend = new JSONObject();
            toSend.put("userID", ID);
            toSend.put("activity", actId[0]+","+actId[1]+","+actId[2]);
            doJSONACT(toSend);
        }catch(JSONException e){
            e.printStackTrace();
            sendNotification(e.getMessage());
        }
    }

    protected void doJSONReq(JSONObject jsonObject1){
        HttpURLConnection connection = null;
        String output = "";

        //Make web request to fetch new data
        try{
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("http://104.131.171.125:3000/api/storeXYZ");
            request.setHeader("Content-Type", "application/json");

            request.setEntity(new StringEntity(jsonObject1.toString()));
            HttpResponse response = client.execute(request);

        } catch (MalformedURLException e){
            e.printStackTrace();
            sendNotification(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            sendNotification(e.getMessage());
        }
        finally {
            if (connection != null){
                connection.disconnect();
                sendNotification("Connection not available");
            }
        }
    }

    protected void doJSONACT(JSONObject jsonObject1){
        HttpURLConnection connection = null;
        String output = "";

        //Make web request to fetch new data
        try{
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("http://104.131.171.125:3000/api/storeACT");
            request.setHeader("Content-Type", "application/json");

            request.setEntity(new StringEntity(jsonObject1.toString()));

            HttpResponse response = client.execute(request);



        } catch (Exception e){
            e.printStackTrace();
            sendNotification(e.getMessage());
        }
        finally {
            if (connection != null){
                sendNotification("Connection not found");
                connection.disconnect();
            }
        }
    }

    protected void onHandleIntent(Intent intent) {
        String writeData = intent.getStringExtra("writeData");
        ID = intent.getStringExtra("userID");
        String[] preCoords = writeData.split(System.getProperty("line.separator"));
        String[][] Coords = new String[preCoords.length][4];
        for(int i=0; i < preCoords.length; i++){
            Coords[i] = preCoords[i].split(",");
        }
        String[] actThing = intent.getStringArrayExtra("activity");
        connectTry(Coords, actThing);
        //System.gc();

    }

    private void sendNotification(String msg) {
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AndroidLauncher.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String curActivity = "inactive";
        switch (activity){
            case 0: curActivity = "inactive";
                break;
            case 1: curActivity = "active";
                break;
            case 2: curActivity = "running";
                break;
            case 3: curActivity = "cycling";
                break;
            case 4: curActivity = "dancing";
                break;
            case 5: curActivity = "walking";
                break;
            case 6: curActivity = "sitting";
                break;
            case 7: curActivity = "standing";
                break;
        }
        intent.putExtra("curActivity", curActivity);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Hello World")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
