package com.gamifyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

import javax.xml.soap.Text;

/**
 * Created by Stephen on 2/1/2015.
 */
public class BuyScreen extends GamifyScreen implements Screen
{
    private ClickListener buildingListener;
    private ScrollBar scrollBar;
    private String currentText;
    private TextDisplayBox textDisplayBox;

    public BuyScreen(gamifyGame game) {
        super(game);
        buildingListener = new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                GamifyImage eventImage = (GamifyImage) event.getListenerActor();
                return true;
            }
        };
        currentText="";
        // Make a new instance of the buildings that is interactable
    }

    @Override
    public void show() {
        //Image itemBar = renderer.imageSetup("ItemBar.png", layer1, 0, 254);
        Image placeHold = renderHelper.getRenderHelper().imageSetup("longBox.png", renderHelper.getRenderHelper().getLayer(1), 26, 8);
        placeHold.addListener(game.getListenerHelper().goScreen(0));


        textDisplayBox=new TextDisplayBox();
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

        scrollBar=new ScrollBar(new ArrayList<GamifyImage>(Building.getDefaultBuildings().values()), undergroundBuild, game, this);


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


        textDisplayBox.addText(new Point(-30, 10), currentText);
        textDisplayBox.addText(new Point(-20, -15),"Vitality: "+game.getVitality( ));
    }

    public TextDisplayBox getTextDisplayBox()
    {
        return textDisplayBox;
    }


    public void setCurrentText(String newText)
    {
       currentText=newText;
    }

    public ArrayList<Building> myBuildings()
    {
        return (ArrayList<Building>) scrollBar.getImages();
    }

}
