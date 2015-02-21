package com.gamifyGame.android;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.Preference;
import android.widget.Toast;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Preferences;
import com.gamifyGame.gamifyGame;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.HashMap;


public class GameBatchUpdater<T> extends AsyncTask<JSONObject, Void, String> {

    Preferences pref;
    Preferences updatePref;
    Context context;
    gamifyGame game;

    public GameBatchUpdater(Preferences basicPref, Preferences updaterPrefs, Context mainContext, gamifyGame gamifyGame){
        pref = basicPref;
        updatePref = updaterPrefs;
        context = mainContext;
        game = gamifyGame;

    }
    protected void onPostExecute(String output){}
    @Override
    protected String doInBackground(JSONObject... jsonObjects) {
        System.out.println("GAMEBATCHUPDATER: Start");
        SharedPreferences sharedPref = context.getSharedPreferences("bitPref", 0);
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
        try {
            game.setLoadingFlag(true);
            File toRead = new File(context.getFilesDir(), "updateFile");
            BufferedReader reader = new BufferedReader(new FileReader((toRead)));
            String line = null;
            String[] lineParts;
            HashMap<String, String> updateFile = new HashMap<String, String>();
            int i = 0;
            while ((line = reader.readLine()) != null) {
                    lineParts = line.split(",");
                updateFile.put(lineParts[0], lineParts[1]);
                System.out.println("\"GAMEBATCHUPDATER: KEYS and VALS: " + lineParts[0] + " , " + lineParts[1]);
            }
            updatePref.put(updateFile);
            System.out.print("This updatefile" + updateFile);
            updatePref.flush();
            // Replace fakeID with userID when userID is implemented
            reader.close();

            // Reset toRead
            if(!toRead.delete()){System.out.println("GAMEBATCHUPDATER: Failed to delete file");}
            if (toRead.exists()){System.out.println("GAMEBATCHUPDATER: Failed to delete file correctly");}
            game.storeUpdatePrefs(updatePref);

            updatePref.get();

        }catch(Exception e){
            System.out.println("GAMEBATCHUPDATER:" + e.getMessage());
            System.out.println("GAMEBATCHUPDATER: crash in background");
        }
        game.setLoadingFlag(false);

        System.out.println("GAMEBATCHUPDATER: Ending");

        return "Ended";
    }
    protected String parseResponse (String response){return response;}
}