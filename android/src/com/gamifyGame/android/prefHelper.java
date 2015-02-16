package com.gamifyGame.android;

import com.badlogic.gdx.Preferences;

/**
 * Created by Folly on 2/15/2015.
 */
public class prefHelper {
    Preferences pref;
    private static prefHelper prefH;

    public static prefHelper getPrefHelper()
    {
        if(prefH==null)
            prefH=new prefHelper();
        return prefH;
    }

    public void setPref(Preferences prefs){
        this.pref = prefs;
    }

    public Preferences getPref(){
        return this.pref;
    }
}
