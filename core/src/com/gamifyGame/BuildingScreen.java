package com.gamifyGame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

/**
 * Created by Stephen on 2/1/2015.
 */
public class BuildingScreen extends BuyScreen
{
    private ScrollBar scrollBar;

    public BuildingScreen(gamifyGame game) {
        super(game);
        // Make a new instance of the buildings that is interactable
    }

    @Override
    public void show()
    {
        super.show();
        renderHelper.getRenderHelper().getLayer(1).draw();
        renderHelper.getRenderHelper().getLayer(2).draw();

        TextDisplayBox placeHold = new TextDisplayBox("longBox.png");
        float placeHoldX = (renderHelper.getRenderHelper().RENDER_WIDTH - renderHelper.getRenderHelper().textureHash.get("longBox.png").getWidth())/2;
        placeHold.addAt(renderHelper.getRenderHelper().getLayer(1), placeHoldX,12);
        placeHold.addListener(new GoScreenClickListener(game.mainS, game));
        placeHold.addText(new Point(0,0), "Return to Main Screen");

        setUpUnderground();
    }

    private void setUpUnderground()
    {
        Json json = new Json();
        Preferences pref = game.getPrefs();

        Integer[] bridges = json.fromJson(Integer[].class, pref.getString("undergroundBridges"));

        ArrayList<GamifyImage> undergroundBuild = renderHelper.getRenderHelper().makeUnderground(1, game);
        renderHelper.getRenderHelper().makeBridges(renderHelper.getRenderHelper().getLayer(1), bridges);

        scrollBar=new ScrollBar(new ArrayList<GamifyImage>(Building.getBuyableBuildings().values()), undergroundBuild, game, this);
    }


    @Override
    public void hide()
    {
        //renderHelper.getRenderHelper().getLayer(0).clear();
        super.hide();
    }


    public ArrayList<Building> getBuyableBuildings()
    {
        return (ArrayList<Building>) scrollBar.getImages();
    }

}
