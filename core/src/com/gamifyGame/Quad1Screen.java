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

    GamifyGraph[] testGraphs;

    public Quad1Screen(gamifyGame game) {
        super(game);
    }

    public void render(float delta) {
        super.render(delta);
        renderHelper.getRenderHelper().moveCorner(this.retBox, Corner.LOWER_LEFT, 30);

        int i = game.getPrefs().getInteger("currentGraph",0) % 2;
        if (i == -1){i = 1; game.getPrefs().putInteger("currentGraph",1);}

        testGraphs[i].shapeRender();
        renderHelper.getRenderHelper().getBatch().begin();
        testGraphs[i].textRender();
        renderHelper.getRenderHelper().getBatch().end();
    }

    public void show() {
        renderHelper renderer = renderHelper.getRenderHelper();
        retBox = renderer.imageSetupCenter("stepBox.png", renderer.getLayer(1), 37, 50);
        Image leftBox = renderer.imageSetup("arrowBoxLeft.png", renderer.getLayer(1),92,0);
        Image rightBox = renderer.imageSetup("arrowBoxRight.png", renderer.getLayer(1),116,0);

        retBox.addListener(new GoScreenClickListener(game.mainS, game));
        leftBox.addListener(game.getListenerHelper().setInt("currentGraph","--"));
        rightBox.addListener(game.getListenerHelper().setInt("currentGraph","++"));

        testGraphs = new GamifyGraph[2];
        renderer.imageSetup("largeScreenBox.png", renderer.getLayer(1), 36, 42);

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
        testGraphs[0] = new LineGraph(testData,"Test Data",GamifyColor.BLUE);

        HashMap<Integer,Integer> spiderData = new HashMap<Integer,Integer>();
        spiderData.put(0,50);
        spiderData.put(1,22);
        spiderData.put(2,0);
        spiderData.put(3,33);
        spiderData.put(4,46);
        spiderData.put(5,64);
        String[] labels = {"% Time Active", "% Time Excercising", "Vitamin Intake", "% Daily Values Reached", "Unbroken Days Well-Slept", "Unbroken Challenges"};
        testGraphs[1] = new SpiderGraph(spiderData,labels,"Test Data",GamifyColor.YELLOW);
    }


}
