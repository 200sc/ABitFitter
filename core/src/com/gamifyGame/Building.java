package com.gamifyGame;

import java.util.HashMap;

/**
 * Created by Andrew on 2/16/2015.
 */
public class Building extends Buyable
{
    private int vitalityPer30Seconds;
    private int power;
    private TriggerCondition triggerCondition;
    private float currentMultiplier;

    private boolean replaceable=true;

    public Building(String name, String desc, int cost, int vitalityPerThreeSeconds, int power, TriggerCondition triggerCondition, String key, boolean replaceable) {
        super(key, name, cost, desc);
        this.triggerCondition = triggerCondition;
        this.power=power;
        this.vitalityPer30Seconds =vitalityPerThreeSeconds;
        this.replaceable=replaceable;
        currentMultiplier=1;
    }

    public TriggerCondition getTriggerCondition() {
        return triggerCondition;
    }

    public static HashMap<String, Building> getDefaultBuildings()
    {
        HashMap<String, Building> defList=new HashMap<String, Building>();
        defList.put("Armory", new Building("Armory", "The place where the weapons are", 100, 1, 2, TriggerCondition.RUNNING, "Armory1.png", true));
        defList.put("Computer Room", new Building("Computer Room", "The place where the computers are", 300, 2, 1, TriggerCondition.ACTIVE, "Computer1.png", true));
        defList.put("Costume Closet", new Building("Costume Closet", "A vast wardrobe, filled with costumes.", 300, 3, 2, TriggerCondition.RUNNING, "Costume1.png", true));
        defList.put("Forging Office", new Building("Forging Office", "An office filled with stacks of official seals, brief cases of fake documents and intricate utensils", 4, 16, 1, TriggerCondition.RUNNING, "Forgery1.png", true));
        defList.put("Garage", new Building("Garage", "The place the vehicles are", 900, 4, 3, TriggerCondition.CYCLING, "Garage1.png", true));
        defList.put("Generator", new Building("Generator", "It makes power", 600, 0, 5, TriggerCondition.ACTIVE, "Generator1.png", true));
        defList.put("HQ", new Building("HQ", "You live here", 1000000000, 100, 3, TriggerCondition.ACTIVE, "HQ1.png", false));
        defList.put("Lab", new Building("Lab","The place where you develop new technologies and conduct experiments", 600, 5, 3, TriggerCondition.CYCLING, "Lab1.png", true));
        defList.put("Smuggler's Cove", new Building("Smuggler's Cove", "Your contacts meet you here for suspicious dealings", 600, 6, 3, TriggerCondition.NONE, "Smuggler1.png", true));
        return defList;
    }

    public static HashMap<String, Building> getBuyableBuildings()
    {
        HashMap<String, Building> defBuilds=getDefaultBuildings();
        defBuilds.remove("HQ");
        return defBuilds;
    }

    public static Building getDefaultBuildingByName(String name)
    {
        return getDefaultBuildings().get(name);
    }

    public float getVitalityPer30Seconds()
    {
        return vitalityPer30Seconds*currentMultiplier;
    }

    public boolean isReplaceable() {
        return replaceable;
    }

    public void setCurrentMultiplier(float multiplier)
    {
        this.currentMultiplier=multiplier;
    }

    public void increaseMultiplier(float factor)
    {
        this.currentMultiplier*=factor;
    }

    @Override
    public boolean equals(Object other)
    {
        if(! (other instanceof Building))
        {
            return false;
        }
        else
        {
            Building otherBuild=(Building) other;
            return (this.getName().equals(((Building) other).getName()));
        }
    }
}
