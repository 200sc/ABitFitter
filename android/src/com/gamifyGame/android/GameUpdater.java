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
        if (LifeListener.getLifeListener().getStatus()){
            System.out.println("ALL LIFE LIFE LIFE");
            gameProcess.graphUpdate(String.valueOf(System.currentTimeMillis()),activity);
        }
        else{System.out.println("NO LIFE NO LIFE NO LIFE");}

        File toWrite = new File(this.getFilesDir(), "updateFile");
        try {
            FileOutputStream writer = new FileOutputStream(toWrite, true);
            writer.write(Byte.valueOf(String.valueOf(System.currentTimeMillis())+','+activity + "\n"));
            writer.close();
        }
        catch (Exception e){
            // This should never happen
            assert(1 == 0);
        }
    }
}
