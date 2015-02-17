package com.gamifyGame.android;

import com.badlogic.gdx.LifecycleListener;

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

}
