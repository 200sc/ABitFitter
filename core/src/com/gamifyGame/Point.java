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

    @Override
    public boolean equals(Object other)
    {
        if(! (other instanceof Point))
        {
            return false;
        }
        else
        {
            Point otherPoint=(Point) other;
            return (this.x == otherPoint.x && this.y == otherPoint.y);
        }
    }
    @Override
    public int hashCode()
    {
        return (new Float(x).hashCode()*13)+new Float(y).hashCode();
    }

}
