package com.gamifyGame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

import java.util.EventListener;

/**
 * Created by Stephen on 2/1/2015.
 */
public class BuyScreen extends GamifyScreen implements Screen
{
    private ClickListener buildingListener;
    private ScrollBar scrollBar;
    private String currentText;

    public BuyScreen(gamifyGame game) {
        super(game);
        buildingListener = new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ChangingImage eventImage = (ChangingImage) event.getListenerActor();
                return true;
            }
        };
        currentText="";
    }

    @Override
    public void show() {
        //Image itemBar = renderer.imageSetup("ItemBar.png", layer1, 0, 254);
        Image placeHold = renderHelper.getRenderHelper().imageSetup("longBox.png", renderHelper.getRenderHelper().getLayer(1), 26, 8);
        placeHold.addListener(game.getListenerHelper().goScreen(0));

        // Make a new instance of the buildings that is interactable
        Json json = new Json();
        Preferences pref = game.getPrefs();
        String[] underground = json.fromJson(String[].class, pref.getString("undergroundBuildings"));
        Integer[] bridges = json.fromJson(Integer[].class, pref.getString("undergroundBridges"));

        ChangingImage[] undergroundBuild = renderHelper.getRenderHelper().makeUnderground(renderHelper.getRenderHelper().getLayer(1), underground);
        renderHelper.getRenderHelper().makeBridges(renderHelper.getRenderHelper().getLayer(1), bridges);
        addBuildingListenerToEachHandle(undergroundBuild);
        scrollBar=new ScrollBar(Building.getDefaultBuildings(), undergroundBuild, game, this);
    }


    @Override
    public void hide() {
        // called when current screen changes from this to a different screen
        //renderer.getLayer(1).removeListener(dragHandle);
        renderHelper.getRenderHelper().getLayer(0).clear();
        super.hide();
    }

    @Override
    public void render(float delta)
    {
        super.render(delta);
        renderHelper.getRenderHelper().getBatch().begin();
        renderHelper.getRenderHelper().textSetCenter(currentText, 45, 65);
        renderHelper.getRenderHelper().imageSetup("midBox.png", renderHelper.getRenderHelper().getLayer(1), 120, 175);
        renderHelper.getRenderHelper().getBatch().end();
    }


    private void addBuildingListenerToEachHandle(ChangingImage[] imageHandles){
        for(int i=0; i <= imageHandles.length-1; i++){
            imageHandles[i].addListener(buildingListener);
        }
    }

    public void setCurrentText(String newText)
    {
       currentText=newText;
    }

}
