package com.gamifyGame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Json;
import com.gamifyGame.ChangingImage;
import com.gamifyGame.GamifyScreen;
import com.gamifyGame.gamifyGame;
import com.gamifyGame.renderHelper;

import java.util.EventListener;

/**
 * Created by Stephen on 2/1/2015.
 */
public class BuyScreen extends GamifyScreen implements Screen {
    DragListener dragHandle;


    public BuyScreen(gamifyGame game) {
        super(game);
    }

    @Override
    public void show() {
        //Image itemBar = renderer.imageSetup("ItemBar.png", layer1, 0, 254);
        Image placeHold = renderHelper.getRenderHelper().imageSetup("placeholder128x24.png", renderHelper.getRenderHelper().getLayer(1), 26, 8);
        placeHold.addListener(game.getListener().goScreen(0));


        // Make a new instance of the buildings that is interactable
        Json json = new Json();
        Preferences pref = game.getPrefs();
        String[] underground = json.fromJson(String[].class, pref.getString("undergroundBuildings"));
        Integer[] bridges = json.fromJson(Integer[].class, pref.getString("undergroundBridges"));

        ChangingImage[] undergroundBuild = renderHelper.getRenderHelper().makeUnderground(renderHelper.getRenderHelper().getLayer(1), underground);
        renderHelper.getRenderHelper().makeBridges(renderHelper.getRenderHelper().getLayer(1), bridges);
        game.getListener().buildingListeners(undergroundBuild);


        Image buyBar = renderHelper.getRenderHelper().imageSetup("buyBar.png", renderHelper.getRenderHelper().getLayer(1), 0, 254);

        // TODO: generate this better and make them interactable.
        String[] buyList = {"Armory1.png", "Computer1.png", "Costume1.png", "Forgery1.png",
                "Garage1.png", "Generator1.png", "HQ1.png", "Lab1.png", "Smuggler1.png"};
        Image[] imageHandles = renderHelper.getRenderHelper().makeScroll(renderHelper.getRenderHelper().getLayer(1), buyList, 0, 254);

        //Make the scroll bar actually scroll
        dragHandle = game.getListener().scroll(imageHandles, undergroundBuild, true);
        game.getListener().dragListeners(imageHandles, undergroundBuild);
        buyBar.addListener(dragHandle);
    }

    @Override
    public void hide() {
        // called when current screen changes from this to a different screen
        //renderer.getLayer(1).removeListener(dragHandle);
        renderHelper.getRenderHelper().getLayer(0).clear();
        super.hide();
    }
}
