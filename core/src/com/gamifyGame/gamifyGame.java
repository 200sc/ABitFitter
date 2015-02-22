package com.gamifyGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class gamifyGame extends Game {
    private Preferences pref, graphPref;
    private ActionResolver actionResolver;
    public MainScreen mainS;
    public testScreen testS;
    public Quad1Screen quad1S;
    public Quad2Screen quad2S;
    public Quad3Screen quad3S;
    public Quad4Screen quad4S;
    public ConsumableScreen consumableScreen;
    public BuildingScreen buyS;
    private listenerHelper helper;
    boolean paused;

    private ChangingImage[] rooms;
    private int[] bridges;

    private static gamifyGame gamifyGame;
    private long vitality;
    private float secondsSinceLastCall = 0;
    private boolean isLoadingSomething;
    SkyImage sky;

    public String challengeText;
    //READ THIS
    //READDDD

    //http://www.gamefromscratch.com/post/2013/11/27/LibGDX-Tutorial-9-Scene2D-Part-1.aspx


    public static gamifyGame getGamifyGame(ActionResolver actionResolver) {
        if (gamifyGame == null)
            gamifyGame = new gamifyGame(actionResolver);
        else
            gamifyGame.actionResolver = actionResolver;
        return gamifyGame;
    }

    public void setActionResolver(ActionResolver actionResolver) {
        this.actionResolver = actionResolver;
    }

    private gamifyGame(ActionResolver actionResolver) {
        this.actionResolver = actionResolver;
    }

    public void create() {

        Gdx.input.setCatchBackKey(true);
        paused = false;


        renderHelper.forceRemake(pref.getFloat("screenWidth", 1200f), pref.getFloat("screenHeight", 1824f));

        helper = new listenerHelper(this);
        mainS = new MainScreen(this);
        testS = new testScreen(this, actionResolver, helper, pref);
        quad1S = new Quad1Screen(this);
        quad3S = new Quad3Screen(this);
        quad4S = new Quad4Screen(this);
        buyS = new BuildingScreen(this);
        consumableScreen = new ConsumableScreen(this);
        challengeText = "";
        vitality = this.getPrefs().getLong("vitality", 0);

        setScreen(mainS);
    }

    public void updateVitality(float delta) {
        secondsSinceLastCall += delta;
        if (secondsSinceLastCall > 30) {
            Json json = new Json();
            String[] underground = json.fromJson(String[].class, pref.getString("undergroundBuildings"));
            secondsSinceLastCall -= 30;

            ArrayList<Consumable> activeConsumables = consumableScreen.getActiveConsumables();

            for (String name : underground) {
                if (!name.equals("Empty")) {
                    Building currentBuilding = Building.getDefaultBuildings().get(name);

                    TriggerCondition curActivity=null;
                    try {
                        curActivity = TriggerCondition.valueOf(this.getPrefs().getString("curActivity").toUpperCase());
                    }
                    catch (IllegalArgumentException e)
                    {

                    }
                    /*
                      case 0: return "inactive";
                      case 1: return "active";
                      case 2: return "running";
                      case 3: return "cycling";
                      case 4: return "dancing";
                      default: return "nothing";
                     */
                    if (currentBuilding.getTriggerCondition()== TriggerCondition.ALL || curActivity == currentBuilding.getTriggerCondition()) {
                        vitality += currentBuilding.getVitalityPer30Seconds();
                    }

                }
            }
        }
        this.getPrefs().putLong("vitality", vitality);
        this.getPrefs().flush();
    }

    public void challengeComplete() {
        // do some big thing with vitality
        addToVitality(10000l);
    }

    public void updateChallenge() {
        challengeText = "Starting!";
        if (isLoadingSomething || pref.getInteger("challengeProgress") == 100) {
            if (pref.getBoolean("challengeComplete", false)) {
                pref.putBoolean("challengeComplete", true);
                challengeText = "Yay!";
                challengeComplete();
            }
            challengeText = "Done!";
            return;
        }
        challengeText = "Step 2";
        String challenge = pref.getString("challengeVariety", "none");
        int progress = 0;
        if (challenge.equals("Be active this hour!")) {
            progress = 5 * (pref.getInteger("minutesWalkedThisHour", 0) + pref.getInteger("minutesRanThisHour", 0)
                    + pref.getInteger("minutesDancedThisHour", 0) + pref.getInteger("minutesBikedThisHour", 0));

        } else if (challenge.equals("Try a new food!")) {
            progress = 100 * pref.getInteger("newFoodThisHour");
        }
        challengeText = "Step 3";
        pref.putInteger("challengeProgress", Math.min(progress, 100));
        challengeText = "Done";
    }


    public void pause() {
        paused = true;
        serverHelper.sendBuidlings(pref.getString("userID"), pref.getString("undergroundBuildings"));
        serverHelper.sendVitality(pref.getString("userID"), getVitality());
    }

    public void resume() {
        paused = false;
    }

    public void storeUpdatePrefs(Preferences updatePref) {
        Map<String, ?> kvPairs = updatePref.get();
        updatePref.clear();
        Set<String> keySet = kvPairs.keySet();
        for (Iterator i = keySet.iterator(); i.hasNext(); ) {
            String key = String.valueOf(i.next());
            String val = String.valueOf(kvPairs.get(key));
            graphUpdate(key, val);
            System.out.println("GAMIFYGAME: " + key + " " + String.valueOf(val));
        }
        updatePref.flush();
    }

    public void setLoadingFlag(boolean value) {
        isLoadingSomething = value;
    }

    public boolean getLoadingFlag() {
        return isLoadingSomething;
    }

    public void graphUpdate(String inKey, String val) {
        Date date = new Date(Long.valueOf(inKey));
        DateFormat format = new SimpleDateFormat("HH,mm", Locale.US);
        format.setTimeZone(TimeZone.getDefault());
        String key = format.format(date);
        pref.putString("graphTmp", "I got " + val + " at time " + key);

        graphPref.putString("activity" + key, val);
        if (val.equals("running")) {
            pref.putInteger("minutesRanThisHour", pref.getInteger("minutesRanThisHour", 0) + 1);
        } else if (val.equals("active")) {
            System.out.println("GAMIFYGAME: gonna increse it");
            pref.putInteger("minutesWalkedThisHour", pref.getInteger("minutesWalkedThisHour", 0) + 1);
        } else if (val.equals("dancing")) {
            pref.putInteger("minutesDancedThisHour", pref.getInteger("minutesDancedThisHour", 0) + 1);
        } else if (val.equals("cycling")) {
            pref.putInteger("minutesBikedThisHour", pref.getInteger("minutesBikedThisHour", 0) + 1);
        }
        pref.flush();
        graphPref.flush();
    }

    public boolean isActive() {
        return !paused;
    }

    public Preferences getPrefs() {
        return pref;
    }

    public ActionResolver getActionResolver() {
        return actionResolver;
    }

    public listenerHelper getListenerHelper() {
        return helper;
    }

    public void sendInt(String key, int val) {
        //serverHelper.sendTestConfirm(val); //TODO: different application of this function,
        // might not need this function later.
    }

    // Setter(s)
    public void setPref(Preferences preferences) {
        pref = preferences;
    }

    public void setGraphPref(Preferences preferences) {
        graphPref = preferences;
    }

    public Long getVitality() {
        return vitality;
    }

    public void addToVitality(Long toAdd) {
        vitality += toAdd;
    }


}

