package com.gamifyGame;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Andrew on 2/19/2015.
 */
public class ConsumableScreen extends BuyScreen
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
        super.show();
        retBox = renderHelper.getRenderHelper().imageSetupCenter("streakBox.png", renderHelper.getRenderHelper().getLayer(1), -37, 50);
        retBox.addListener(new GoScreenClickListener(game.mainS, game));

        Collection<Consumable> possibleToBuy=Consumable.getAllConsumables().values();
        this.drawConsumables(possibleToBuy, 10, 90);
        for(Consumable currentConsumable: possibleToBuy) {
            //currentConsumable.clearListeners();
            addBuyListener(currentConsumable);
            currentConsumable.addListener(this.textBoxControlListener());
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




        this.drawConsumables(inventory, 10, 130);
        for(Consumable current: inventory)
        {
            current.clearListeners();
            addActivateListener(current);
            current.addListener(this.textBoxControlListener());
        }
        for(Consumable current: active)
        {
            current.clearListeners();
        }
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

    private DragListener textBoxControlListener()
    {
        return new DragListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                getMovingTextDisplayBox().gradualMoveToPosition(120, 175, 1.5f);
                setSelectedBuyable((Buyable) event.getListenerActor());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                getMovingTextDisplayBox().waitThenGradualMoveToPosition(180, 175, 1.5f, 5);
            }
        };
    }


    private void addActivateListener(Consumable consumable)
    {
        consumable.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Consumable consumable = (Consumable) event.getListenerActor();
                inventory.remove(consumable);
                consumable.remove();
                active.add(consumable);
                consumable.run();
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
                if(game.getVitality()>consumable.getCost())
                {
                    Consumable newInventory=consumable.copy();
                    inventory.add(newInventory);
                    addActivateListener(newInventory);
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
