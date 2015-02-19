package com.gamifyGame.android;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.badlogic.gdx.Preferences;
import com.gamifyGame.gamifyGame;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * Created by Stephen on 2/14/2015.
 */
public class challengeAlarm extends WakefulBroadcastReceiver {

    gamifyGame game;

    public void onReceive(Context context, Intent intent) {
        //Determine if a new Challenge should be started this hour.
        ActionResolverAndroid actionResolverAndroid = ActionResolverAndroid.getActionResolverAndroid(context, false);
        gamifyGame game = gamifyGame.getGamifyGame(actionResolverAndroid);
        Preferences pref = game.getPrefs();

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

            boolean availableThisHour = pref.getBoolean(challengeTime(), false);
            float challengeChancesToday = getChallengeChances();

            // If available and this hour is randomly chosen from those available,
            if (availableThisHour && Math.random() < 1f / challengeChancesToday) {
                pref.putBoolean("waitingChallenge", true);
                pref.putBoolean("challengedToday", true);
                pref.flush();
        }
    }

    private String challengeTime(){
        int day = Calendar.DAY_OF_WEEK;
        int hour = Calendar.HOUR_OF_DAY;
        return String.valueOf(day) + ',' + String.valueOf(hour);
    }

    private float getChallengeChances(){
        float total = 1;
        String day = String.valueOf(Calendar.DAY_OF_WEEK);
        for (int i = Calendar.HOUR_OF_DAY+1; i < 24; i++) {
            if (game.getPrefs().getBoolean(day + ',' + String.valueOf(i),false)){
                total++;
            }
        }
        return total;
    }
}
