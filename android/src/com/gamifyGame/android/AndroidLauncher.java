package com.gamifyGame.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.FileObserver;
import android.view.Display;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.gamifyGame.gamifyGame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;


public class AndroidLauncher extends AndroidApplication {

    private final String GAMIFY_VERSION = "0.1.1a";
    Preferences pref;
    FileObserver outChallengeWatch;

	@Override
	protected void onCreate (Bundle savedInstanceState)
    {

		super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        ActionResolverAndroid actionResolverAndroid = ActionResolverAndroid.getActionResolverAndroid(this, true);
        final gamifyGame gameProcess = gamifyGame.getGamifyGame(actionResolverAndroid);

        Bundle extras = this.getIntent().getExtras();
        try{String userID = (String) extras.get("ID");}
        catch(Exception e){String userID = "4321";}

        // Make a fake ID, Replace when userID is implemented
        pref = this.getPreferences("Bitfitpref");
        final Preferences updatePref = this.getPreferences("Update");
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


        new GameBatchUpdate(pref, updatePref, this.getContext(), gameProcess, "main").execute();


        System.out.println("AndroidLauncher: Android Launcher create!");

        LifeListener gameListener = LifeListener.getLifeListener();

        this.addLifecycleListener(gameListener);

        AccelAlarm alarm = new AccelAlarm();
        alarm.setPref(pref);
        alarm.setAlarm(this, GAMIFY_VERSION, fakeID);


        // For testing clear prefs here
        //pref.clear();


        // CREATE FILE LISTENERS AND ATTACH APPROPRIATE
        File sharedPref = new File(getApplicationContext().getFilesDir(), "/outChallenge");
        System.out.println("Android Launcher: " + sharedPref + "  gahhhhh " + getApplicationContext().getFilesDir() + "outChallenge");
         outChallengeWatch =  new FileObserver(getApplicationContext().getFilesDir()+""){
            public void onEvent(int event, String file){

                if(event == FileObserver.MODIFY){
                    this.stopWatching();
                    System.out.println("GAMEBATCHUPDATER: THE FILE IS " + file);
                    if(file.equals("outChallenge")) challengeFileUpdater();
                    this.startWatching();
                }

            }
        };
        outChallengeWatch.startWatching();




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
        public GameBatchUpdate(Preferences basicPref, Preferences updaterPrefs, Context mainContext, gamifyGame gamifyGame, String toDo){
            super(basicPref, updaterPrefs, mainContext, gamifyGame, toDo);
        }
    }


    public boolean challengeFileUpdater(){
        try {
            File sharedPref = new File(getApplicationContext().getFilesDir(), "outChallenge");

            BufferedReader br = new BufferedReader(new FileReader(sharedPref));
            String line;
            String[] lineList;

            List<String> booleanList = Arrays.asList("challengedToday", "challengeHour", "newFoodThisHour");
            while((line = br.readLine()) != null){
                // process
                lineList = line.split(",");
                if(booleanList.contains(lineList[0])){
                    pref.putBoolean(lineList[0], Boolean.parseBoolean(lineList[1]));
                }else{
                    pref.putInteger(lineList[0], Integer.parseInt(lineList[1]));
                }
            }
            FileWriter o = new FileWriter(sharedPref,false);
            o.write("");
            pref.flush();
        }catch(Exception e) {return false;
        }
        System.out.println("ANDROID LAUNCHER: CHALLENGE UPDATE");
        return true;
    }

}
