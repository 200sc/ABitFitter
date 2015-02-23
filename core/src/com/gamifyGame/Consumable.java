package com.gamifyGame;

import com.badlogic.gdx.scenes.scene2d.Action;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrew on 2/19/2015.
 */
public class Consumable extends Buyable
{
    private float multiplier;
    private float lifespan;
    private ArrayList<Building> worksWith;

    public Consumable(String path, String name, int cost, String desc, float multiplier, float lifespan, ArrayList<Building> worksWith) {
        super(path, name, cost, desc);
        this.multiplier = multiplier;
        this.lifespan = lifespan;
        this.worksWith=worksWith;
    }

    public Consumable copy()
    {
        return new Consumable(this.getName(), this.getBuyableName(), this.getCost(), this.getDesc(), this.getMultiplier(), this.getLifespan(), worksWith);
    }

    public float getMultiplier() {
        return multiplier;
    }

    public float getLifespan() {
        return lifespan;
    }

    public void run(final Building activatedOn)
    {
        if(worksWith.contains(activatedOn))
        {
            activatedOn.increaseMultiplier(this.getMultiplier());
            this.addAction(new Action() {
                @Override
                public boolean act(float delta) {
                    lifespan-=delta;
                    if(lifespan<=0)
                    {
                        activatedOn.setCurrentMultiplier(1);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    public static HashMap<String, Consumable> getAllConsumables()
    {
        HashMap<String, Consumable> defConsumables=new HashMap<String, Consumable>();
        HashMap<String, Building> allBuildings=Building.getDefaultBuildings();
        ArrayList<Building> worksWith=new ArrayList<Building>();
        worksWith.add(allBuildings.get("HQ"));
        worksWith.add(allBuildings.get("Armory"));
        worksWith.add(allBuildings.get("Garage"));
        worksWith.add(allBuildings.get("Forging Office"));
        defConsumables.put("Dollar", new Consumable("dollar.png", "Dollar", 100,  "Funds your operation", 3f,  2*60*60, worksWith));

        worksWith=new ArrayList<Building>();
        worksWith.add(allBuildings.get("HQ"));
        worksWith.add(allBuildings.get("Garage"));
        worksWith.add(allBuildings.get("Generator"));
        worksWith.add(allBuildings.get("Lab"));

        defConsumables.put("Battery", new Consumable("battery.png", "Battery", 100, "Helps provide power to your buildings and devices", 1.5f, 60*60, worksWith));

        worksWith=new ArrayList<Building>();
        worksWith.add(allBuildings.get("HQ"));
        worksWith.add(allBuildings.get("Costume Closet"));
        worksWith.add(allBuildings.get("Forging Office"));
        worksWith.add(allBuildings.get("Lab"));

        defConsumables.put("Feather", new Consumable("feather.png", "Feather", 100,  "You make clothes and things out of this", 2f,  4*60*60, worksWith));
        return  defConsumables;
    }

    @Override
    public boolean equals(Object other)
    {
        if(other instanceof Consumable)
        {
            return (this.getBuyableName().equals(((Consumable) other).getBuyableName()));
        }
        else
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        return this.getBuyableName().hashCode();
    }

    public String toString()
    {
        String effectiveOnSummary="\nEffective on\n";
        for(Building current: worksWith)
        {
            effectiveOnSummary+=" "+current.getBuyableName()+"\n";
        }
        return this.getBuyableName()+"\n Cost "+ this.getCost()+"\n"+effectiveOnSummary.substring(0, effectiveOnSummary.length());
    }

    public ArrayList<Building> getWorksWith() {
        return worksWith;
    }
}
