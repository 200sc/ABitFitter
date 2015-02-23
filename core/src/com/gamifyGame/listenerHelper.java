package com.gamifyGame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Json;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Patrick Stephen on 2/1/2015.
 * This guy should hold all the listeners. Anyone who wants a listener should go here to get it.
 */
public class listenerHelper {
    private final gamifyGame game;
    private ClickListener challengeListener;
    private ClickListener testYes, testNo, scanAction, servingsChosen, goCons;

    public listenerHelper(gamifyGame gamify){
        this.game = gamify;
        goCons= new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {game.setScreen(game.consumableScreen); return true;}};
        final Preferences pref=game.getPrefs();
        testYes = new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {pref.putInteger("confirmed",1); pref.flush(); game.sendInt("userConfirm",1); return true;}};
        testNo = new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {pref.putInteger("confirmed",-1); pref.flush(); game.sendInt("userConfirm",0); return true;}};



        scanAction = new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            { game.getActionResolver().scanAct("ScanScreen"); return true;}};

        servingsChosen = new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                new PopUpBox(60, 150, 3, " One serving of current food eaten");

                if(game.getPrefs().getString("latestFood") != null) {
                    String latestFood = pref.getString("latestFood");
                    serverHelper.sendFood(pref.getString("userID"),latestFood, 1);



                    // create recent food list
                    String recentFoodCheck = pref.getString("recentFoods");
                    Json json = new Json();
                    if(recentFoodCheck == null) {
                        PriorityQueue<String> queueForPrefs = new PriorityQueue<String>();
                        pref.putString("recentFoods", json.toJson(queueForPrefs));
                    }

                    /*try {
                        PriorityQueue recentFoods = json.fromJson(PriorityQueue.class, recentFoodCheck);


                        if(!recentFoods.contains(latestFood)) {

                            if (recentFoods.size() > 5) {
                                recentFoods.poll();
                            }

                            recentFoods.add(latestFood);

                            pref.putString("recentFoods", json.toJson(recentFoods));
                            pref.flush();
                        }

                    }catch(Exception e){
                            new PopUpBox(60, 150, 10, "recent foods is not a queue");
                    }*/
                }


                return true;
            }};

        challengeListener = new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ChangingImage eventImage = (ChangingImage) event.getListenerActor();
                eventImage.swapTexture();
                pref.putBoolean(eventImage.getString("time"),eventImage.getName().equals(eventImage.name2));
                pref.flush();
                return true;
            }
        };
    }

    public ClickListener getChallengeListener(){return challengeListener;}
    public ClickListener getTestYes(){return testYes;}
    public ClickListener getTestNo(){return testNo;}
    public ClickListener scanningAction(){return scanAction;}
    public ClickListener getServingsChosen(){return servingsChosen;}


    public ClickListener setInt(final String key,final String str){
        return new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                int val;
                if (str.equals("++")) val = game.getPrefs().getInteger(key,0) + 1;
                else val = game.getPrefs().getInteger(key,0) - 1;
                game.getPrefs().putInteger(key,val);
                game.getPrefs().flush(); return true;}};
    }

    public ClickListener setInt(final String key,final int val){
        return new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {game.getPrefs().putInteger(key,val); game.getPrefs().flush(); return true;}};
    }

    public ClickListener setBoolean(final String key, final boolean val){
        return new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {game.getPrefs().putBoolean(key,val); game.getPrefs().flush(); return true;}};
    }

    public ClickListener setBoolean(final String key, final char inp){
        boolean val = true;
        switch (inp){
            case 't': val = true; break;
            case 'f': val = false; break;
            case 'a':
                return new ClickListener(){
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                    {
                        if (!(game.getPrefs().getBoolean(key,false))){game.getPrefs().putBoolean(key,true);}
                        else game.getPrefs().putBoolean(key,false);
                        game.getPrefs().flush();
                        return true;}};
        }
        return setBoolean(key,val);
    }
}
