package com.gamifyGame.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;



public abstract class FoodRequestTask<T> extends AsyncTask<JSONObject, Void, String> {
    private String serverAddress;
    private String endpoint;
    private Context context;

    public FoodRequestTask(String serverAddress, String endpoint, Context appContext){
        this.serverAddress = serverAddress;
        this.endpoint = endpoint;
        this.context = appContext;
    }

    @Override
    protected String doInBackground(JSONObject... jsonObjects) {
        HttpURLConnection connection = null;
        String res = "";
        HttpEntity entity = null;

        //sendNotification("requesting "+serverAddress+endpoint);


        //Make the web request to fetch new data
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(serverAddress+endpoint);
            //request.setHeader("Content-Type", "application/json");
            //request.setEntity(new StringEntity(jsonObjects[0].toString()));
            HttpResponse response = client.execute(request);
            entity = response.getEntity();
            res = EntityUtils.toString(response.getEntity());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (entity != null){
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                }
            }
            //sendNotification("ending async");
        }
        return res;
    }

    protected abstract String parseResponse (String response);

    private void sendNotification(String msg) {
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, AndroidLauncher.class);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Hello World")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());
    }
}