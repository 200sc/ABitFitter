package com.gamifyGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Stephen on 2/15/2015.
 */
public abstract class GamifyGraph {


    String type;
    public Image background;
    public boolean dumped;
    Color color1, color2, color3;
    public HashMap<Long,Integer> data;
    ArrayList<Long> xPoints;
    ArrayList<Integer> yPoints;
    float yMax;
    int dataPointCount;
    List<Long> keys;

    public final int botLabelCount = 5;
    public final int leftLabelCount = 10;

    public long end;
    public long start;

    public final int graphHeight = 240;
    public final int graphWidth = 130;

    int borderX = 36;
    int borderY = 42;

    public void shapeRender(){

    }

    public void textRender(){
        int xIncrement = (xPoints.size()/dataPointCount);
        int xPixelIncrement = (graphWidth/dataPointCount);

        int x = 0;
        int borderX = 38;
        int borderY = 54;

        renderHelper renderer = renderHelper.getRenderHelper();

        renderer.textSet(type,borderX+1,borderY+graphHeight-2,GamifyTextSize.XTRABIG);

        if(xPoints.size() == 0){
            renderer.textSet("No data recorded yet",borderX+30,borderY+60);
            return;
        }

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

}
