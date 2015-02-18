package com.gamifyGame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
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
                        current.setColor(Color.GREEN);
                    Building currentEvent=(Building) event.getListenerActor();
                    myScreen.setCurrentText(currentEvent.toString());
                }
                myScreen.getTextDisplayBox().gradualMoveToPosition(120, 175, 1.5f);
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                myScreen.getTextDisplayBox().waitThenGradualMoveToPosition(180, 175, 1.5f, 10);
                if(isLongBar == false){
                    Building eventImage = (Building) event.getListenerActor();
                    eventImage.setColor(startColor);
                    for(GamifyImage current: undergroundBuildings)
                        current.setColor(startColor);
                    Integer index = renderHelper.getRenderHelper().buildCheck(undergroundBuildings,eventImage, game);
                    snapBack(eventImage);
                    if(index !=null )
                    {
                        GamifyImage beingReplaced= undergroundBuildings.remove(index.intValue());
                        undergroundBuildings.add(index,  Building.getDefaultBuildingByName(eventImage.getBuildingName()));
                        undergroundBuildings.get(index).addAt(beingReplaced.getStage(), beingReplaced.getX(), beingReplaced.getY());
                        undergroundBuildings.get(index).toBack();
                        beingReplaced.remove();

                        //Store the change in prefs.
                        Json json = new Json();
                        Preferences pref = game.getPrefs();
                        String[] underground = json.fromJson(String[].class, pref.getString("undergroundBuildings"));
                        underground[index] = eventImage.getBuildingName();
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
                    eventImage.setColor(Color.RED);
                    eventImage.moveBy(x-startX/2, y-startY);
                }
                else
                {
                    moveScroll((x - startX) / 4, 0);
                    if (isLongBar == false) {eventImage.moveBy(0, y - startY);}
                }
            }};
    }


    private void snapBack(GamifyImage toSnap)
    {
        int index=images.lastIndexOf(toSnap);
        float newX;
        GamifyImage neighbor;

        if(images.size()==1)
        {
            toSnap.setPosition(0, 0);
            return;
        }
        else if(index==0)
        {
            neighbor=images.get(index+1);
            newX=neighbor.getX()-toSnap.getWidth();
        }
        else
        {
            neighbor=images.get(index-1);
            newX=neighbor.getX()+toSnap.getWidth();
        }
        toSnap.setPosition(newX, neighbor.getY());
    }

    private void makeScroll(Stage stage, int hOrigin, int vOrigin){
        // Gotta have some size so I will for now use the Basic size of the HQ1 but scaled down a teensy bit
        int width  = (int)(.8 *renderHelper.getRenderHelper().textureHash.get("HQ1.png").getWidth());
        int height = (int) (.8* renderHelper.getRenderHelper().textureHash.get("HQ1.png").getHeight());
        int buyBarHeight=renderHelper.getRenderHelper().textureHash.get("buyBar.png").getHeight();

        //Image[] imgHandles = new Image[images.size()];
        for(int i=0; i<=images.size()-1; i++)
        {
            GamifyImage currentImage=images.get(i);
            currentImage.setSize(width, height);
            currentImage.addAt(stage, (int) hOrigin+(i*width), (int) vOrigin + buyBarHeight-height);
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

    public ArrayList<? extends GamifyImage> getImages()
    {
        return images;
    }
}
