package com.gamifyGame;

import java.io.File;

/**
 * Created by Futility on 11/11/2014.
 */


public interface ActionResolver {
    public void scanAct(CharSequence text);
    public void toHomeScreen(CharSequence text);
    public void showToast(CharSequence text);
    public void putSharedPrefs(String key, String value);
    public void setSleepState(boolean isSleeping);
    public File getContextString();
}
