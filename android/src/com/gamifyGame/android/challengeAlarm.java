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

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by Stephen on 2/14/2015.
 */
public class challengeAlarm extends WakefulBroadcastReceiver {

    gamifyGame game;
    private final String[] CHALLENGE_PROMPTS = {"Try a new food!","Be active this hour!"};

    public void onReceive(Context context, Intent intent) {
        // This stuff happens every day at midnight!
        /*
        if (pref != null) {
            if (Calendar.HOUR_OF_DAY == 0) {
                pref.putBoolean("challengedToday", false);
                // Data to update!
                int challengeProgress = pref.getInteger("challengeProgress", 0);
                pref.putInteger("challengeProgress", 0);

                String todaysChallenge = pref.getString("todaysChallenge", "none");
                pref.putString("todaysChallenge", "none");

                int stepsTaken = pref.getInteger("stepsTaken", 0);
                pref.putInteger("stepsTaken", 0);

                int minutesSlept = pref.getInteger("minutesSlept", 0);
                pref.putInteger("minutesSlept", 0);

                int minutesWalked = pref.getInteger("minutesWalked", 0);
                pref.putInteger("minutesWalked", 0);

                int minutesRan = pref.getInteger("minutesRan", 0);
                pref.putInteger("minutesRan", 0);

                int minutesBiked = pref.getInteger("minutesBiked", 0);
                pref.putInteger("minutesBiked", 0);

                int minutesDanced = pref.getInteger("minutesDanced", 0);
                pref.putInteger("minutesDanced", 0);

                //TODO: Write more data!

                String today = String.valueOf(Calendar.DAY_OF_YEAR);

                // This math might suck, i.e. leap years
                int toDelete = 0;
                if (Calendar.DAY_OF_YEAR > 8) {
                    toDelete = Calendar.DAY_OF_YEAR - 8;
                } else {
                    toDelete = Calendar.DAY_OF_YEAR - 8 + 365;
                }
                String lastWeek = String.valueOf(toDelete);

                File toWrite = new File(context.getFilesDir(), today);
                try {
                    FileOutputStream writer = new FileOutputStream(toWrite);
                    writer.write(Byte.valueOf("challengeProgress," + challengeProgress + "\n"));
                    writer.write(Byte.valueOf("todaysChallenge," + todaysChallenge + "\n"));
                    writer.write(Byte.valueOf("stepsTaken," + stepsTaken + "\n"));
                    writer.write(Byte.valueOf("minutesSlept," + minutesSlept + "\n"));
                    writer.write(Byte.valueOf("minutesWalked," + minutesWalked + "\n"));
                    writer.write(Byte.valueOf("minutesRan," + minutesRan + "\n"));
                    writer.write(Byte.valueOf("minutesBiked," + minutesBiked + "\n"));
                    writer.write(Byte.valueOf("minutesDanced," + minutesDanced + "\n"));
                    writer.close();
                } catch (Exception e) {
                    // This should never happen
                    assert (1 == 0);
                }
                File deleteFile = new File(context.getFilesDir(), lastWeek);
                deleteFile.delete();

                pref.flush();
            }

            // If the user has been challenged today already, it is at least an hour later,
            // so assign waitingChallenge to be false.
            if (pref.getBoolean("challengedToday", false)) {
                pref.putBoolean("waitingChallenge", false);
                pref.flush();
                return;
            }
            }
            */
        if(LifeListener.getLifeListener().getStatus()) {
            ActionResolverAndroid actionResolverAndroid = ActionResolverAndroid.getActionResolverAndroid(context, false);
            game = gamifyGame.getGamifyGame(actionResolverAndroid);
            Preferences pref = game.getPrefs();

            // Daily reset
            if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 0) {
                //pref.putInteger("challengeProgress",0);
            }

            // Hourly reset
            pref.putInteger("minutesWalked",pref.getInteger("minutesWalked",0)
                            + pref.getInteger("minutesWalkedThisHour",0));
            pref.putInteger("minutesRan",pref.getInteger("minutesRan",0)
                            + pref.getInteger("minutesRanThisHour",0));
            pref.putInteger("minutesDanced",pref.getInteger("minutesDanced",0)
                            + pref.getInteger("minutesDancedThisHour",0));
            pref.putInteger("minutesBiked",pref.getInteger("minutesBiked",0)
                            + pref.getInteger("minutesBikedThisHour",0));

            pref.putInteger("minutesWalkedThisHour",0);
            pref.putInteger("minutesRanThisHour",0);
            pref.putInteger("minutesDancedThisHour",0);
            pref.putInteger("minutesBikedThisHour",0);
            pref.putInteger("newFoodThisHour", 0);


            boolean availableThisHour = pref.getBoolean(challengeTime(), false);
            float challengeChancesToday = getChallengeChances();
            sendChallengeNotification(context,String.valueOf(challengeTime()) + " " + String.valueOf(availableThisHour) + " " + String.valueOf(challengeChancesToday));
            if (availableThisHour && Math.random() <= 1f / challengeChancesToday) {
                pref.putBoolean("challengeHour", true);
                pref.putBoolean("challengedToday", true);
                String challengePrompt = generateChallenge();
                pref.putString("challengeVariety", challengePrompt);
                sendChallengeNotification(context, challengePrompt);
            }
            pref.flush();
        }
        else{
            SharedPreferences sPref = context.getApplicationContext().getSharedPreferences("bitFitpref", 0);

            boolean availableThisHour = sPref.getBoolean(challengeTime(), false);
            float challengeChancesToday = getSharedChallengeChances(sPref);
            if (availableThisHour && Math.random() < 1f / challengeChancesToday) {
                SharedPreferences.Editor editor = sPref.edit();
                editor.putBoolean("waitingChallenge", true);
                editor.putBoolean("challengeHour", true);
                editor.putBoolean("challengedToday", true);
                String challengePrompt = generateChallenge();
                editor.putString("challengeVariety", challengePrompt);
                sendChallengeNotification(context, challengePrompt);
                editor.apply();
            }
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

    private float getChallengeChances(){
        float total = 1;
        String day = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        for (int i = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1; i < 24; i++) {
            if (game.getPrefs().getBoolean(day + ',' + String.valueOf(i),false)){
                total++;
            }
        }
        return total;
    }

    private float getSharedChallengeChances(SharedPreferences pref){
        float total = 1;
        String day = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        for (int i = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1; i < 24; i++) {
            if (pref.getBoolean(day + ',' + String.valueOf(i), false)){
                total++;
            }
        }
        return total;
    }
}
