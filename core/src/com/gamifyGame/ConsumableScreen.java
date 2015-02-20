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
        //Image itemBar = renderer.imageSetup("ItemBar.png", layer1, 0, 254);
        Image placeHold = renderHelper.getRenderHelper().imageSetup("longBox.png", renderHelper.getRenderHelper().getLayer(1), 26, 8);
        placeHold.addListener(game.getListenerHelper().goScreen(0));
    }
    @Override
    public void render(float delta)
    {
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
