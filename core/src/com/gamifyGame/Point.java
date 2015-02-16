package com.gamifyGame;

/**
 * Created by Andrew on 2/7/2015.
 */
public class Point {
    public float x;
    public float y;

    public Point(float x, float y) {
        this.x = x;
        this.y=y;
    }

    public Point getXYDistances(Point otherPoint)
    {
        return new Point(this.x-otherPoint.x, this.y-otherPoint.y);
    }

    public void scaleBy(float toScale)
    {
        this.scaleXBy(toScale);
        this.scaleYBy(toScale);
    }

    public void scaleXBy(float toScale)
    {
        this.x*=toScale;
    }
    public void scaleYBy(float toScale)
    {
        this.y*=toScale;
    }

}
