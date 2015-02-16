package com.gamifyGame.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IntegerRes;
import android.view.View;
import android.widget.Toast;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import com.gamifyGame.gamifyGame;

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
        Toast.makeText(this,"Comps",Toast.LENGTH_SHORT).show();
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

        try {
            updatePref.clear();
            File toRead = new File(this.getFilesDir(), "updateFile");
            BufferedReader reader = new BufferedReader(new FileReader((toRead)));
            String line = null;
            String[] lineParts;
            HashMap<String,String> updateFile = new HashMap<String,String>();
            int i = 0;
            while ((line = reader.readLine()) != null){
                lineParts = line.split(",");
                updateFile.put(lineParts[0],lineParts[1]);
            }
            updatePref.put(updateFile);
            updatePref.flush();
            // Replace fakeID with userID when userID is implemented
            reader.close();
        }
        catch (Exception e){
            assert (1 == 0);
        }
        pref.putString("userID", fakeID);
        pref.flush();

        AccelAlarm alarm = new AccelAlarm();
        alarm.setPref(pref);
        alarm.setVersion(GAMIFY_VERSION);
        alarm.setAlarm(this, GAMIFY_VERSION, fakeID);


        setContentView(R.layout.loginscreenres);
        gameProcess.setPref(pref);
        gameProcess.setGraphPref(graphPref);
        gameProcess.storeUpdatePrefs(updatePref);
        initialize(gameProcess,config);

	}

    protected void onResume(){
        Bundle extras = this.getIntent().getExtras();

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("bitPref", 0);

        if(sharedPref.getString("currentFood", null) != null){
            Toast.makeText(this, "Hello Here We Are", Toast.LENGTH_SHORT).show();
            pref.putString("latestFood", sharedPref.getString("currentFood", null));
            pref.flush();
        }

        Toast.makeText(this, "PRINNTTTT" + sharedPref.getString("currentFood", null), Toast.LENGTH_SHORT).show();
        super.onResume();

    }

}
