package com.gamifyGame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Stephen on 2/16/2015.
 */
public class SpiderGraph {

    List<Integer> keys;
    String type;
    Color color1, color2, color3;
    public HashMap<Integer,Integer> data;
    public Image background;
    public boolean dumped;
    private int borderX;
    private int borderY;
    private long min = 0;
    private long max = 70;

    float hex1X = 1;
    float hex1Y = 0;
    float hex2X = .5f;
    float hex2Y = 1;
    float hex3X = -.5f;
    float hex3Y = 1;
    float hex4X = -1;
    float hex4Y = 0;
    float hex5X = -.5f;
    float hex5Y = -1;
    float hex6X = .5f;
    float hex6Y = -1;



    public SpiderGraph(HashMap<Integer,Integer> graphPref, String dataType, GamifyColor inColor) {
        data = graphPref;
        type = dataType;
        show();
        switch (inColor){
            case PINK:
            case BLUE:
                color1 = renderHelper.getRenderHelper().blueLight;
                color2 = renderHelper.getRenderHelper().blueDark;
                color3 = renderHelper.getRenderHelper().blueOutline;
                break;
            case GREEN:
            case YELLOW:
        }
    }

    public void shapeRender(){
        ShapeRenderer shapes1 = renderHelper.getRenderHelper().getShapeRenderer();
        renderHelper renderer = renderHelper.getRenderHelper();

        float centerX = borderX + 72;
        float centerY = borderY + 125;


        // DRAW BACKGROUND HEXAGONS
        shapes1.begin(ShapeRenderer.ShapeType.Filled);
        shapes1.setColor(color2);

        for(float i = 17.5f; i <= max; i+= 17.5){
            shapes1.line(centerX+ (i*hex1X),centerY + (i*hex1Y), centerX+ (i*hex2X), centerY+ (i*hex2Y));
            shapes1.line(centerX+ (i*hex2X),centerY + (i*hex2Y), centerX+ (i*hex3X), centerY+ (i*hex3Y));
            shapes1.line(centerX+ (i*hex3X),centerY + (i*hex3Y), centerX+ (i*hex4X), centerY+ (i*hex4Y));
            shapes1.line(centerX+ (i*hex4X),centerY + (i*hex4Y), centerX+ (i*hex5X), centerY+ (i*hex5Y));
            shapes1.line(centerX+ (i*hex5X),centerY + (i*hex5Y), centerX+ (i*hex6X), centerY+ (i*hex6Y));
            shapes1.line(centerX+ (i*hex6X),centerY + (i*hex6Y), centerX+ (i*hex1X), centerY+ (i*hex1Y));
        }


        shapes1.end();

        // DRAW DATA LINES

        shapes1.begin(ShapeRenderer.ShapeType.Filled);
        shapes1.setColor(color1);

        shapes1.line(centerX+ (data.get(1)*hex1X),centerY + (data.get(1)*hex1Y), centerX+ (data.get(2)*hex2X), centerY+ (data.get(2)*hex2Y));
        shapes1.line(centerX+ (data.get(2)*hex2X),centerY + (data.get(2)*hex2Y), centerX+ (data.get(3)*hex3X), centerY+ (data.get(3)*hex3Y));
        shapes1.line(centerX+ (data.get(3)*hex3X),centerY + (data.get(3)*hex3Y), centerX+ (data.get(4)*hex4X), centerY+ (data.get(4)*hex4Y));
        shapes1.line(centerX+ (data.get(4)*hex4X),centerY + (data.get(4)*hex4Y), centerX+ (data.get(5)*hex5X), centerY+ (data.get(5)*hex5Y));
        shapes1.line(centerX+ (data.get(5)*hex5X),centerY + (data.get(5)*hex5Y), centerX+ (data.get(6)*hex6X), centerY+ (data.get(6)*hex6Y));
        shapes1.line(centerX+ (data.get(6)*hex6X),centerY + (data.get(6)*hex6Y), centerX+ (data.get(1)*hex1X), centerY+ (data.get(1)*hex1Y));

        shapes1.end();
    }

    public void textRender(){


    }

    public void show(){
        renderHelper renderer = renderHelper.getRenderHelper();
        Stage layer1 = renderer.getLayer(1);
        borderX = 0;
        borderY = 42;
        background = renderer.imageSetup("largeScreenBox.png", layer1, borderX, borderY);
        keys = asSortedList(data.keySet());
        dumped = false;
    }

    public static
    <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
        List<T> list = new ArrayList<T>(c);
        java.util.Collections.sort(list);
        return list;
    }

}
