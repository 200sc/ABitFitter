package com.gamifyGame;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Folly on 2/19/2015.
 * Makes the help button which will on click overlay with a contextual help interface for screens.
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
        this.addText(new Point(-2, 2), "Help", GamifyTextSize.MEDIUM);
        this.addHelpListener();
        this.imgWidth = renderHelper.getRenderHelper().textureHash.get(helpBoxResource).getWidth();
        this.imgHeight = renderHelper.getRenderHelper().textureHash.get(helpBoxResource).getHeight();

        this.displayingFlag = false;
    }

    private void displayHelpContext(){


        float xLoc = renderHelper.getRenderHelper().RENDER_WIDTH /2 - imgWidth/2;
        float yLoc = renderHelper.getRenderHelper().RENDER_HEIGHT /3;

        //Construct the overlay
        OverlayHelper overlay = new OverlayHelper(helpBoxResource, game);
        if(!overlay.setup()){return;} // Part of the promise otherwise bad things could happen


        TextDisplayBox resumeGame = new TextDisplayBox("longBox.png");
        resumeGame.addAt(renderHelper.getRenderHelper().getLayer(3), xLoc + 5, yLoc-renderHelper.getRenderHelper().textureHash.get("longBox.png").getHeight()/2);
        resumeGame.addText(new Point(0,0), "Resume Game");
        resumeGame.addListener(overlay.resumeListener);


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

    private void mainScreenDisplay(){
        HelpInfoBox midBoxDesc = new HelpInfoBox("48Box.png", 85, renderHelper.getRenderHelper().RENDER_HEIGHT /2 + 10, "The middle box displays your vitality which is the main resource of the game. \n If clicked on this will bring up the screen used to buy new buildings" , GamifyColor.GREEN);
    }
    private void quadScreen1Display(){
    }
    private void quadScreen2Display(){
    }
    private void quadScreen3Display(){
    }
    private void quadScreen4Display(){
    }
    private void buyScreenDisplay(){
        renderHelper renderer = renderHelper.getRenderHelper();
        Texture t48 = renderer.textureHash.get("48Box.png");
        HelpInfoBox scrollDesc = new HelpInfoBox("48Box.png", 15, renderer.RENDER_HEIGHT -t48.getHeight(), "\n\n Top bar is a scrollbar of buildings.\n" , GamifyColor.GREEN);
        HelpInfoBox scrollBoxDesc = new HelpInfoBox("48Box.png", 120, renderer.RENDER_HEIGHT - t48.getHeight()-60, "\n\n Pull a building down to see its description and cost." , GamifyColor.GREEN);
        HelpInfoBox buildingSpaceDesc = new HelpInfoBox("48Box.png", renderer.RENDER_WIDTH /2-t48.getWidth()
                        , renderer.RENDER_HEIGHT /2 - t48.getHeight()/2, " \n\nDrag Buildings from scrollbar. If there are valid spaces they will appear green.\n" , GamifyColor.GREEN);

        HelpInfoBox expandDesc = new HelpInfoBox("48Box.png", renderer.RENDER_WIDTH /2, renderer.RENDER_HEIGHT /6 , "\n\n Buy a level extender to be able to build up to 3 levels deep!.\n" , GamifyColor.GREEN);

        return ;
    }

}
