package com.gamifyGame;

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * Created by Andrew on 2/19/2015.
 */
public class MovingTextDisplayBox extends TextDisplayBox
{
    private float pixelsPerSecondX;
    private float pixelsPerSecondY;
    private float remainingMoveTime;
    private float remainingWaitingTime;

    private float queuedGoalx;
    private float queuedGoalY;
    private float queuedRemainingMoveTime;
    private float queuedRemainingWaitTime;

    public MovingTextDisplayBox(String path)
    {
        super(path);
        remainingMoveTime =0;
        remainingWaitingTime=0;
        queuedRemainingMoveTime =0;
        queuedRemainingWaitTime =0;

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
                        remainingMoveTime=0;
                        if(queuedRemainingMoveTime>0) {
                            waitThenGradualMoveToPosition(queuedGoalx, queuedGoalY, queuedRemainingMoveTime, queuedRemainingWaitTime);
                            queuedRemainingMoveTime = 0;
                            queuedRemainingWaitTime = 0;
                            queuedRemainingMoveTime = 0;
                            queuedRemainingWaitTime = 0;
                        }
                    }
                    if(remainingMoveTime > 0 && remainingWaitingTime<=0)
                    {
                        moveBy(delta*pixelsPerSecondX, delta*pixelsPerSecondY);
                        remainingMoveTime -=delta;
                    }
                }
                return false;
            }
        });
    }
    private boolean gradualMoveBy(float x, float y, float time)
    {
        remainingMoveTime = time;
        pixelsPerSecondX = x;
        pixelsPerSecondY = y;

        return true;
    }

    public void gradualMoveToPosition(float x, float y, float time)
    {
        if(remainingMoveTime<=0) {
            remainingWaitingTime = 0;
            queuedRemainingMoveTime = 0;
            queuedRemainingWaitTime = 0;
            queuedGoalx = 0;
            queuedGoalY = 0;
            gradualMovePos(x, y, time);
        }
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
        if(remainingMoveTime==0 && remainingWaitingTime==0) {
            remainingWaitingTime = waitTime;
            gradualMovePos(x, y, moveTime);
        }
        else if(queuedRemainingMoveTime ==0)
        {
            queuedRemainingWaitTime =waitTime;
            queuedRemainingMoveTime=moveTime;
            queuedGoalx=x;
            queuedGoalY=y;
        }
    }
}
