package com.gamifyGame.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IntegerRes;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import com.gamifyGame.ActionResolver;
import com.gamifyGame.gamifyGame;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.security.Key;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class AndroidLauncher extends AndroidApplication {

    private final String GAMIFY_VERSION = "0.1.1a";
    Preferences pref;

	@Override
	protected void onCreate (Bundle savedInstanceState)
    {

		super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        ActionResolverAndroid actionResolverAndroid = ActionResolverAndroid.getActionResolverAndroid(this, true);
        gamifyGame gameProcess = gamifyGame.getGamifyGame(actionResolverAndroid);

        Bundle extras = this.getIntent().getExtras();
        try{String userID = (String) extras.get("ID");}
        catch(Exception e){String userID = "4321";}

        // Make a fake ID, Replace when userID is implemented
        pref = this.getPreferences("Bitfitpref");
        Preferences updatePref = this.getPreferences("Update");
        Preferences graphPref = this.getPreferences("Graphpref");
        double ID = Math.random()*(Math.pow(10d,15d))%Math.pow(10d,15d)+Math.pow(10d,16d);
        String fakeID = pref.getString("userID",String.valueOf(ID));



        // Set screen size of this device in pixels
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float width = size.x;
        float height = size.y;
        System.out.println("AndroidLauncher: Screen Width: "+ width + " Height: " + height );
        pref.putFloat("screenWidth",  width);
        pref.putFloat("screenHeight", height);


        pref.putString("userID", fakeID);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("bitPref", 0);
        if(sharedPref.getBoolean("challengeAlarmTriggered",false)){
            pref.putInteger("minutesWalked",sharedPref.getInt("minutesWalked",0));
            pref.putInteger("minutesRan",sharedPref.getInt("minutesRan",0));
            pref.putInteger("minutesDanced",sharedPref.getInt("minutesDanced",0));
            pref.putInteger("minutesBiked",sharedPref.getInt("minutesBiked",0));

            pref.putInteger("minutesWalkedThisHour",sharedPref.getInt("minutesWalkedThisHour",0));
            pref.putInteger("minutesRanThisHour",sharedPref.getInt("minutesRanThisHour",0));
            pref.putInteger("minutesDancedThisHour",sharedPref.getInt("minutesDancedThisHour",0));
            pref.putInteger("minutesBikedThisHour",sharedPref.getInt("minutesBikedThisHour",0));
            pref.putInteger("newFoodThisHour", sharedPref.getInt("newFoodThisHour",0));
            pref.putBoolean("challengeHour", sharedPref.getBoolean("challengeHour",false));
            pref.putBoolean("challengedToday", sharedPref.getBoolean("challengedToday",false));
            pref.putString("challengeVariety", sharedPref.getString("challengeVariety","none"));
            sharedPref.edit().putBoolean("challengeAlarmTriggered",false).apply();
        }
        pref.flush();


        new GameBatchUpdate(pref, updatePref, this.getContext(), gameProcess).execute();


        System.out.println("AndroidLauncher: Android Launcher create!");

        LifeListener gameListener = LifeListener.getLifeListener();

        this.addLifecycleListener(gameListener);

        AccelAlarm alarm = new AccelAlarm();
        alarm.setPref(pref);
        alarm.setVersion(GAMIFY_VERSION);

        alarm.setAlarm(this, GAMIFY_VERSION, fakeID);


        // For testing clear prefs here
        //pref.clear();



        setContentView(R.layout.loginscreenres);
        gameProcess.setPref(pref);
        gameProcess.setGraphPref(graphPref);
        //gameProcess.storeUpdatePrefs(updatePref);
        gameListener.setStatus(true);
        initialize(gameProcess, config);

	}

    protected void onResume(){
        Bundle extras = this.getIntent().getExtras();

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("bitPref", 0);
        SharedPreferences.Editor edit = sharedPref.edit();


        if(sharedPref.getString("currentFood", null) != null){

            pref.putString("latestFood", sharedPref.getString("currentFood", null));
            pref.flush();
            edit.remove("currentFood");
            edit.apply();

        }

        // Force clearing for testing
        //sharedPref.edit().clear().commit();

        //ActionResolverAndroid actionResolverAndroid = ActionResolverAndroid.getActionResolverAndroid(this, true);
        //gamifyGame gameProcess = gamifyGame.getGamifyGame(actionResolverAndroid);




        System.out.println("AndroidLauncher: Android Launcher onResume!");

        super.onResume();

    }

    class GameBatchUpdate extends GameBatchUpdater<String> {
        public GameBatchUpdate(Preferences basicPref, Preferences updaterPrefs, Context mainContext, gamifyGame gamifyGame){
            super(basicPref, updaterPrefs, mainContext, gamifyGame);
        }
    }

}
