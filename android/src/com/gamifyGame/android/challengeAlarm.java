package com.gamifyGame.android;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.badlogic.gdx.Preferences;
import com.gamifyGame.gamifyGame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Stephen on 2/14/2015.
 */
public class challengeAlarm extends WakefulBroadcastReceiver {

    gamifyGame game;
    private HashMap<String,String> input;
    private HashMap<String,String> daily;
    private final String[] CHALLENGE_PROMPTS = {"Try a new food!","Be active this hour!"};

    public void onReceive(Context context, Intent intent) {

        //**********************************************\\
        //
        // Initialize maps and files
        input = new HashMap<String,String>();
        daily = new HashMap<String,String>();
        System.out.println(String.valueOf(context));
        File toRead = new File(context.getApplicationContext().getFilesDir(), "inChallenge");
        File toWrite = new File(context.getApplicationContext().getFilesDir(), "outChallenge");
        File dailyFile = new File(context.getApplicationContext().getFilesDir(), "dailyData");


        try {

            //**********************************************\\
            //
            // Read the contents of the input files
            BufferedReader reader = new BufferedReader(new FileReader((toRead)));
            String line = null;
            String[] lineParts;
            while ((line = reader.readLine()) != null) {
                lineParts = line.split(",");
                input.put(lineParts[0], lineParts[1]);
                System.out.println("\"ChallengeAlarm: INPUT KEYS and VALS: " + lineParts[0] + " , " + lineParts[1]);
            }
            reader.close();

            reader = new BufferedReader(new FileReader((dailyFile)));
            line = null;
            while ((line = reader.readLine()) != null) {
                lineParts = line.split(",");
                daily.put(lineParts[0], lineParts[1]);
                System.out.println("\"ChallengeAlarm: DAILY KEYS and VALS: " + lineParts[0] + " , " + lineParts[1]);
            }
            reader.close();

            //**********************************************\\

            FileOutputStream hourWriter = new FileOutputStream(toWrite);
            FileOutputStream dailyWriter = new FileOutputStream(dailyFile);
            if (hourWriter == null || dailyWriter == null) {
                System.out.println("ChallengeAlarm: Writer is null");
            }


            //**********************************************\\
            //
            // Write to output for the day
            dailyWriter.write(("minutesWalked,"+String.valueOf(
                    getInteger(daily,"minutesWalked")+
                    getInteger(input,"minutesWalkedThisHour"))).getBytes());
            dailyWriter.write(("minutesRan,"+String.valueOf(
                    getInteger(daily,"minutesRan")+
                    getInteger(input,"minutesRanThisHour"))).getBytes());
            dailyWriter.write(("minutesDanced,"+String.valueOf(
                    getInteger(daily,"minutesDanced")+
                    getInteger(input,"minutesDancedThisHour"))).getBytes());
            dailyWriter.write(("minutesBiked,"+String.valueOf(
                    getInteger(daily,"minutesBiked"))+
                    getInteger(input,"minutesBikedThisHour")).getBytes());
            dailyWriter.write(("stepsTaken,"+String.valueOf(
                    getInteger(daily,"stepsTaken"))+
                    getInteger(input,"stepsTakenThisHour")).getBytes());
            dailyWriter.write(("newFood,"+String.valueOf(
                    getInteger(daily,"newFood"))+
                    getInteger(input,"newFoodThisHour")).getBytes());


            // And the output for GDX
            hourWriter.write(("minutesWalkedThisHour,0\n").getBytes());
            hourWriter.write(("minutesRanThisHour,0\n").getBytes());
            hourWriter.write(("minutesDancedThisHour,0\n").getBytes());
            hourWriter.write(("minutesBikedThisHour,0\n").getBytes());
            hourWriter.write(("stepsTakenThisHour,0\n").getBytes());
            hourWriter.write(("newFoodThisHour,0\n").getBytes());



            //**********************************************\\
            //
            // Store a bunch of things once a day
            if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 1){
                String today = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
                File weeklyFile = new File(context.getApplicationContext().getFilesDir(), "day"+today);

                int toDelete = 0;
                if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) > 8) {
                    toDelete = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 8;
                } else {
                    toDelete = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 8 + 365;
                }
                String lastWeek = String.valueOf(toDelete);
                File deleteFile = new File(context.getFilesDir(), "day"+lastWeek);

                FileOutputStream weekWriter = new FileOutputStream(weeklyFile, true);
                hourWriter.write(Byte.valueOf("challengeProgress," + getInteger(daily, "challengeProgress") + "\n"));
                hourWriter.write(Byte.valueOf("todaysChallenge," + getString(daily, "todaysChallenge") + "\n"));
                hourWriter.write(Byte.valueOf("stepsTaken," + getInteger(daily, "stepsTaken") + "\n"));
                hourWriter.write(Byte.valueOf("minutesSlept," + getInteger(daily, "minutesSlept") + "\n"));
                hourWriter.write(Byte.valueOf("minutesWalked," + getInteger(daily, "minutesWalked") + "\n"));
                hourWriter.write(Byte.valueOf("minutesRan," + getInteger(daily, "minutesRan") + "\n"));
                hourWriter.write(Byte.valueOf("minutesBiked," + getInteger(daily, "minutesBiked") + "\n"));
                hourWriter.write(Byte.valueOf("minutesDanced," + getInteger(daily, "minutesDanced") + "\n"));

                weekWriter.close();

            }


            //**********************************************\\
            //
            // If challenged this hour, write the challenge data to output.
            boolean availableThisHour = Boolean.valueOf(input.get(challengeTime()));
            float challengeChancesToday = getChallengeChances(input);

            if (availableThisHour && Math.random() < 1f / challengeChancesToday && !getBoolean(daily,"challengedToday")) {
                hourWriter.write(("challengeHour,true").getBytes());
                hourWriter.write(("challengedToday,true").getBytes());

                String challengePrompt = generateChallenge();
                hourWriter.write(("challengeVariety," + challengePrompt).getBytes());


                sendChallengeNotification(context, challengePrompt);
            }

            hourWriter.close();
            dailyWriter.close();
        } catch (Exception e) {
            // This should never happen
            System.out.println("ChallengeAlarm: crash");
            System.out.println("ChallengeAlarm: " + e.getMessage());
            assert (1 == 0);
        }
    }

    private String generateChallenge(){
        float[] weights = new float[2];
        weights[0] = .5f;
        weights[1] = .5f;
        double rand = new Random(5).nextDouble();
        double acc = 0;
        int i = -1;
        while(acc < rand){
            i++;
            acc += weights[i];
        }
        return CHALLENGE_PROMPTS[i];
    }

    private void sendChallengeNotification(Context context, String msg) {
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, AndroidLauncher.class);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Challenge in Progress!")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());
    }

    private String challengeTime(){
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return String.valueOf(day-1) + ',' + String.valueOf(hour);
    }


    private float getChallengeChances(HashMap<String,String> input){
        float total = 1;
        String day = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        for (int i = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1; i < 24; i++) {
            if (getBoolean(input, day + ',' + String.valueOf(i))){
                total++;
            }
        }
        return total;
    }

    private Boolean getBoolean(HashMap<String,String> map, String key){
        if (input.containsKey(key)) return Boolean.valueOf(input.get(key));
        return false;
    }

    private Integer getInteger(HashMap<String,String> map, String key){
        if (input.containsKey(key)) return Integer.valueOf(input.get(key));
        return 0;
    }

    private String getString(HashMap<String,String> map, String key){
        if (input.containsKey(key)) return input.get(key);
        return "none";
    }

}
