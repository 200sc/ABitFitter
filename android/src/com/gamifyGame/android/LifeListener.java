package com.gamifyGame.android;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.LifecycleListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Folly on 2/17/2015.
 */
public class LifeListener implements LifecycleListener {
    private boolean isAlive;
    private static LifeListener gameListener;


   public static LifeListener getLifeListener(){
       if(gameListener ==null) gameListener=new LifeListener();
       return gameListener;
   }

   private LifeListener(){
       isAlive = false;

   }

    @Override
    public void pause() {
        isAlive = false;
    }

    @Override
    public void resume() {
        //isAlive = true;
    }

    @Override
    public void dispose() {
        isAlive = false;



    }

    public boolean getStatus(){
        return isAlive;
    }

    public void setStatus(boolean status){
        isAlive = status;
    }



    class buildingUpdate extends PostJsonTask<String> {
        JSONObject tempRes = new JSONObject();
        String username;
        String userID;

        public buildingUpdate(String serverAddress, String endpoint) {
            super(serverAddress, endpoint);
        }

        protected String parseResponse(String response){
            return response;
        }

        @Override
        protected void onPostExecute(String output) {
            System.out.println("Finished updating buildings");
        }
    }



}
