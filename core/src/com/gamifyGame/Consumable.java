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

    public Consumable(String path, String name, int cost, TriggerCondition triggerCondition, String desc, float multiplier, float lifespan) {
        super(path, name, cost, triggerCondition, desc);
        this.multiplier = multiplier;
        this.lifespan = lifespan;
    }

    public Consumable copy()
    {
        return new Consumable(this.getName(), this.getBuyableName(), this.getCost(), this.getTriggerCondition(), this.getDesc(), this.getMultiplier(), this.getLifespan());
    }

    public float getMultiplier() {
        return multiplier;
    }

    public float getLifespan() {
        return lifespan;
    }

    public void run()
    {
        this.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                lifespan-=delta;
                if(lifespan<=0) {
                    return true;
                }
                return false;
            }
        });
    }

    public static HashMap<String, Consumable> getAllConsumables()
    {
        HashMap<String, Consumable> defConsumables=new HashMap<String, Consumable>();
        defConsumables.put("Consumable1", new Consumable("battery.png", "Battery", 100, TriggerCondition.FOOD, "Desc to come", 1.5f, 60*60));
        defConsumables.put("Consumable2", new Consumable("dollar.png", "Dollar", 100, TriggerCondition.SLEEP, "Desc to come", 3f,  2*60*60));
        defConsumables.put("Consumable3", new Consumable("feather.png", "Feather", 100, TriggerCondition.RUNNING, "Desc to come", 2f,  4*60*60));
        return  defConsumables;
    }

    @Override
    public boolean equals(Object other){
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


}
