package com.gamifyGame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

/**
 * Created by Andrew on 2/16/2015.
 */
public class ScrollBar
{
    private gamifyGame game;
    private ArrayList<GamifyImage> images;
    //private ArrayList<GamifyImage> undergroundBuildings;
    private BuyScreen myScreen;
    public static final long PADDING=2;

    public ScrollBar(ArrayList<GamifyImage> images, ArrayList<GamifyImage> undergroundBuildings, gamifyGame game, BuyScreen myScreen)
    {
        this.myScreen=myScreen;
        this.images=images;
        this.game=game;
        //this.undergroundBuildings =undergroundBuildings;

        Image buyBar = renderHelper.getRenderHelper().imageSetup("buyBar.png", renderHelper.getRenderHelper().getLayer(1), 0, 254);

        makeScroll(renderHelper.getRenderHelper().getLayer(1), 0, 254);

        //Make the getDefaultScrollBarListener bar actually getDefaultScrollBarListener
        DragListener dragHandle = getDefaultScrollBarListener( undergroundBuildings, true);
        for(GamifyImage currentImage: images)
        {
            currentImage.addListener(getDefaultScrollBarListener( undergroundBuildings, false));
        }
        buyBar.addListener(dragHandle);
    }
    private DragListener getDefaultScrollBarListener(final ArrayList<GamifyImage> undergroundBuildings, final boolean isLongBar)
    {
        return new DragListener(){
            private float startX, startY, sY;
            private Color startColor;
            private boolean notScroll = false;

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                startX = x; startY = y; sY=event.getListenerActor().getY();
                startColor = new Color(event.getListenerActor().getColor()); //Deep copy
                if(isLongBar == false){
                    //event.getListenerActor().setColor(Color.GREEN);
                    for(GamifyImage current: undergroundBuildings)
                    {
                        if(current instanceof Building) {
                            Building currentBuild = (Building) current;
                            if(currentBuild.isReplaceable())
                                current.setColor(Color.GREEN);
                            else
                                current.setColor(Color.RED);
                        }
                       else
                            current.setColor(Color.GREEN);
                    }
                    Buyable currentEvent=(Buyable) event.getListenerActor();
                    myScreen.setSelectedBuyable(currentEvent);
                }
                //myScreen.getMovingTextDisplayBox().waitThenGradualMoveToPosition(120, 175, 1.5f, 10);
                myScreen.getMovingTextDisplayBox().gradualMoveToPosition(120, 175, 1.5f);
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
               myScreen.getMovingTextDisplayBox().waitThenGradualMoveToPosition(180, 175, 1.5f, 5);
                if(isLongBar == false){
                    Building eventImage = (Building) event.getListenerActor();
                    eventImage.setColor(startColor);
                    for(GamifyImage current: undergroundBuildings)
                        current.setColor(startColor);
                    Integer index = buildCheck(undergroundBuildings, eventImage, game);
                    notScroll =  snapBack(eventImage);
                    if(index !=null )
                    {
                        GamifyImage beingReplaced= undergroundBuildings.remove(index.intValue());
                        undergroundBuildings.add(index,  Building.getDefaultBuildingByName(eventImage.getBuyableName()));
                        undergroundBuildings.get(index).addAt(beingReplaced.getStage(), beingReplaced.getX(), beingReplaced.getY());
                        undergroundBuildings.get(index).toBack();
                        beingReplaced.remove();

                        //Store the change in prefs.
                        Json json = new Json();
                        Preferences pref = game.getPrefs();
                        String[] underground = json.fromJson(String[].class, pref.getString("undergroundBuildings"));
                        underground[index] = eventImage.getBuyableName();
                        pref.putString("undergroundBuildings", json.toJson(underground));
                        pref.flush();
                    }
                }
            }
            public void touchDragged(InputEvent event, float x, float y, int pointer)
            {
                Image eventImage = (Image) event.getListenerActor();
                if(sY-eventImage.getY() > eventImage.getHeight()/3 || notScroll ){
                    notScroll = true;
                    eventImage.setColor(Color.GREEN);
                    eventImage.moveBy(x-startX/2, y-startY);
                }
                else
                {
                    moveScroll((x - startX) / 4, 0);
                    if (isLongBar == false) {eventImage.moveBy(0, y - startY);}
                }
            }};
    }


    private boolean snapBack(GamifyImage toSnap)
    {
        int index=images.lastIndexOf(toSnap);
        float newX;
        GamifyImage neighbor;

        if(images.size()==1)
        {
            toSnap.setPosition(0, 0);
            return false;
        }
        else if(index==0)
        {
            neighbor=images.get(index+1);
            newX=neighbor.getX()-toSnap.getWidth()-ScrollBar.PADDING;
        }
        else
        {
            neighbor=images.get(index-1);
            newX=neighbor.getX()+toSnap.getWidth()+ScrollBar.PADDING;
        }
        toSnap.setPosition(newX, neighbor.getY());
        return false;
    }

    private void makeScroll(Stage stage, int hOrigin, int vOrigin){
        // Gotta have some size so I will for now use the Basic size of the HQ1 but scaled down a teensy bit
        int width  = (int)(.75 *renderHelper.getRenderHelper().textureHash.get("HQ1.png").getWidth());
        int height = (int) (.75* renderHelper.getRenderHelper().textureHash.get("HQ1.png").getHeight());
        int buyBarHeight=renderHelper.getRenderHelper().textureHash.get("buyBar.png").getHeight();

        //Image[] imgHandles = new Image[images.size()];
        for(int i=0; i<=images.size()-1; i++)
        {
            GamifyImage currentImage=images.get(i);
            currentImage.setSize(width, height);
            currentImage.addAt(stage, (int) hOrigin+(i*(width+PADDING)-PADDING), (int) vOrigin + buyBarHeight-(height*16/15));
        }
    }
    private void moveScroll(float xMove, float yMove)
    {
        // If no items make sure not to crash on scrolling
        if(images.isEmpty())
        {
            return;
        }

        // Does not scroll if already at the end of our things to be displayed
        if(xMove > 0 && images.get(0).getX() > 0){return;}
        if(xMove < 0 && images.get(images.size()-1).getX()+images.get(images.size()-1).getWidth() < 180 ){return;}
        //Moves the images

        for(GamifyImage building: images)
        {
            building.moveBy(xMove, yMove);
        }
    }
    private Integer buildCheck(ArrayList<GamifyImage> possibleBuildingSites, Building toBuy, gamifyGame game )
    {
        if(toBuy.getCost()>game.getVitality())
        {
            new PopUpBox(40, 150, 10, "You cannot afford that building");

            this.myScreen.getMovingTextDisplayBox().setColor(Color.BLACK);
            this.myScreen.getMovingTextDisplayBox().addAction(new Action()
            {
                private float remainingTime=1;
                @Override
                public boolean act(float delta)
                {
                    remainingTime-=delta;
                    if(remainingTime<0)
                    {
                        myScreen.getMovingTextDisplayBox().resetColor();
                        return true;
                    }
                    return false;
                }
            });
            return null;
        }

        int foundIndex = -1;
        float minX = toBuy.getX();
        float maxX = toBuy.getRight();
        float minY = toBuy.getY();
        float maxY = toBuy.getTop();

        for(int i=0; i<possibleBuildingSites.size(); i++)
        {
            GamifyImage currentImage=possibleBuildingSites.get(i);
            //TODO: Worry about HQ/other conditions
            if (renderHelper.getRenderHelper().rectangleCollided(minX, maxX, minY, maxY, currentImage.getX(), currentImage.getRight(), currentImage.getY(), currentImage.getTop()))
            {
                boolean success;
                if(currentImage instanceof  Building)
                {
                    Building currentBuilding=(Building) currentImage;
                    success=currentBuilding.isReplaceable();
                }
                else
                {
                    success=true;
                }
                if(success) {
                    game.addToVitality((long) -toBuy.getCost());
                    return i;
                }
                else
                {
                    return null;
                }
            }
        }

        return null;
    }

    public ArrayList<? extends GamifyImage> getImages()
    {
        return images;
    }
}
