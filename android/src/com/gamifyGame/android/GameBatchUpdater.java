package com.gamifyGame.android;
import android.content.Context;

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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


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

    public boolean challengeFileUpdater(){
        try {
            File sharedPref = new File(context.getApplicationContext().getFilesDir(), "outChallenge");

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
            sharedPref.delete();
            pref.flush();
        }catch(Exception e) {return false;
        }return true;
    }

    @Override
    protected String doInBackground(JSONObject... jsonObjects) {
        System.out.println("GAMEBATCHUPDATER: Start");

        challengeFileUpdater();

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