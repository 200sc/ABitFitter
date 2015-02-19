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
    }

    @Override
    public void show() {
        retBox = renderHelper.getRenderHelper().imageSetupCenter("streakBox.png", renderHelper.getRenderHelper().getLayer(1), -37, 50);
        retBox.addListener(game.getListenerHelper().goScreen(0));
        HashMap<Integer,Integer> testData = new HashMap<Integer,Integer>();
        testData.put(1,50);
        testData.put(2,22);
        testData.put(3,0);
        testData.put(4,33);
        testData.put(5,46);
        testData.put(6,64);
        testGraph = new SpiderGraph(testData,"Test Data",GamifyColor.BLUE);
    }
}
