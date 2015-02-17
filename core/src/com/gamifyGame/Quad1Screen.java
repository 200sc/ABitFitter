package com.gamifyGame;

import com.badlogic.gdx.Screen;

/**
 * Created by Stephen on 2/1/2015.
 */
public class Quad1Screen extends GamifyScreen implements Screen {

    public Quad1Screen(gamifyGame game) {
        super(game);
    }

    public void render(float delta) {
        super.render(delta);
        renderHelper.getRenderHelper().moveCorner(this.retBox, Corner.LOWER_LEFT, 30);
    }

    public void show() {
        retBox = renderHelper.getRenderHelper().imageSetupCenter("stepBox.png", renderHelper.getRenderHelper().getLayer(1), 37, 50);
        retBox.addListener(game.getListenerHelper().goScreen(0));
    }


}
