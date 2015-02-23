package com.gamifyGame.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.badlogic.gdx.Preferences;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 * Created by Folly on 1/20/2015.
 */
public final class Zxing extends Activity {

    private static final String TAG = Zxing.class.getSimpleName();
    private static final String PACKAGE_NAME = Zxing.class.getPackage().getName();


    Context context;
    Bundle thisBundle;

    public void onCreate(Bundle icicle) {
        // Enter from the scanAct call from actionResolver
        super.onCreate(icicle);

        this.thisBundle = icicle;
        this.context = getApplicationContext();
        IntentIntegrator integrator;
        integrator = new IntentIntegrator(Zxing.this);

        //Toast.makeText(getApplicationContext(), ")@(#*)@(#*$)@(*%", Toast.LENGTH_SHORT).show();
        integrator.initiateScan();
        setContentView(R.layout.scaninter);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(result==null){Toast.makeText(getApplicationContext(), "Scan canceled", Toast.LENGTH_SHORT).show();}
        String tmp = result.getContents();
        JSONObject tmpM = new JSONObject();
        try {
            tmpM.put("Contents:", tmp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //final CharSequence tmpMessage = result.toString();
        final CharSequence tmpMessage = tmp;
        //Toast.makeText(getApplicationContext(), tmpMessage, Toast.LENGTH_SHORT).show();
        Intent intent2 = new Intent(getApplicationContext(), AndroidLauncher.class);

        intent2.putExtra("Message", tmpMessage);
        new checkFood("http://www.opennutritiondatabase.com/foods/", tmp +".json").execute(tmpM);


    }
    class checkFood extends FoodRequestTask<String> {


        public checkFood(String serverAddress, String endpoint) {
            super(serverAddress, endpoint, getApplicationContext());
        }

        protected String parseResponse(String response){
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            return response;
        }

        @Override
        protected void onPostExecute(String output){

            Intent change = new Intent(getApplicationContext(), AndroidLauncher.class);

            SharedPreferences prefs = context.getSharedPreferences("bitPref", 1);
            SharedPreferences.Editor edit = prefs.edit();


            JSONObject out = null;

            try {
                out = new JSONObject(output);
                String brand = out.getString("brand_name");
                Toast.makeText(getApplicationContext(), brand, Toast.LENGTH_LONG).show();
                edit.putString("currentFood", output);

            } catch (JSONException e) {
                edit.putString("currentFood", "");
                e.printStackTrace();
            }


            edit.commit();


            startActivity(change);
        }
    }



}
