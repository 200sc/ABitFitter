package com.gamifyGame;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Andrew on 2/19/2015.
 */
public class ConsumableScreen extends GamifyScreen
{
    public ConsumableScreen(gamifyGame game)
    {
        super(game);
    }

    public void show() {
        //Image itemBar = renderer.imageSetup("ItemBar.png", layer1, 0, 254);
        Image placeHold = renderHelper.getRenderHelper().imageSetup("longBox.png", renderHelper.getRenderHelper().getLayer(1), 26, 8);
        placeHold.addListener(game.getListenerHelper().goScreen(0));
    }
}
