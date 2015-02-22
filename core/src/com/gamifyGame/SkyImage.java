package com.gamifyGame;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.Calendar;

/**
 * Created by Stephen on 2/22/2015.
 */
public class SkyImage extends GamifyImage {

    final private int increment = 291892;
    final private int day = 86400000;
    static SkyImage sky;
    private Image time;
    private String dayTime;

    public static SkyImage getSkyImage (String path){
        if (sky == null) {sky = new SkyImage(path);}
        return sky;
    }

    private SkyImage(String path){
        super(path);
        dayTime = timeOfDay();
        time = renderHelper.getRenderHelper().imageSetup( dayTime, renderHelper.getRenderHelper().getLayer(0), 0, 0);
        this.addAt(renderHelper.getRenderHelper().getLayer(0),0,-296);
        renderHelper.getRenderHelper().imageSetup("background.png", renderHelper.getRenderHelper().getLayer(0), 0, 0);
        this.addAction(new Action(){
            public boolean act(float delta){
                setPosition(0,(float)Math.floor((System.currentTimeMillis() % day)/increment)-296);
                if(dayTime != timeOfDay()) {
                    dayTime = timeOfDay();
                    time.remove();
                    time = renderHelper.getRenderHelper().imageSetup( dayTime, renderHelper.getRenderHelper().getLayer(0), 0, 0);
                }
                return false;
            }
        });
    }

    public String timeOfDay(){
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour < 5){ return "midnight.png";}
        else if (hour < 9){ return "sunrise.png";}
        else if (hour < 17){ return "day.png";}
        else return "night.png";
    }
}
