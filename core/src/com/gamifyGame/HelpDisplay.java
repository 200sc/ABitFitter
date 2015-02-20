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
    boolean displayingFlag;
    Array<Actor> toBeRestored;
    int currentScreenNumber; //where main is 0 and buy is 5
    public HelpDisplay(String path) {
        super(path);
        this.addText(new Point(-2, 2), "Help", "m");
        this.addHelpListener();
        this.imgWidth = renderHelper.getRenderHelper().textureHash.get(helpBoxResource).getWidth();
        this.imgHeight = renderHelper.getRenderHelper().textureHash.get(helpBoxResource).getHeight();
        this.currentScreenNumber = 0;
        this.displayingFlag = false;
    }

    private void displayHelpContext(){
        if(displayingFlag){return ;}
        displayingFlag = true;
        toBeRestored= new Array<Actor>( renderHelper.getRenderHelper().getLayer(3).getActors());

        //Make it so the other layers are not interactable
        renderHelper.getRenderHelper().setProcessor(3);



        //Construct the overlay

        float xLoc = renderHelper.getRenderHelper().RENDERED_SCREEN_WIDTH/2 - imgWidth/2;
        float yLoc = renderHelper.getRenderHelper().RENDERED_SCREEN_HEIGHT/2 - imgHeight/2;
        TextDisplayBox helpMenu =new TextDisplayBox(helpBoxResource);
        helpMenu.addAt(renderHelper.getRenderHelper().getLayer(3), xLoc, yLoc);
        helpMenu.setVisible(false);

        TextDisplayBox resumeGame = new TextDisplayBox("longBox.png");
        resumeGame.addAt(renderHelper.getRenderHelper().getLayer(3), xLoc + 5, yLoc);
        resumeGame.addText(new Point(0,0), "Resume Game");
        resumeGame.addListener(resumeListener);

        if(currentScreenNumber == 0){mainScreenDisplay();}
        else if(currentScreenNumber == 1){quadScreen1Display();}
        else if(currentScreenNumber == 2){quadScreen2Display();}
        else if(currentScreenNumber == 3){quadScreen3Display();}
        else if(currentScreenNumber == 4){quadScreen4Display();}
        else{buyScreenDisplay();}
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
        displayingFlag = false;
    }
ClickListener resumeListener = new ClickListener(){
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){ resumeGame(); return true;}};


    private void mainScreenDisplay(){


        return ;
    }
    private void quadScreen1Display(){
        return ;
    }
    private void quadScreen2Display(){
        return ;
    }
    private void quadScreen3Display(){
        return ;
    }
    private void quadScreen4Display(){
        return ;
    }
    private void buyScreenDisplay(){
        return ;
    }

}
