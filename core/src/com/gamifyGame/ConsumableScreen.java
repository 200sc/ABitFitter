package com.gamifyGame;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Andrew on 2/19/2015.
 */
public class ConsumableScreen extends BuyScreen
{
    private HashMap<Consumable, Integer> inventory;
    private ArrayList<Consumable> active;

    public ConsumableScreen(gamifyGame game)
    {
        super(game);
        inventory =new HashMap<Consumable, Integer>();
        for(Consumable current: Consumable.getAllConsumables().values())
        {
            inventory.put(current, 0);
        }
        active= new ArrayList<Consumable>();
    }

    public void show()
    {
        super.show();
        retBox = renderHelper.getRenderHelper().imageSetupCenter("streakBox.png", renderHelper.getRenderHelper().getLayer(1), -37, 50);
        retBox.addListener(new GoScreenClickListener(game.mainS, game));

        Collection<Consumable> possibleToBuy=Consumable.getAllConsumables().values();
        this.drawPossibleToBuy(possibleToBuy, 50, 10, 260);
        for(Consumable currentConsumable: possibleToBuy)
        {
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



        drawInventory(inventory, 10, 200, 10);
        for(Consumable current: inventory.keySet())
        {
            if(inventory.get(current)>0)
            {
                current.clearListeners();
                addActivateListener(current);
                current.addListener(this.textBoxControlListener());
            }
        }
        for(Consumable current: active)
        {
            current.clearListeners();
        }
    }


    private void drawInventory(HashMap<Consumable, Integer> toDraw, int xLoc, int yBaseLoc, int yPadding) {
        float currentY = yBaseLoc;

        for (Consumable currentConsumable : toDraw.keySet()) {
            currentConsumable.addAt(renderHelper.getRenderHelper().getLayer(1), xLoc, currentY);
            currentY += currentConsumable.getHeight() + yPadding;
            renderHelper.getRenderHelper().batch.begin();
            renderHelper.getRenderHelper().drawTextOnImageNicely(""+toDraw.get(currentConsumable),currentConsumable, 10, 0, GamifyTextSize.MEDIUM, GamifyColor.BLACK);
            renderHelper.getRenderHelper().batch.end();
        }
    }

    private void drawPossibleToBuy(Collection<Consumable> toDraw, int baseX, int xPadding, int yLoc)
    {
        float currentX=baseX;

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
                if(inventory.get(consumable)>0)
                {
                    Consumable activated=consumable.copy();
                    active.add(activated);
                    activated.run();
                    inventory.put(consumable, inventory.get(consumable)-1);
                }
                //consumable.remove();
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
                    inventory.put(newInventory, inventory.get(newInventory)+1);
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
