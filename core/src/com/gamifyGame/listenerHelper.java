package com.gamifyGame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Json;

/**
 * Created by Patrick Stephen on 2/1/2015.
 * This guy should hold all the listeners. Anyone who wants a listener should go here to get it.
 */
public class listenerHelper {
    private final gamifyGame game;
    private ClickListener challengeListener;
    private ClickListener returnS, goS1, goS2, goS3, goS4, goS5, testYes, testNo, scanAction;

    public listenerHelper(gamifyGame gamify){
        this.game = gamify;
        returnS = new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {game.setScreen(game.mainS); return true;}};
        goS1 = new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {game.setScreen(game.quad1S); return true;}};
        goS2 = new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {game.setScreen(game.quad2S); return true;}};
        goS3 = new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {game.setScreen(game.quad3S); return true;}};
        goS4 = new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {game.setScreen(game.quad4S); return true;}};
        goS5 = new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {game.setScreen(game.buyS); return true;}};
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

    public ClickListener goScreen(int val){
        switch (val){
            case 0: return returnS;
            case 1: return goS1;
            case 2: return goS2;
            case 3: return goS3;
            case 4: return goS4;
            case 5: return goS5;
            default: return null;
        }
    }

    public void dragListeners(Image[] imageHandles, ChangingImage[] underground){
        for(int i=0; i <= imageHandles.length-1; i++){
            imageHandles[i].addListener(scroll(imageHandles,underground, false));
        }
    }

    public DragListener scroll(final Image[] imgHandles, final ChangingImage[] underground, final boolean isLongBar){
        return new DragListener(){
            float startX, startY, sY, sX;
            Color startColor;
            boolean notScroll = false;
            boolean isDown = false;
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){

                startX = x; startY = y; sY = event.getListenerActor().getY();
                //sX = event.getListenerActor().getX();
                startColor = new Color(event.getListenerActor().getColor()); //Deep copy
                if(isLongBar == false){
                    //event.getListenerActor().setColor(Color.GREEN);
                    for(int i=0; i <underground.length; i++){underground[i].setColor(Color.GREEN);}
                }
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                Image eventImage = (Image) event.getListenerActor();
                if(isLongBar == false){
                    eventImage.setColor(startColor);
                    for(int i=0; i <underground.length; i++){underground[i].setColor(startColor);}
                    int index = renderHelper.getRenderHelper().buildCheck(underground,eventImage);
                    eventImage.moveBy(sX-eventImage.getX(), sY-eventImage.getY());
                    if(index != -1){
                        //Store the change in prefs.
                        Json json = new Json();
                        Preferences pref = game.getPrefs();
                        String[] underground = json.fromJson(String[].class, pref.getString("undergroundBuildings"));
                        underground[index] = eventImage.getName();
                        pref.putString("undergroundBuildings", json.toJson(underground));
                        pref.flush();
                    }
                }

            }
            public void touchDragged(InputEvent event, float x, float y, int pointer)
            {
                Image eventImage = (Image) event.getListenerActor();
                if(sY-eventImage.getY() > eventImage.getHeight()/3 || notScroll ){
                    notScroll = true;
                    eventImage.setColor(Color.RED);
                    eventImage.moveBy(x-startX/2, y-startY);
                }
                else {
                    sX = eventImage.getX();
                    renderHelper.getRenderHelper().moveScroll(imgHandles, (x - startX) / 2, 0);
                    if (isLongBar == false) {eventImage.moveBy(0, y - startY);}
                }
            }};
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
