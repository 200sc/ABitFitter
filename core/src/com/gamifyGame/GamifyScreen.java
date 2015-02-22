package com.gamifyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Andrew on 2/9/2015.
 */
public abstract class GamifyScreen implements Screen {
    //protected ActionResolver actionResolver;
    //protected BitmapFont smallFont;
    //protected ShapeRenderer shapes;
    protected gamifyGame game;
    //protected float Ax, A2x, A5x, Ay, A2y, A5y, Az, A2z, A5z;
    protected int frameCount;
    protected Image retBox;
    int starsDrawn;
    ArrayList<Image> stars;


    public GamifyScreen(gamifyGame game)
    {
        this.game = game;
        stars = new ArrayList<Image>();
        starsDrawn = 0;
    }


    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            game.getActionResolver().toHomeScreen("Back Pressed");
        }

        Stage layer0 = renderHelper.getRenderHelper().getLayer(0);
        Stage layer1 = renderHelper.getRenderHelper().getLayer(1);
        Stage layer2 = renderHelper.getRenderHelper().getLayer(2);
        Stage layer3 = renderHelper.getRenderHelper().getLayer(3);

        // Undraw the last screen
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        layer0.draw();
        layer1.draw();
        layer2.draw();

        if (frameCount % 60 * 10 == 0){
            if (frameCount % (60*30) == 0) {
                game.updateChallenge();
            }
            System.gc();
        }
        game.updateVitality(delta);
        renderHelper.getRenderHelper().getLayer(0).act(Gdx.graphics.getDeltaTime());
        renderHelper.getRenderHelper().getLayer(1).act(Gdx.graphics.getDeltaTime());
        renderHelper.getRenderHelper().getLayer(2).act(Gdx.graphics.getDeltaTime());
        renderHelper.getRenderHelper().getLayer(3).act(Gdx.graphics.getDeltaTime());

        //renderHelper.getRenderHelper().getBatch().begin();
        //renderHelper.getRenderHelper().textSet(String.valueOf(frameCount),15,60);
        //renderHelper.getRenderHelper().textSet(String.valueOf(delta),15,70);
        //renderHelper.getRenderHelper().getBatch().end();

        frameCount = (frameCount + 1);
        frameCount = frameCount % (60*30);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public abstract void show();

    @Override
    public void hide() {
        // called when current screen changes from this to a different screen
        renderHelper.getRenderHelper().getLayer(1).clear();
        renderHelper.getRenderHelper().getLayer(2).clear();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK){

        }
        return false;
    }
}
