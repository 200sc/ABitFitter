package com.gamifyGame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

/**
 * Created by Stephen on 2/1/2015.
 */
public class BuyScreen extends GamifyScreen implements Screen
{
    private ClickListener buildingListener;
    private ScrollBar scrollBar;
    private Building selectedBuilding;
    private MovingTextDisplayBox textDisplayBox;

    public BuyScreen(gamifyGame game) {
        super(game);
        buildingListener = new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                GamifyImage eventImage = (GamifyImage) event.getListenerActor();
                return true;
            }
        };
        selectedBuilding =null;
        // Make a new instance of the buildings that is interactable
    }

    @Override
    public void show() {
        //Image itemBar = renderer.imageSetup("ItemBar.png", layer1, 0, 254);

        TextDisplayBox placeHold = new TextDisplayBox("longBox.png");
        float placeHoldX = (renderHelper.getRenderHelper().RENDERED_SCREEN_WIDTH - renderHelper.getRenderHelper().textureHash.get("longBox.png").getWidth())/2;
        placeHold.addAt(renderHelper.getRenderHelper().getLayer(1), placeHoldX,12);
        placeHold.addListener(game.getListenerHelper().goScreen(0));
        placeHold.addText(new Point(0,0), "Return to Main Screen");


        textDisplayBox=new MovingTextDisplayBox("midBox.png");
        textDisplayBox.addAt(renderHelper.getRenderHelper().getLayer(1), 180, 175);

        setUpUnderground();
    }

    private void setUpUnderground()
    {
        Json json = new Json();
        Preferences pref = game.getPrefs();
        String[] underground = json.fromJson(String[].class, pref.getString("undergroundBuildings"));
        Integer[] bridges = json.fromJson(Integer[].class, pref.getString("undergroundBridges"));

        ArrayList<GamifyImage> undergroundBuild = renderHelper.getRenderHelper().makeUnderground(renderHelper.getRenderHelper().getLayer(1), underground);
        renderHelper.getRenderHelper().makeBridges(renderHelper.getRenderHelper().getLayer(1), bridges);

        for(GamifyImage currentGamifyImage: undergroundBuild){
            currentGamifyImage.addListener(buildingListener);
        }

        scrollBar=new ScrollBar(new ArrayList<GamifyImage>(Building.getBuyableBuildings().values()), undergroundBuild, game, this);


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
        //renderHelper.getRenderHelper().textSetCenter(currentText, 45, 65);
        //renderHelper.getRenderHelper().textSetCenter("Vitality: "+game.getVitality(), 35, 75);



        //renderHelper.getRenderHelper().imageSetup("midBox.png", renderHelper.getRenderHelper().getLayer(1), 120, 175);
        renderHelper.getRenderHelper().getBatch().end();

        if(selectedBuilding !=null)
            textDisplayBox.addText(new Point(0, 30), selectedBuilding.toString());
        textDisplayBox.addText(new Point(0, -15),"Vitality: "+game.getVitality( ));

    }

    public MovingTextDisplayBox getMovingTextDisplayBox()
    {
        return textDisplayBox;
    }


    public void setSelectedBuilding(Building newBuilding)
    {
       selectedBuilding =newBuilding;
    }

    public ArrayList<Building> myBuildings()
    {
        return (ArrayList<Building>) scrollBar.getImages();
    }

}
