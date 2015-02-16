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
import com.gamifyGame.Corner;
import com.gamifyGame.GamifyScreen;
import com.gamifyGame.gamifyGame;
import com.gamifyGame.renderHelper;

import java.util.HashMap;

/**
 * Created by Stephen on 2/1/2015.
 */
public class Quad1Screen extends GamifyScreen implements Screen {

    HistogramGraph testGraph;

    public Quad1Screen(gamifyGame game) {
        super(game);
    }

    public void render(float delta) {
        super.render(delta);
        renderHelper.getRenderHelper().moveCorner(this.retBox, Corner.LOWER_LEFT, 30);

        testGraph.shapeRender();

        renderHelper.getRenderHelper().getBatch().begin();
        testGraph.textRender();
        renderHelper.getRenderHelper().getBatch().end();
    }

    public void show() {
        retBox = renderHelper.getRenderHelper().imageSetupCenter("stepBox.png", renderHelper.getRenderHelper().getLayer(1), 37, 50);
        retBox.addListener(game.getListener().goScreen(0));
        HashMap<Long,Integer> testData = new HashMap<Long,Integer>();
        testData.put(System.currentTimeMillis()-100000,40);
        testData.put(System.currentTimeMillis()-90000,45);
        testData.put(System.currentTimeMillis()-80000,20);
        testData.put(System.currentTimeMillis()-70000,68);
        testData.put(System.currentTimeMillis()-60000,104);
        testData.put(System.currentTimeMillis()-50000,34);
        testData.put(System.currentTimeMillis()-40000,78);
        testGraph = new HistogramGraph(testData,"Test Data",GamifyColor.BLUE,"large");
    }


}
