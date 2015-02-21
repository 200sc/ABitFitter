package com.gamifyGame;

import com.badlogic.gdx.Screen;
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
    gamifyGame game;

    public HelpDisplay(String path, gamifyGame curGame) {
        super(path);
        this.game = curGame;
        this.addText(new Point(-2, 2), "Help", "m");
        this.addHelpListener();
        this.imgWidth = renderHelper.getRenderHelper().textureHash.get(helpBoxResource).getWidth();
        this.imgHeight = renderHelper.getRenderHelper().textureHash.get(helpBoxResource).getHeight();

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
        helpMenu.setSize(renderHelper.getRenderHelper().RENDERED_SCREEN_WIDTH, renderHelper.getRenderHelper().RENDERED_SCREEN_HEIGHT);
        helpMenu.addAt(renderHelper.getRenderHelper().getLayer(3), 0,0);

        helpMenu.getColor().a = 0.4f;


        TextDisplayBox resumeGame = new TextDisplayBox("longBox.png");
        resumeGame.addAt(renderHelper.getRenderHelper().getLayer(3), xLoc + 5, yLoc);
        resumeGame.addText(new Point(0,0), "Resume Game");
        resumeGame.addListener(resumeListener);


        Screen curScreen =  game.getScreen();
        if(  curScreen instanceof MainScreen){mainScreenDisplay();}
        else if(curScreen instanceof Quad1Screen){quadScreen1Display();}
        else if(curScreen instanceof Quad2Screen){quadScreen2Display();}
        else if(curScreen instanceof Quad3Screen){quadScreen3Display();}
        else if(curScreen instanceof Quad4Screen){quadScreen4Display();}
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
//        TextDisplayBox midBoxDesc  =new TextDisplayBox("48Box.png");
//        midBoxDesc.addAt(renderHelper.getRenderHelper().getLayer(3), 85, renderHelper.getRenderHelper().RENDERED_SCREEN_HEIGHT/2);
//        midBoxDesc.setColor(renderHelper.getRenderHelper().yellowOutline);
//        midBoxDesc.getColor().a = 0.3f;
//        midBoxDesc.addText(new Point(0,0), "This Goes to buying stuff");

        HelpInfoBox midBoxDesc = new HelpInfoBox("48Box.png", 85, renderHelper.getRenderHelper().RENDERED_SCREEN_HEIGHT/2 + 10, "The middle box displays your vitality which is the main resource of the game. \n If clicked on this will bring up the screen used to buy new buildings" , "black");


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
