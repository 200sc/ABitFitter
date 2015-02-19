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

    LineGraph testGraph;

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
        HashMap<Long,Integer> testData = new HashMap<Long,Integer>();
        testData.put(System.currentTimeMillis()-110000000,40);
        testData.put(System.currentTimeMillis()-19000000,45);
        testData.put(System.currentTimeMillis()-18000000,20);
        testData.put(System.currentTimeMillis()-17000000,68);
        testData.put(System.currentTimeMillis()-16000000,104);
        testData.put(System.currentTimeMillis()-15000000,34);
        testData.put(System.currentTimeMillis()-10000000,40);
        testData.put(System.currentTimeMillis()-9000000,45);
        testData.put(System.currentTimeMillis()-8000000,20);
        testData.put(System.currentTimeMillis()-7000000,68);
        testData.put(System.currentTimeMillis()-6000000,104);
        testData.put(System.currentTimeMillis()-5000000,34);
        testData.put(System.currentTimeMillis()-4000000,78);
        testData.put(System.currentTimeMillis()-1000000,40);
        testData.put(System.currentTimeMillis()-900000,45);
        testData.put(System.currentTimeMillis()-800000,20);
        testData.put(System.currentTimeMillis()-700000,68);
        testData.put(System.currentTimeMillis()-600000,104);
        testData.put(System.currentTimeMillis()-500000,34);
        testData.put(System.currentTimeMillis()-400000,78);
        testData.put(System.currentTimeMillis()-200000,40);
        testData.put(System.currentTimeMillis()-190000,45);
        testData.put(System.currentTimeMillis()-180000,20);
        testData.put(System.currentTimeMillis()-170000,68);
        testData.put(System.currentTimeMillis()-160000,104);
        testData.put(System.currentTimeMillis()-150000,34);
        testData.put(System.currentTimeMillis()-140000,78);
        testData.put(System.currentTimeMillis()-100000,40);
        testData.put(System.currentTimeMillis()-90000,45);
        testData.put(System.currentTimeMillis()-80000,0);
        testData.put(System.currentTimeMillis()-70000,68);
        testData.put(System.currentTimeMillis()-60000,104);
        testData.put(System.currentTimeMillis()-50000,34);
        testData.put(System.currentTimeMillis()-40000,78);
        testGraph = new LineGraph(testData,"Test Data",GamifyColor.BLUE);
        retBox.addListener(game.getListenerHelper().goScreen(0));
    }


}
