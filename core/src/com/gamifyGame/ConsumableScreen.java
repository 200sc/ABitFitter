package com.gamifyGame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Andrew on 2/19/2015.
 */
public class ConsumableScreen extends BuyScreen {
    private HashMap<Consumable, Integer> inventory;
    private HashMap<Consumable, Integer> capacity;

    public ConsumableScreen(gamifyGame game) {
        super(game);
        inventory = new HashMap<Consumable, Integer>();
        capacity = new HashMap<Consumable, Integer>();
        for (Consumable current : Consumable.getAllConsumables().values()) {
            inventory.put(current, 0);
            capacity.put(current, 1);
        }
    }

    public void show() {
        super.show();
        retBox = renderHelper.getRenderHelper().imageSetupCenter("streakBox.png", renderHelper.getRenderHelper().getLayer(1), -37, 50);
        retBox.addListener(new GoScreenClickListener(game.mainS, game));
        retBox.setZIndex(100);

        Collection<Consumable> possibleToBuy = Consumable.getAllConsumables().values();
        this.drawPossibleToBuy(possibleToBuy, 50, 10, 260);
        for (Consumable currentConsumable : possibleToBuy) {
            addBuyListener(currentConsumable);
            currentConsumable.addListener(this.textBoxControlListener());
        }
        ArrayList<GamifyImage> result = renderHelper.getRenderHelper().makeUnderground(1, game);
        for (Consumable current : inventory.keySet())
        {
            if (inventory.get(current) > 0) {
                current.clearListeners();

            }//addActivateListener(current);
            current.addListener(getDefaultScrollBarListener(result));
            //current.addListener(this.textBoxControlListener());
        }
        drawInventory(inventory, 10, 200, 10);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        renderHelper.getRenderHelper().moveCorner(retBox, Corner.LOWER_RIGHT, 31);
        for(Consumable currentConsumable : inventory.keySet())
        {
            renderHelper.getRenderHelper().batch.begin();
            renderHelper.getRenderHelper().drawTextOnImageNicely("" + inventory.get(currentConsumable), currentConsumable, 10, 0, GamifyTextSize.MEDIUM, GamifyColor.BLACK, "left");
            renderHelper.getRenderHelper().batch.end();
        }
    }


    private void drawInventory(HashMap<Consumable, Integer> toDraw, int xLoc, int yBaseLoc, int yPadding) {
        float currentY = yBaseLoc;

        for (Consumable currentConsumable : toDraw.keySet()) {
            currentConsumable.addAt(renderHelper.getRenderHelper().getLayer(1), xLoc, currentY);
            currentY += currentConsumable.getHeight() + yPadding;

        }
    }

    private void drawPossibleToBuy(Collection<Consumable> toDraw, int baseX, int xPadding, int yLoc) {
        float currentX = baseX;

        for (Consumable currentConsumable : toDraw) {
            currentConsumable.addAt(renderHelper.getRenderHelper().getLayer(1), currentX, yLoc);
            currentX += currentConsumable.getWidth() + xPadding;
        }
    }

    private DragListener textBoxControlListener() {
        return new DragListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getMovingTextDisplayBox().gradualMoveToPosition(120, 175, 1.5f);
                setSelectedBuyable((Buyable) event.getListenerActor());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getMovingTextDisplayBox().waitThenGradualMoveToPosition(180, 175, 1.5f, 5);
            }
        };
    }


    /*private void addActivateListener(Consumable consumable) {
        consumable.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

            }
        });
    }*/

    private int converTypeNumber(Consumable consumable) {
        if (consumable.getBuyableName().equals("Battery")) {
            return 0;
        } else if (consumable.getBuyableName().equals("Dollar")) {
            return 1;
        } else {
            return 2;
        }
    }

    private void addBuyListener(Consumable consumable) {
        consumable.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Consumable consumable = (Consumable) event.getListenerActor();
                if (game.getVitality() > consumable.getCost()) {
                    Consumable newInventory = consumable.copy();
                    inventory.put(newInventory, inventory.get(newInventory) + 1);
                    game.addToVitality((long) -consumable.getCost());
                }
                return true;
            }
        });
    }

    private DragListener getDefaultScrollBarListener(final ArrayList<GamifyImage> undergroundBuildings) {
        return new DragListener() {
            private float startX,startY,sY;
            private Color startColor;
            private boolean notScroll = false;

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                startX = x;
                startY = y;
                sY = event.getListenerActor().getY();
                startColor = new Color(event.getListenerActor().getColor()); //Deep copy
                for (GamifyImage current : undergroundBuildings) {
                    current.setColor(Color.GREEN);
                }
                Buyable currentEvent = (Buyable) event.getListenerActor();
                setSelectedBuyable(currentEvent);
                getMovingTextDisplayBox().gradualMoveToPosition(120, 175, 1.5f);
                return true;

            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                getMovingTextDisplayBox().waitThenGradualMoveToPosition(180, 175, 1.5f, 5);
                Consumable eventImage = (Consumable) event.getListenerActor();
                eventImage.setColor(startColor);
                for (GamifyImage current : undergroundBuildings)
                    current.setColor(startColor);
               buildCheck(undergroundBuildings, eventImage, game);
               drawInventory(inventory, 10, 200, 10);
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                Image eventImage = (Image) event.getListenerActor();
                eventImage.setColor(Color.GREEN);
                eventImage.moveBy(x - startX, y - startY);

            }
        };
    }

    private void buildCheck(ArrayList<GamifyImage> possibleBuildingSites, Consumable toBuy, gamifyGame game )
    {
        if(toBuy.getCost()>game.getVitality())
        {
            getMovingTextDisplayBox().setColor(Color.BLACK);
            getMovingTextDisplayBox().addAction(new Action()
            {
                private float remainingTime=1;
                @Override
                public boolean act(float delta)
                {
                    remainingTime-=delta;
                    if(remainingTime<0)
                    {
                        getMovingTextDisplayBox().resetColor();
                        return true;
                    }
                    return false;
                }
            });
        }

        int foundIndex = -1;
        float minX = toBuy.getX();
        float maxX = toBuy.getRight();
        float minY = toBuy.getY();
        float maxY = toBuy.getTop();

        for(int i=0; i<possibleBuildingSites.size(); i++)
        {
            GamifyImage currentImage=possibleBuildingSites.get(i);
            //TODO: Worry about other conditions
            if (renderHelper.getRenderHelper().rectangleCollided(minX, maxX, minY, maxY, currentImage.getX(), currentImage.getRight(), currentImage.getY(), currentImage.getTop())) {
                boolean success;
                if (currentImage instanceof Building && inventory.get(toBuy) > 0)
                {
                    Building currentBuilding = (Building) currentImage;
                    Consumable activated = toBuy.copy();
                    Json json = new Json();
                    String result = game.getPrefs().getString("consumables", null);
                    float[][] consumables = new float[][]{
                            {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
                    if (result != null) {
                        consumables = json.fromJson(float[][].class, result);
                    }

                    consumables[i][converTypeNumber(toBuy)] = consumables[i][converTypeNumber(toBuy)] + toBuy.getLifespan();
                    game.getPrefs().putString("consumables", json.toJson(consumables));
                    game.getPrefs().flush();

                    inventory.put(toBuy, inventory.get(toBuy) - 1);
                    return;
                }
                return;
            }
        }

        return;
    }
}

