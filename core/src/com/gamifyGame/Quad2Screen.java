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
        retBox.addListener(game.getListener().goScreen(0));
    }
}
