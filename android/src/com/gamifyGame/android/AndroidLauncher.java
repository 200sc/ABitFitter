package com.gamifyGame.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.gamifyGame.gamifyGame;


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
        display.getSize(size); //TODO: Do what android studio says in the red line
        float width = size.x;
        float height = size.y;
        System.out.println("AndroidLauncher: Screen Width: "+ width + " Height: " + height );
        pref.putFloat("screenWidth",  width);
        pref.putFloat("screenHeight", height);


        pref.putString("userID", fakeID);
        pref.flush();


        new GameBatchUpdate(pref, updatePref, this.getContext(), gameProcess).execute();


        System.out.println("AndroidLauncher: Android Launcher create!");

        LifeListener gameListener = LifeListener.getLifeListener();

        this.addLifecycleListener(gameListener);

        AccelAlarm alarm = new AccelAlarm();
        alarm.setPref(pref);
        alarm.setAlarm(this, GAMIFY_VERSION, fakeID);


        // For testing clear prefs here
        //pref.clear();



        setContentView(R.layout.loginscreenres);
        gameProcess.setPref(pref);
        gameProcess.setGraphPref(graphPref);
        //gameProcess.storeUpdatePrefs(updatePref);
        gameListener.setStatus(true);
        initialize(gameProcess, config);

        // CREATE FILE LISTENERS AND ATTACH APPROPRIATE

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
