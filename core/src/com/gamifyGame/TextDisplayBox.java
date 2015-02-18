package com.gamifyGame;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;

import java.util.HashMap;

/**
 * Created by Andrew on 2/17/2015.
 */
public class TextDisplayBox extends GamifyImage
{
    private HashMap<Point, String> locationToText;

    private float pixelsPerSecondX;
    private float pixelsPerSecondY;
    private float remainingMoveTime;
    private float remainingWaitingTime;

    public TextDisplayBox()
    {
        super("midBox.png");
        locationToText=new HashMap<Point, String>();
        remainingMoveTime =0;

        this.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                //super.act(delta);
                if(remainingWaitingTime >0)
                {
                    remainingWaitingTime -=delta;
                }
                else
                {
                    remainingWaitingTime =0;
                    if(remainingMoveTime <=0)
                    {
                        remainingMoveTime =0;
                    }
                    else
                    {
                        moveBy(delta*pixelsPerSecondX, delta*pixelsPerSecondY);
                        remainingMoveTime -=delta;
                    }
                }
                return false;
            }
        });
    }

    public void addText(Point location, String text)
    {
        locationToText.put(location, text);
    }

    public void draw(Batch b, float parentAlpha)
    {
        super.draw(b, parentAlpha);
        b.end();
        renderHelper.getRenderHelper().getBatch().begin();
        for(Point curLoc: locationToText.keySet())
        {
            renderHelper.getRenderHelper().drawTextOnImage(locationToText.get(curLoc), this, curLoc.x, curLoc.y);
        }
        renderHelper.getRenderHelper().getBatch().end();
        b.begin();
    }




    private boolean gradualMoveBy(float x, float y, float time)
    {
        remainingMoveTime =time;
        pixelsPerSecondX =x;
        pixelsPerSecondY =y;
        return true;
    }

    public void gradualMoveToPosition(float x, float y, float time)
    {
        remainingWaitingTime =0;
        gradualMovePos(x, y, time);
    }
    private void gradualMovePos(float x, float y, float time)
    {
        Math.abs(this.getX()-x);

        if(Math.abs(this.getX()-x)<1 && Math.abs(this.getY()-y)<1)
        {
            this.setPosition(x, y);
        }
        else
        {
            this.gradualMoveBy( (x-this.getX())/time, (y-this.getY())/time, time);
        }
    }

    public void waitThenGradualMoveToPosition(float x, float y, float moveTime, float waitTime)
    {
        if(!(remainingWaitingTime==0 && remainingMoveTime>0))
            remainingWaitingTime =waitTime;
        gradualMovePos(x, y, moveTime);
    }
}
