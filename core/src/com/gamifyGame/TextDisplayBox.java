package com.gamifyGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

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
        addText(location, text, GamifyTextSize.MEDIUM);
    }

    public void addText(Point location, String text, GamifyTextSize fontSize)
    {
        addText(location, text, fontSize, GamifyColor.WHITE);
    }
    public void addText(Point location, String text, GamifyTextSize fontSize, GamifyColor color){
        locationToText.put(location, new TextInfo(text, fontSize, color));
    }


    public void draw(Batch b, float parentAlpha)
    {
        super.draw(b, parentAlpha);
        b.end();
        renderHelper.getRenderHelper().getBatch().begin();
        for(Point curLoc: locationToText.keySet())
        {
            renderHelper.getRenderHelper().drawTextOnImageNicely(locationToText.get(curLoc).getTextToWrite(),
                    this, curLoc.x, curLoc.y,locationToText.get(curLoc).getSizeOfFont(), locationToText.get(curLoc).getColorOfFont() );
        }
        renderHelper.getRenderHelper().getBatch().end();
        b.begin();
    }

    public void resetColor()
    {
        this.setColor(myBaseColor);
    }

    private class TextInfo extends Object{
        String textToWrite;GamifyTextSize sizeOfFont; GamifyColor colorOfFont;
        public TextInfo(String text, GamifyTextSize size){
            textToWrite = text;
            sizeOfFont = size;
            colorOfFont = GamifyColor.WHITE;
        }
        public TextInfo(String text, GamifyTextSize size, GamifyColor color){
            textToWrite = text;
            sizeOfFont = size;
            colorOfFont = color;
        }
        public String getTextToWrite(){return textToWrite;}
        public GamifyTextSize getSizeOfFont(){return sizeOfFont;}
        public GamifyColor getColorOfFont(){return colorOfFont;}
    }

}
