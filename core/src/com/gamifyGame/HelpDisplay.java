package com.gamifyGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Folly on 2/19/2015.
 */
public class HelpDisplay extends TextDisplayBox {
    final String helpBoxResource = "placeholder140x140.png";
    float imgWidth, imgHeight;
    Array<Actor> toBeRestored;

    public HelpDisplay(String path) {
        super(path);
        this.addText(new Point(-2, 2), "Help", "m");
        this.addHelpListener();
        this.imgWidth = renderHelper.getRenderHelper().textureHash.get(helpBoxResource).getWidth();
        this.imgHeight = renderHelper.getRenderHelper().textureHash.get(helpBoxResource).getHeight();
    }

    private void displayHelpContext(){
        toBeRestored= new Array<Actor>( renderHelper.getRenderHelper().getLayer(3).getActors());
        if(toBeRestored.size < 1){
            return;
        }

        //Make it so the other layers are not interactable
        renderHelper.getRenderHelper().setProcessor(3);

        //Construct the overlay
        TextDisplayBox helpMenu =new TextDisplayBox(helpBoxResource);
        float xLoc = renderHelper.getRenderHelper().RENDERED_SCREEN_WIDTH/2 - imgWidth/2;
        float yLoc = renderHelper.getRenderHelper().RENDERED_SCREEN_HEIGHT/2 - imgHeight/2;
        helpMenu.addAt(renderHelper.getRenderHelper().getLayer(3), xLoc, yLoc);

        TextDisplayBox resumeGame = new TextDisplayBox("longBox.png");
        resumeGame.addAt(renderHelper.getRenderHelper().getLayer(3), xLoc, yLoc);
        resumeGame.addListener(resumeListener);


    }

    private void addHelpListener(){
        ClickListener helpListener = new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){ displayHelpContext(); return true;}};
        this.addListener(helpListener);
    }

    private void resumeGame(){ // Gets the user back to the main screen away from help overlay
        renderHelper.getRenderHelper().getLayer(3).clear();
        for(Actor actor: toBeRestored){
            renderHelper.getRenderHelper().getLayer(3).addActor(actor);
        }
        renderHelper.getRenderHelper().resetProcessor();
    }
    ClickListener resumeListener = new ClickListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){ resumeGame(); return true;}};


}
