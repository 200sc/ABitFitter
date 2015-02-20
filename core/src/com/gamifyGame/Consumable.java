package com.gamifyGame;

/**
 * Created by Andrew on 2/19/2015.
 */
public class Consumable extends GamifyImage
{
    private int multiplier;
    private TriggerCondition condition;
    private int lifespan;

    public Consumable(String path, int multiplier, TriggerCondition condition, int lifespan)
    {
        super(path);
        this.multiplier = multiplier;
        this.condition = condition;
        this.lifespan=lifespan;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public TriggerCondition getCondition() {
        return condition;
    }

    public int getLifespan() {
        return lifespan;
    }


}
