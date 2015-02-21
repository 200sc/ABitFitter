package com.gamifyGame;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.Collection;

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
        this.drawConsumables(Consumable.getAllConsumables().values(), 10, 90);
        for(Consumable currentConsumable: inventory) {
            addBuyListener(currentConsumable);
        }
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

    private void drawConsumables(Collection<Consumable> toDraw, int xPadding, int yLoc)
    {
        float currentX=xPadding;

        for(Consumable currentConsumable: toDraw)
        {
            currentConsumable.addAt(renderHelper.getRenderHelper().getLayer(1),currentX, yLoc);
            currentX+=currentConsumable.getWidth()+xPadding;
        }
    }


    private void addActivateListener(Consumable consumable)
    {
        consumable.clearListeners();
        consumable.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Consumable consumable = (Consumable) event.getListenerActor();
                inventory.remove(consumable);
                active.add(consumable);
                consumable.run();
                consumable.clearListeners();

                //inventory.remove(event.getListenerActor());
                //active.add((Consumable) (event.getListenerActor()));
                return true;
            }
        });
    }

    private void addBuyListener(Consumable consumable)
    {
        consumable.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                Consumable consumable=(Consumable) event.getListenerActor();
                if(game.getVitality()>consumable.getCost()) {
                    inventory.add(consumable);
                    addActivateListener(consumable);
                    game.addToVitality((long) -consumable.getCost());
                }
                return true;
            }
        });
    }


    public ArrayList<Consumable> getActiveConsumables()
    {
        return active;
    }
}
