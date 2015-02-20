package com.gamifyGame;

import com.badlogic.gdx.Screen;

import java.util.HashMap;

/**
 * Created by Stephen on 2/1/2015.
 */
public class Quad2Screen extends GamifyScreen implements Screen {

    SpiderGraph testGraph;

    public Quad2Screen(gamifyGame game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        renderHelper.getRenderHelper().moveCorner(retBox, Corner.LOWER_RIGHT, 31);

        testGraph.shapeRender();
        renderHelper.getRenderHelper().getBatch().begin();
        testGraph.textRender();
        renderHelper.getRenderHelper().getBatch().end();
    }

    @Override
    public void show() {
        retBox = renderHelper.getRenderHelper().imageSetupCenter("streakBox.png", renderHelper.getRenderHelper().getLayer(1), -37, 50);
        retBox.addListener(game.getListenerHelper().goScreen(0));
        HashMap<Integer,Integer> testData = new HashMap<Integer,Integer>();
        testData.put(0,50);
        testData.put(1,22);
        testData.put(2,0);
        testData.put(3,33);
        testData.put(4,46);
        testData.put(5,64);
        String[] labels = {"% Time Active", "% Time Excercising", "Vitamin Intake", "% Daily Values Reached", "Unbroken Days Well-Slept", "Unbroken Challenges"};
        testGraph = new SpiderGraph(testData,labels,"Test Data",GamifyColor.YELLOW);
    }
}
