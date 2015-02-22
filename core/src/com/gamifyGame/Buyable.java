package com.gamifyGame;

/**
 * Created by Andrew on 2/20/2015.
 */
public class Buyable extends GamifyImage
{
    private String desc;
    private String name;
    private int cost;

    public Buyable(String path, String name, int cost, String desc ) {
        super(path);

        this.desc = desc;
        this.name = name;
        this.cost = cost;
    }



    public String getDesc() {
        return desc;
    }

    public String getBuyableName() {
        return name;
    }

    public int getCost() {
        return cost;
    }
    public String toString()
    {
        return this.getBuyableName()+"\n Cost: "+ this.getCost()+"\n"+ this.getDesc();
    }
}
