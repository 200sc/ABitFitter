package com.gamifyGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;

import java.util.HashMap;

/**
 * Created by Andrew on 2/17/2015.
 */
public class TextDisplayBox extends GamifyImage
{
    private HashMap<Point, TextInfo> locationToText;
    private Color myBaseColor;



    public TextDisplayBox(String path)
    {
        super(path);
        locationToText=new HashMap<Point, TextInfo>();
        this.myBaseColor=new Color(this.getColor());
    }

    public void addText(Point location, String text)
    {
        locationToText.put(location, new TextInfo(text, "medium"));
    }

    public void addText(Point location, String text, String fontSize)
    {
        locationToText.put(location, new TextInfo(text, fontSize));
    }

    public void draw(Batch b, float parentAlpha)
    {
        super.draw(b, parentAlpha);
        b.end();
        renderHelper.getRenderHelper().getBatch().begin();
        for(Point curLoc: locationToText.keySet())
        {
            renderHelper.getRenderHelper().drawTextOnImageNicely(locationToText.get(curLoc).getTextToWrite(), this, curLoc.x, curLoc.y,locationToText.get(curLoc).getSizeOfFont() );
        }
        renderHelper.getRenderHelper().getBatch().end();
        b.begin();
    }

    public void resetColor()
    {
        this.setColor(myBaseColor);
    }

    private class TextInfo extends Object{
        String textToWrite;String sizeOfFont;
        public TextInfo(String text, String size){
            textToWrite = text;
            sizeOfFont = size;
        }
        public String getTextToWrite(){return textToWrite;}
        public String getSizeOfFont(){return sizeOfFont;}

    }

}
