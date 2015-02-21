package com.gamifyGame;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;

/**
 * Created by Andrew on 2/19/2015.
 */
public class ConsumableScreen extends GamifyScreen
{
    private ArrayList<Consumable> inventory;
    private ArrayList<Consumable> active;

    public ConsumableScreen(gamifyGame game)
    {
        super(game);
        inventory =new ArrayList<Consumable>();
        active= new ArrayList<Consumable>();
    }

    public void show()
    {

        retBox = renderHelper.getRenderHelper().imageSetupCenter("streakBox.png", renderHelper.getRenderHelper().getLayer(1), -37, 50);
        retBox.addListener(new GoScreenClickListener(game.mainS, game));
    }
    @Override
    public void render(float delta)
    {
        super.render(delta);
        renderHelper.getRenderHelper().moveCorner(retBox, Corner.LOWER_RIGHT, 31);
        ArrayList<Consumable> toRemove= new ArrayList<Consumable>();
        for(Consumable currentConsumable: active)
        {
            if(currentConsumable.getLifespan()<=0)
            {
                toRemove.add(currentConsumable);
            }
        }
        active.removeAll(toRemove);
    }


    public ArrayList<Consumable> getActiveConsumables()
    {
        return active;
    }
}
