package com.gamifyGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

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
public class LineGraph {

    List<Long> keys;
    String type;
    ArrayList<Long> xPoints;
    ArrayList<Integer> yPoints;
    Color color1, color2, color3;
    float yMax;
    int dataPointCount;
    public HashMap<Long,Integer> data;
    public Image background;
    public boolean dumped;
    public final int botLabelCount = 5;
    public final int leftLabelCount = 10;

    public long end;
    public long start;

    public final int graphHeight = 240;
    public final int graphWidth = 130;

    public LineGraph(HashMap<Long,Integer> graphPref, String dataType, GamifyColor inColor) {
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
        dataPointCount = 16;
        update();
    }

    public void shapeRender(){
        ShapeRenderer shapes1 = renderHelper.getRenderHelper().getShapeRenderer();

        float yRatio = (((float)graphHeight-25) / yMax);
        int xPixelIncrement = (graphWidth/yPoints.size());

        int x = 0;
        int borderX = 48;
        int borderY = 54;

        renderHelper renderer = renderHelper.getRenderHelper();


        shapes1.begin(ShapeRenderer.ShapeType.Filled);
        shapes1.setColor(color2);
        int lastX = borderX;
        float lastY = borderY + 10;
        int thisX;
        float thisY;
        for (int i = 0; i < yPoints.size(); i++) {
            thisX = lastX + xPixelIncrement;
            thisY = (yPoints.get(i)* yRatio) + borderY + 10;
            shapes1.line(lastX,lastY,thisX,thisY);
            shapes1.circle(thisX,thisY,2);
            lastX = thisX;
            lastY = thisY;
        }
        shapes1.end();
        shapes1.begin(ShapeRenderer.ShapeType.Line);
        shapes1.setColor(color3);
        for (int i = 0; i < graphHeight; i+= graphHeight / leftLabelCount){
            shapes1.line(borderX,borderY+i,renderer.getRenderedWidth()-2,borderY+i);
        }
        shapes1.end();


    }

    public void textRender(){

        int xIncrement = (xPoints.size()/dataPointCount);
        int xPixelIncrement = (graphWidth/dataPointCount);

        int x = 0;
        int borderX = 38;
        int borderY = 54;

        renderHelper renderer = renderHelper.getRenderHelper();

        renderer.textSet(type,borderX+1,borderY+graphHeight-2,"large");

        for (int i = 0; i < botLabelCount; i++) {
            Date date = new Date(xPoints.get(x));
            DateFormat format = new SimpleDateFormat("MMM.dd\nHH:mm");
            format.setTimeZone(TimeZone.getDefault());
            String formatted = format.format(date);
            renderer.textSet(formatted, (borderX+10 + (xPixelIncrement * i)*(dataPointCount/botLabelCount)), borderY,"small");
            x += xIncrement;
        }
        x = 0;
        for (int i = 0; i < graphHeight; i+= graphHeight / leftLabelCount){
            renderer.textSet(String.valueOf((int)((yMax/leftLabelCount)*x)),borderX,borderY+i+7,"small");
            x++;
        }
    }

    public void show(){
        renderHelper renderer = renderHelper.getRenderHelper();
        Stage layer1 = renderer.getLayer(1);
        int borderX = 36;
        int borderY = 42;
        background = renderer.imageSetup("largeScreenBox.png", layer1, borderX, borderY);
        keys = asSortedList(data.keySet());
        xPoints = new ArrayList<Long>();
        yPoints = new ArrayList<Integer>();
        yMax = 0;
        for (Iterator i = keys.iterator(); i.hasNext();) {
            Object xData =  i.next();
            xPoints.add((Long)xData);
            int yPoint = data.get(xData);
            if (yPoint > yMax) {
                yMax = yPoint;
            }
            yPoints.add(yPoint);
        }

        dumped = false;
    }

    public void update(){
        end = System.currentTimeMillis();
        //------------milisec,sec, min,hour,day
        start = end - (1000 * 60 * 60 * 24 * 7);
    }

    public static
    <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
        List<T> list = new ArrayList<T>(c);
        java.util.Collections.sort(list);
        return list;
    }

}
