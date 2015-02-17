package com.gamifyGame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

/**
 * Created by Stephen on 2/1/2015.
 */
public class BuyScreen extends GamifyScreen implements Screen
{
    private DragListener dragHandle;
    private ClickListener buildingListener;
    private ArrayList<Building> buyableBuildings;

    public BuyScreen(gamifyGame game) {
        super(game);
        buildingListener = new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ChangingImage eventImage = (ChangingImage) event.getListenerActor();
                return true;
            }
        };
        buyableBuildings=new ArrayList<Building>();
        buyableBuildings=Building.getDefaultBuildings();
    }

    @Override
    public void show() {
        //Image itemBar = renderer.imageSetup("ItemBar.png", layer1, 0, 254);
        Image placeHold = renderHelper.getRenderHelper().imageSetup("placeholder128x24.png", renderHelper.getRenderHelper().getLayer(1), 26, 8);
        placeHold.addListener(game.getListenerHelper().goScreen(0));

        // Make a new instance of the buildings that is interactable
        Json json = new Json();
        Preferences pref = game.getPrefs();
        String[] underground = json.fromJson(String[].class, pref.getString("undergroundBuildings"));
        Integer[] bridges = json.fromJson(Integer[].class, pref.getString("undergroundBridges"));

        ChangingImage[] undergroundBuild = renderHelper.getRenderHelper().makeUnderground(renderHelper.getRenderHelper().getLayer(1), underground);
        renderHelper.getRenderHelper().makeBridges(renderHelper.getRenderHelper().getLayer(1), bridges);
        addBuildingListenerToEachHandle(undergroundBuild);


        Image buyBar = renderHelper.getRenderHelper().imageSetup("buyBar.png", renderHelper.getRenderHelper().getLayer(1), 0, 254);

        // TODO: generate this better and make them interactable.
        String[] keys=new String[buyableBuildings.size()];
        int i=0;
        for(Building current: buyableBuildings)
        {
            keys[i]=current.getImageKey();
            i++;
        }
        Image[] imageHandles = renderHelper.getRenderHelper().makeScroll(renderHelper.getRenderHelper().getLayer(1), keys, 0, 254);

        //Make the getDefaultScrollBarListener bar actually getDefaultScrollBarListener
        dragHandle = getDefaultScrollBarListener(imageHandles, undergroundBuild, true);
        dragListeners(imageHandles, undergroundBuild);
        buyBar.addListener(dragHandle);
    }


    @Override
    public void hide() {
        // called when current screen changes from this to a different screen
        //renderer.getLayer(1).removeListener(dragHandle);
        renderHelper.getRenderHelper().getLayer(0).clear();
        super.hide();
    }

    private DragListener getDefaultScrollBarListener(final Image[] imgHandles, final ChangingImage[] underground, final boolean isLongBar)
    {
        return new DragListener(){
            private float startX, startY, sY, sX;
            private Color startColor;
            private boolean notScroll = false;
            private boolean isDown = false;
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(isDown){return true;}
                isDown = true;
                startX = x; startY = y; sY = event.getListenerActor().getY();
                //sX = event.getListenerActor().getX();
                startColor = new Color(event.getListenerActor().getColor()); //Deep copy
                if(isLongBar == false){
                    //event.getListenerActor().setColor(Color.GREEN);
                    for(int i=0; i <underground.length; i++){underground[i].setColor(Color.GREEN);}
                }
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                Image eventImage = (Image) event.getListenerActor();
                if(isLongBar == false){
                    eventImage.setColor(startColor);
                    for(int i=0; i <underground.length; i++){underground[i].setColor(startColor);}
                    int index = renderHelper.getRenderHelper().buildCheck(underground,eventImage);
                    eventImage.moveBy(sX-eventImage.getX(), sY-eventImage.getY());
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
                isDown = false;
            }
            public void touchDragged(InputEvent event, float x, float y, int pointer)
            {
                Image eventImage = (Image) event.getListenerActor();
                if(sY-eventImage.getY() > eventImage.getHeight()/3 || notScroll ){
                    notScroll = true;
                    eventImage.setColor(Color.RED);
                    eventImage.moveBy(x-startX/2, y-startY);
                }
                else {
                    sX = eventImage.getX();
                    renderHelper.getRenderHelper().moveScroll(imgHandles, (x - startX) / 2, 0);
                    if (isLongBar == false) {eventImage.moveBy(0, y - startY);}
                }
            }};
    }

    private void dragListeners(Image[] imageHandles, ChangingImage[] underground){
        for(int i=0; i <= imageHandles.length-1; i++){
            imageHandles[i].addListener(getDefaultScrollBarListener(imageHandles, underground, false));
        }
    }
    private void addBuildingListenerToEachHandle(ChangingImage[] imageHandles){
        for(int i=0; i <= imageHandles.length-1; i++){
            imageHandles[i].addListener(buildingListener);
        }
    }
}
