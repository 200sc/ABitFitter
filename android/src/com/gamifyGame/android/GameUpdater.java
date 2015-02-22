package com.gamifyGame.android;

import android.app.IntentService;
import android.content.Intent;

import com.badlogic.gdx.Preferences;
import com.gamifyGame.gamifyGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * Created by Stephen on 2/12/2015.
 */
public class GameUpdater extends IntentService {

    public GameUpdater(){
        super("Tracker");
        setIntentRedelivery(true);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        ActionResolverAndroid actionResolverAndroid = ActionResolverAndroid.getActionResolverAndroid(this, false);
        gamifyGame gameProcess = gamifyGame.getGamifyGame(actionResolverAndroid);
        String activity = intent.getStringExtra("curActivity");

        File toWrite = new File(this.getApplicationContext().getFilesDir(), "updateFile");
        try {
            FileOutputStream writer = new FileOutputStream(toWrite, true);
            if (writer == null) {
                System.out.println("GameUpdater: Writer is null");
            }

            //System.out.println("GameUpdater: act is " + activity);

            writer.write((String.valueOf(System.currentTimeMillis()) + ',' + activity + "\n").getBytes());
            writer.close();
        } catch (Exception e) {
            // This should never happen
            System.out.println("GameUpdater: crash");
            System.out.println("GameUpdater: " + e.getMessage());
            assert (1 == 0);
        }
    }
}
