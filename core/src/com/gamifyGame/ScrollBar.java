package com.gamifyGame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
    private ArrayList<? extends GamifyImage> images;
    private ChangingImage[] underground;

    public ScrollBar(ArrayList<? extends GamifyImage> images, ChangingImage[] undergroundBuild, gamifyGame game)
    {
        this.images=images;
        this.game=game;
        this.underground=undergroundBuild;

        Image buyBar = renderHelper.getRenderHelper().imageSetup("buyBar.png", renderHelper.getRenderHelper().getLayer(1), 0, 254);

        makeScroll(renderHelper.getRenderHelper().getLayer(1), 0, 254);

        //Make the getDefaultScrollBarListener bar actually getDefaultScrollBarListener
        DragListener dragHandle = getDefaultScrollBarListener(images, undergroundBuild, true);
        for(GamifyImage currentImage: images)
        {
            currentImage.addListener(getDefaultScrollBarListener(images, undergroundBuild, false));
        }
        buyBar.addListener(dragHandle);
    }
    private DragListener getDefaultScrollBarListener(final ArrayList<? extends GamifyImage> imgHandles, final ChangingImage[] underground, final boolean isLongBar)
    {
        return new DragListener(){
            private float startX, startY;
            private Color startColor;
            private boolean notScroll = false;

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                startX = x; startY = y;
                startColor = new Color(event.getListenerActor().getColor()); //Deep copy
                if(isLongBar == false){
                    //event.getListenerActor().setColor(Color.GREEN);
                    for(int i=0; i <underground.length; i++){underground[i].setColor(Color.GREEN);}
                }
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                if(isLongBar == false){
                    Building eventImage = (Building) event.getListenerActor();
                    eventImage.setColor(startColor);
                    for(int i=0; i <underground.length; i++){underground[i].setColor(startColor);}
                    int index = renderHelper.getRenderHelper().buildCheck(underground,eventImage);
                    snapBack(eventImage);
                    if(index != -1){
                        //Store the change in prefs.
                        Json json = new Json();
                        Preferences pref = game.getPrefs();
                        String[] underground = json.fromJson(String[].class, pref.getString("undergroundBuildings"));
                        underground[index] = eventImage.getName();
                        pref.putString("undergroundBuildings", json.toJson(underground));
                        pref.flush();
                    }
                }
            }
            public void touchDragged(InputEvent event, float x, float y, int pointer)
            {
                Image eventImage = (Image) event.getListenerActor();
                if(startY-eventImage.getY() > eventImage.getHeight()/3 || notScroll ){
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


    public void snapBack(GamifyImage toSnap)
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

    public void makeScroll(Stage stage, int hOrigin, int vOrigin){
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

       /*for(int i=0; i <= images.length-1; i++){
            imgHandles[i] = imageSetup(images[i], stage, hOrigin+(i*width),vOrigin + textureHash.get("buyBar.png").getHeight()-height);
            imgHandles[i].setSize(width, height);
        }*/
    }
    public void moveScroll(float xMove, float yMove)
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
}