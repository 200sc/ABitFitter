package com.gamifyGame;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * Created by Andrew on 2/16/2015.
 */
public class Building
{
    private String desc;
    private String name;
    private int cost;
    private int power;
    private TriggerCondition triggerCondition;
    private String key;

    public Building(String name, String desc, int cost, int power, TriggerCondition triggerCondition, String key) {
        this.desc = desc;
        this.name = name;
        this.cost = cost;
        this.power=power;
        this.triggerCondition=triggerCondition;
        this.key=key;
    }


    public static ArrayList<Building> getDefaultBuildings()
    {
        ArrayList<Building> defList=new ArrayList<Building>(10);
        defList.add(new Building("Armory", "The place where the weapons are", 1, 2, TriggerCondition.FOOD, "Armory1.png"));
        defList.add(new Building("Computer Room", "The place where the computers are", 3, 1, TriggerCondition.SLEEP, "Computer1.png"));
        defList.add(new Building("Costume Closet", "A vast wardrobe, filled with costumes.", 3,2, TriggerCondition.FOOD, "Costume1.png"));
        defList.add(new Building("Forging Office", "An office filled with stacks of offical seals, brief cases of fake documents and intricate utensils for the creation of more.", 4,1, TriggerCondition.SLEEP, "Forgery1.png"));
        defList.add(new Building("Garage", "Where are the cars are", 5, 3, TriggerCondition.RUNNING, "Garage1.png"));
        defList.add(new Building("Generator", "It makes power", 6, 3, TriggerCondition.NONE, "Generator1.png"));
        //defList.add(new Building("HQ", "You live here", 6, 3, TriggerCondition.NONE, "HQ1.png"));
        defList.add(new Building("Lab", "You live here", 6, 3, TriggerCondition.RUNNING, "Lab1.png"));
        defList.add(new Building("Smuggler's Cove", "You live here", 6, 3, TriggerCondition.NONE, "Smuggler1.png"));

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

    public String getImageKey()
    {
        return key;
    }
}
