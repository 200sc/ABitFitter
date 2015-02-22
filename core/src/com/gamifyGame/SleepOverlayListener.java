package com.gamifyGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Folly on 2/20/2015.
 * This Class pulls up the sleep overlay and does the needed background work to make the app know the user wants to sleep.
 */
public class SleepOverlayListener extends ClickListener {
    final String helpBoxResource = "placeholder140x140.png";

    boolean displayingFlag;
    Array<Actor> toBeRestored;
    gamifyGame game;

    public SleepOverlayListener(gamifyGame game){
        super();
        this.game = game;
    }

    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
        renderHelper renderer =  renderHelper.getRenderHelper();

        //TODO: Set stuff in background to know that sleeping is happening

        OverlayHelper overlay = new OverlayHelper(helpBoxResource, game);
        if(!overlay.setup()){return true;}



        TextDisplayBox resumeGame = new TextDisplayBox("popUpBoxBlue.png");
        resumeGame.addAt(renderer.getLayer(3), renderer.RENDER_WIDTH /2-renderer.textureHash.get("popUpBoxBlue.png").getWidth()/2, renderer.RENDER_HEIGHT /3-renderer.textureHash.get("popUpBoxBlue.png").getHeight()/2);
        resumeGame.addText(new Point(0,0), "Resume Game", GamifyTextSize.BIG, GamifyColor.GREEN);
        resumeGame.addListener(overlay.resumeListener);
        resumeGame.setColor(Color.WHITE);

        GamifyImage sleepingCap = new GamifyImage("stockingCap.png");
        sleepingCap.setSize(renderer.textureHash.get("48Box.png").getWidth()/2, renderer.textureHash.get("48Box.png").getHeight()); //TODO: get actual resourceand take out this line
        sleepingCap.addAt(renderer.getLayer(3), 2 + renderer.RENDER_WIDTH /2-renderer.textureHash.get("48Box.png").getWidth()/2/2, renderer.RENDER_HEIGHT *2/3);

//        overlay.addShape(0,0, 100, 100);

        return true;
    }

    ClickListener resumeListener = new ClickListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){ resumeGame(); return true;}};
    private void resumeGame(){ // Gets the user back to the main screen away from help overlay
        renderHelper.getRenderHelper().getLayer(3).clear();
        for(Actor actor: toBeRestored){
            renderHelper.getRenderHelper().getLayer(3).addActor(actor);
        }
        renderHelper.getRenderHelper().resetProcessor();
        displayingFlag = false;
    }

}
