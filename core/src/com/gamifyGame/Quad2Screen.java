package com.gamifyGame;

import com.badlogic.gdx.Screen;

/**
 * Created by Stephen on 2/1/2015.
 */
public class Quad2Screen extends GamifyScreen implements Screen {

    public Quad2Screen(gamifyGame game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        renderHelper.getRenderHelper().moveCorner(retBox, Corner.LOWER_RIGHT, 31);
    }

    @Override
    public void show() {
        retBox = renderHelper.getRenderHelper().imageSetupCenter("streakBox.png", renderHelper.getRenderHelper().getLayer(1), -37, 50);
        retBox.addListener(game.getListenerHelper().goScreen(0));
    }
}
