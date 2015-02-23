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
public class HistogramGraph extends GamifyGraph {

    public HistogramGraph(HashMap<Long,Integer> graphPref, String dataType, GamifyColor inColor, String scale, int x, int y) {
        data = graphPref;
        type = dataType;
        keys = asSortedList(data.keySet());
        xPoints = new ArrayList<Long>();
        yPoints = new ArrayList<Integer>();
        show();
        switch (inColor){
            case PINK:
            case BLUE:
                color1 = renderHelper.getRenderHelper().blueLight;
                color2 = renderHelper.getRenderHelper().blueDark;
                color3 = renderHelper.getRenderHelper().blueOutline;
                break;
            case GREEN:
                color1 = renderHelper.getRenderHelper().greenLight;
                color2 = renderHelper.getRenderHelper().greenDark;
                color3 = renderHelper.getRenderHelper().greenOutline;
                break;
            case YELLOW:
                color1 = renderHelper.getRenderHelper().yellowLight;
                color2 = renderHelper.getRenderHelper().yellowDark;
                color3 = renderHelper.getRenderHelper().yellowOutline;
                break;
        }
        if (scale.equals("small")){
            dataPointCount = 32;
        }
        //else if (scale.equals("large")){
            //dataPointCount = 8;
        //}
        else dataPointCount = 16;
        borderX = x;
        borderY = y;
        update();
    }

    public void shapeRender(){
        ShapeRenderer shapes1 = renderHelper.getRenderHelper().getShapeRenderer();

        float yRatio = (((float)graphHeight-15) / yMax);
        int xIncrement = (xPoints.size()/dataPointCount);
        int xPixelIncrement = (graphWidth/dataPointCount);

        int x = 0;

        renderHelper renderer = renderHelper.getRenderHelper();

        shapes1.begin(ShapeRenderer.ShapeType.Filled);
        shapes1.setColor(color1);
        for (int i = 0; i < dataPointCount; i+= 2) {
            if(i % 2 == 0) {
                shapes1.box(borderX + (xPixelIncrement * i), borderY, 0, xPixelIncrement, yPoints.get(x) * yRatio, 0);
                x += xIncrement*2;
            }
        }
        shapes1.end();
        shapes1.begin(ShapeRenderer.ShapeType.Filled);
        shapes1.setColor(color2);
        x = xIncrement;
        for (int i = 1; i < dataPointCount; i+=2) {
            shapes1.box(borderX + (xPixelIncrement * i), borderY, 0, xPixelIncrement, yPoints.get(x) * yRatio, 0);
            x += xIncrement*2;
        }
        shapes1.end();
        shapes1.begin(ShapeRenderer.ShapeType.Line);
        shapes1.setColor(color3);
        for (int i = 0; i < graphHeight; i+= graphHeight / leftLabelCount){
            shapes1.line(borderX,borderY+i,borderX+130,borderY+i);
            shapes1.line(borderX,borderY+i+1,borderX+130,borderY+i+1);
        }
        x = 0;
        for (int i = 0; i < dataPointCount; i+= 2) {
            shapes1.box(borderX + (xPixelIncrement * i), borderY, 0, xPixelIncrement, yPoints.get(x) * yRatio, 0);
            x += xIncrement*2;
        }
        shapes1.end();

    }

    public void textRender(){

        int xIncrement = (xPoints.size()/dataPointCount);
        int xPixelIncrement = (graphWidth/dataPointCount);

        int x = 0;
        int borderX = 19;
        int borderY = 25;

        renderHelper renderer = renderHelper.getRenderHelper();

        renderer.textSet(type,borderX+1,borderY+graphHeight-2,GamifyTextSize.XTRABIG);

        for (int i = 0; i < botLabelCount; i++) {
            Date date = new Date(xPoints.get(x));
            DateFormat format = new SimpleDateFormat("MMM dd\nHH mm");
            format.setTimeZone(TimeZone.getDefault());
            String formatted = format.format(date);
            renderer.textSet(formatted, (borderX+10 + (xPixelIncrement * i)*(dataPointCount/botLabelCount)), borderY);
            x += xIncrement;
        }
        x = 0;
        for (int i = 0; i < graphHeight; i+= graphHeight / leftLabelCount){
            renderer.textSet(String.valueOf((int)((yMax/leftLabelCount)*x)),borderX,borderY+i+7,GamifyTextSize.SMALL);
            x++;
        }
    }

    public void show(){
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
