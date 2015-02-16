package com.gamifyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Json;
import com.gamifyGame.Corner;
import com.gamifyGame.GamifyScreen;
import com.gamifyGame.gamifyGame;
import com.gamifyGame.renderHelper;


/**
 * Created by Stephen on 2/1/2015.
 */
public class Quad4Screen extends GamifyScreen implements Screen {

    public Quad4Screen(gamifyGame game) {

        super(game);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        renderHelper.getRenderHelper().moveCorner(retBox, Corner.UPPER_LEFT, 30);


        String latestFood = "No Food recorded";
        if(game.getPrefs().getString("latestFood") != null){
           latestFood = game.getPrefs().getString("latestFood");
        }

        Json json = new Json();
        String food = json.fromJson(String.class, latestFood);

        //food = new Json(latestFood);
        //String brand = out.getString("brand_name");

        renderHelper.getRenderHelper().getBatch().begin();
        renderHelper.getRenderHelper().textSet(latestFood, 20, 20);
        renderHelper.getRenderHelper().textSet("HI", 20, 80);
        renderHelper.getRenderHelper().getBatch().end();
    }


    @Override
    public void show() {
        retBox = renderHelper.getRenderHelper().imageSetupCenter("48Box.png", renderHelper.getRenderHelper().getLayer(1), 37, -25);
        retBox.addListener(game.getListener().goScreen(0));
        Image nuBox = renderHelper.getRenderHelper().imageSetup("print_scan.png", renderHelper.getRenderHelper().getLayer(1), 30,30);
        nuBox.setSize(nuBox.getWidth()/4, nuBox.getHeight()/4);
        nuBox.setColor(com.badlogic.gdx.graphics.Color.MAGENTA);
        nuBox.addListener(game.getListener().scanningAction());



    }


}
