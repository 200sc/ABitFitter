package com.gamifyGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Folly on 2/20/2015.
 * This Class pulls up the sleep overlay and does the needed background work to make the app know the user wants to sleep.
 */
public class SleepOverlayListener extends ClickListener {

    boolean displayingFlag;
    Array<Actor> toBeRestored;
    gamifyGame game;
    OverlayHelper overlay;

    public SleepOverlayListener(gamifyGame game){
        super();
        this.game = game;
    }

    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        return setSleepOverlay();
    }
    public boolean setSleepOverlay(){
        renderHelper renderer =  renderHelper.getRenderHelper();

        //TODO: Set stuff in background to know that sleeping is happening

        overlay = new OverlayHelper("overlay.png", game);
        if(!overlay.setup()){return true;}

        toBeRestored= new Array<Actor>( renderer.getLayer(1).getActors());
        renderer.getLayer(1).clear();

        TextDisplayBox resumeGame = new TextDisplayBox("popUpBoxBlue.png");
        resumeGame.addAt(renderer.getLayer(3), renderer.RENDER_WIDTH /2-renderer.textureHash.get("popUpBoxBlue.png").getWidth()/2, renderer.RENDER_HEIGHT/3-renderer.textureHash.get("popUpBoxBlue.png").getHeight()/2);
        resumeGame.addText(new Point(0, 0), "Resume Game", GamifyTextSize.BIG, GamifyColor.WHITE, "left");
        resumeGame.addListener(resumeListener);


        GamifyImage sleepingCap = new GamifyImage("nightCap.png");
        //sleepingCap.setSize(renderer.textureHash.get("48Box.png").getWidth()/2, renderer.textureHash.get("48Box.png").getHeight()); //TODO: get actual resourceand take out this line
        sleepingCap.addAt(renderer.getLayer(3), 6 + renderer.RENDER_WIDTH/2-renderer.textureHash.get("48Box.png").getWidth()/2/2, renderer.RENDER_HEIGHT*2/3 + 15);
        sleepingCap.setZIndex(0);

//        final GamifyImage z = new GamifyImage("leftZs.png");
        final float xLocOfZ = renderHelper.RENDER_WIDTH*2/3 - renderer.textureHash.get("leftZs.png").getWidth()/2;
        final float yLocOfZ = sleepingCap.getY() + 20;
//        z.addAt(renderer.getLayer(3), xLocOfZ, yLocOfZ);
        final ChangingImage z = new ChangingImage("leftZs.png", "rightZs.png", renderer.getLayer(3), (int)xLocOfZ, (int)yLocOfZ);
        z.addAction(new Action() {
            float deltaCount = 0;


            @Override
            public boolean act(float delta) {

                deltaCount = deltaCount + delta;
                int deltaC = (int) deltaCount;
                if (deltaC < 10) {          z.getColor().a = .8f;
                } else if (deltaC < 20) {  z.getColor().a = .6f;
                } else if (deltaC < 30) {  z.getColor().a = .2f;
                }

                if (deltaC > 30 && deltaC < 33) {
                    z.getColor().a = 0f;
                    z.setPosition(xLocOfZ, yLocOfZ);
                }else if(deltaC > 33){
                    deltaCount = 0;
                    z.getColor().a = 1;
                }else if(deltaC%4 ==0 || deltaC%4==1) {
                    z.moveBy(delta/2, delta);

                }else if(deltaC%4 == 1){
                    z.moveBy(delta,delta);
                }else if(deltaC%4 == 2){
                    z.moveBy(-delta/2, delta);
                }else{
                    z.moveBy(-delta, delta);
                }

                if(deltaC%6 == 2){
                    z.swapTexture(2);
                }else if(deltaC%6 ==5 ){
                    z.swapTexture(0);
                }
                return false;
            }
        });

        game.getActionResolver().putSharedPrefs("isSleeping", "true");

//        overlay.addShape(0,0, 100, 100);

        return true;
    }

    ClickListener resumeListener = new ClickListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){ resumeGame(); return true;}};




    private void resumeGame(){ // Gets the user back to the main screen away from help overlay

        for(Actor actor: toBeRestored){
            renderHelper.getRenderHelper().getLayer(1).addActor(actor);
        }
        overlay.resumeGame();
    }

}
