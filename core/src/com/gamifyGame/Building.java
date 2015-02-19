package com.gamifyGame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrew on 2/16/2015.
 */
public class Building extends GamifyImage
{
    private String desc;
    private String name;
    private int cost;
    private int vitalityPerThreeSeconds;
    private int power;
    private TriggerCondition triggerCondition;

    public Building(String name, String desc, int cost, int vitalityPerThreeSeconds, int power, TriggerCondition triggerCondition, String key) {
        super(key);
        this.desc = desc;
        this.name = name;
        this.cost = cost;
        this.power=power;
        this.vitalityPerThreeSeconds=vitalityPerThreeSeconds;
        this.triggerCondition=triggerCondition;
    }


    public static HashMap<String, Building> getDefaultBuildings()
    {
        HashMap<String, Building> defList=new HashMap<String, Building>();
        defList.put("Armory", new Building("Armory", "The place where the weapons are", 100, 1, 2, TriggerCondition.FOOD, "Armory1.png"));
        defList.put("Computer Room", new Building("Computer Room", "The place where the computers are", 300, 2, 1, TriggerCondition.SLEEP, "Computer1.png"));
        defList.put("Costume Closet", new Building("Costume Closet", "A vast wardrobe, filled with costumes.", 300, 3, 2, TriggerCondition.FOOD, "Costume1.png"));
        defList.put("Forging Office", new Building("Forging Office", "An office filled with stacks of offical seals, brief cases of fake documents and intricate utensils for the creation of more.", 4, 16, 1, TriggerCondition.SLEEP, "Forgery1.png"));
        defList.put("Garage", new Building("Garage", "Where are the cars are", 500, 4, 3, TriggerCondition.RUNNING, "Garage1.png"));
        defList.put("Generator", new Building("Generator", "It makes power", 600, 0, 5, TriggerCondition.NONE, "Generator1.png"));
        defList.put("HQ", new Building("HQ", "You live here", 1000000000, 100, 3, TriggerCondition.NONE, "HQ1.png"));
        defList.put("Lab", new Building("Lab", "You live here", 600, 5, 3, TriggerCondition.RUNNING, "Lab1.png"));
        defList.put("Smuggler's Cove", new Building("Smuggler's Cove", "You live here", 600, 6, 3, TriggerCondition.NONE, "Smuggler1.png"));

        /*textureHash.put("Armory1.png",imageLoad("Armory1.png"));
        textureHash.put("Computer1.png",imageLoad("Computer1.png"));
        textureHash.put("Costume1.png",imageLoad("Costume1.png"));
        textureHash.put("Elevator1.png",imageLoad("Elevator1.png"));
        textureHash.put("Bridge1.png",imageLoad("Bridge1.png"));
        textureHash.put("Empty1.png",imageLoad("Empty1.png"));
        textureHash.put("Forgery1.png",imageLoad("Forgery1.png"));
        textureHash.put("Garage1.png",imageLoad("Garage1.png"));
        textureHash.put("Generator1.png",imageLoad("Generator1.png"));
        textureHash.put("HQ1.png",imageLoad("HQ1.png"));
        textureHash.put("Lab1.png",imageLoad("Lab1.png"));
        textureHash.put("Smuggler1.png",imageLoad("Smuggler1.png"));*/
        return defList;
    }
    public static Building getDefaultBuildingByName(String name)
    {
        return getDefaultBuildings().get(name);
    }

    public String toString()
    {
        return desc;
    }

    public int getCost() {return cost;}

    public int getVitalityPerThreeSeconds()
    {
        return vitalityPerThreeSeconds;
    }

    public String getBuildingName()
    {
        return name;
    }



}
