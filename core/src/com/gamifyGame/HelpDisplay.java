package com.gamifyGame;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
        this.imgWidth = renderHelper.getRenderHelper().textureHash.get("overlay.png").getWidth();
        this.imgHeight = renderHelper.getRenderHelper().textureHash.get("overlay.png").getHeight();

        this.displayingFlag = false;
    }

    private void displayHelpContext(){


        float xLoc = renderHelper.getRenderHelper().RENDER_WIDTH /2;
        float yLoc = renderHelper.getRenderHelper().RENDER_HEIGHT /3;

        //Construct the overlay
        OverlayHelper overlay = new OverlayHelper("overlay.png", game);
        if(!overlay.setup()){return;} // Part of the promise otherwise bad things could happen
        overlay.setColor(Color.GRAY);
        overlay.getColor().a = 0.4f;



        Screen curScreen =  game.getScreen();
        if(  curScreen instanceof MainScreen){mainScreenDisplay();}
        else if(curScreen instanceof Quad1Screen){quadScreen1Display();}
        else if(curScreen instanceof Quad2Screen){quadScreen2Display();}
        else if(curScreen instanceof Quad3Screen){quadScreen3Display();}
        else if(curScreen instanceof Quad4Screen){quadScreen4Display();}
        else if(curScreen instanceof ConsumableScreen){consumableDisplay();}
        else{buyScreenDisplay();}


        TextDisplayBox resumeGame = new TextDisplayBox("smallPopUpBoxBlue.png");
        resumeGame.addAt(renderHelper.getRenderHelper().getLayer(3), xLoc-renderHelper.getRenderHelper().textureHash.get("smallPopUpBoxBlue.png").getWidth()/2, 12);
        resumeGame.addText(new Point(0, 0), "Resume Game");
        resumeGame.addListener(overlay.resumeListener);

    }

    private void addHelpListener(){
        ClickListener helpListener = new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){ displayHelpContext(); return true;}};
        this.addListener(helpListener);
    }

    private void mainScreenDisplay(){
        HelpInfoBox vitalityDesc = new HelpInfoBox("smallPopUpBoxBlue.png", 95, renderHelper.RENDER_HEIGHT /2 + 18, "Vitality is the main resource of the game" , GamifyColor.GREEN);
        HelpInfoBox midBoxDesc = new HelpInfoBox("smallPopUpBoxBlue.png", 80, renderHelper.RENDER_HEIGHT /2- 10, "Redirects to screen for building screen" , GamifyColor.GREEN);
        HelpInfoBox dataDesc = new HelpInfoBox("smallPopUpBoxBlue.png", renderHelper.RENDER_WIDTH-4-renderHelper.getRenderHelper().textureHash.get("smallPopUpBoxBlue.png").getWidth(), renderHelper.RENDER_HEIGHT*2/3, "Redirects to a screen with graphs about your fitness", GamifyColor.GREEN);
        HelpInfoBox consumableDesc = new HelpInfoBox("smallPopUpBoxBlue.png", 4, renderHelper.RENDER_HEIGHT*2/3, "Redirects to screen for buying consumable boosts", GamifyColor.GREEN);

        HelpInfoBox foodDesc = new HelpInfoBox("smallPopUpBoxBlue.png", 4, renderHelper.RENDER_HEIGHT * 2/7, "Redirects to screen to enter food information", GamifyColor.GREEN);
        HelpInfoBox challengeDesc = new HelpInfoBox("smallPopUpBoxBlue.png", renderHelper.RENDER_WIDTH-4-renderHelper.getRenderHelper().textureHash.get("smallPopUpBoxBlue.png").getWidth(), renderHelper.RENDER_HEIGHT * 2/7, "Redirects to a screen for setting challenge hours", GamifyColor.GREEN);


        HelpInfoBox sleepScreen = new HelpInfoBox("smallPopUpBoxBlue.png", renderHelper.RENDER_WIDTH * 2/3, 4, "Click to start logging sleep, stops logging everything else", GamifyColor.GREEN);

    }
    private void quadScreen3Display(){
        HelpInfoBox sDesc = new HelpInfoBox("smallPopUpBoxBlue.png", 4, 36, "Shows or hides challenge hour selection", GamifyColor.GREEN);
        HelpInfoBox blueDesc = new HelpInfoBox("smallPopUpBoxBlue.png", renderHelper.RENDER_WIDTH-4-renderHelper.getRenderHelper().textureHash.get("smallPopUpBoxBlue.png").getWidth(), renderHelper.RENDER_HEIGHT*2/3, "Hours in blue are open for challeneges", GamifyColor.GREEN);

        HelpInfoBox challengeSelectorDesc = new HelpInfoBox("smallPopUpBoxBlue.png", renderHelper.RENDER_WIDTH/2, renderHelper.RENDER_HEIGHT/3, "Select hours for which challenges can happen", GamifyColor.GREEN);

    }
    private void quadScreen2Display(){
        consumableDisplay();
    }
    private void consumableDisplay()
    {
        renderHelper renderer = renderHelper.getRenderHelper();
        Texture t48 = renderer.textureHash.get("smallPopUpBoxBlue.png");
        HelpInfoBox buyableDesc = new HelpInfoBox("smallPopUpBoxBlue.png", 15, renderer.RENDER_HEIGHT -t48.getHeight(), "Buy consumables by clicking here\n" , GamifyColor.GREEN);
        HelpInfoBox inventory = new HelpInfoBox("smallPopUpBoxBlue.png", 37, renderer.RENDER_HEIGHT -t48.getHeight()-60, "The numbers beside each item is how many you have \n" , GamifyColor.GREEN);
        HelpInfoBox buildingSpaceDesc = new HelpInfoBox("smallPopUpBoxBlue.png", renderer.RENDER_WIDTH /2-t48.getWidth()
                , renderer.RENDER_HEIGHT /2 - t48.getHeight()/2-20, "Drag a consumable from your inventory to a building to use it" , GamifyColor.GREEN);
    }
    private void quadScreen1Display(){
        HelpInfoBox returnDesc = new HelpInfoBox("smallPopUpBoxBlue.png", 4, 36, "Return to main screen", GamifyColor.GREEN);
        HelpInfoBox graphDesc = new HelpInfoBox("smallPopUpBoxBlue.png", renderHelper.RENDER_WIDTH-4-renderHelper.getRenderHelper().textureHash.get("smallPopUpBoxBlue.png").getWidth(), renderHelper.RENDER_HEIGHT*2/3, "Graphs of your data", GamifyColor.GREEN);
        HelpInfoBox changeGraph = new HelpInfoBox("smallPopUpBoxBlue.png",renderHelper.RENDER_WIDTH-renderHelper.getRenderHelper().textureHash.get("smallPopUpBoxBlue.png").getWidth()+4,4, "Cycle through graphs to show", GamifyColor.GREEN);


    }
    private void quadScreen4Display(){
        HelpInfoBox showFoodDesc = new HelpInfoBox("smallPopUpBoxBlue.png", 2, renderHelper.RENDER_HEIGHT - 60, "To return to main screen", GamifyColor.GREEN);
        HelpInfoBox scanDesc = new HelpInfoBox("smallPopUpBoxBlue.png", renderHelper.RENDER_WIDTH/3, renderHelper.RENDER_HEIGHT -35, "Click to bring up scanner for food", GamifyColor.GREEN);

        HelpInfoBox servingDesc = new HelpInfoBox("smallPopUpBoxBlue.png", renderHelper.RENDER_WIDTH * 3/4 -15, renderHelper.RENDER_HEIGHT - 60, "Select hours for which challenges can happen", GamifyColor.GREEN);
        HelpInfoBox mainDesc = new HelpInfoBox("smallPopUpBoxBlue.png", renderHelper.RENDER_WIDTH/2, renderHelper.RENDER_HEIGHT/3, "Select hours for which challenges can happen", GamifyColor.GREEN);


    }
    private void buyScreenDisplay(){
        renderHelper renderer = renderHelper.getRenderHelper();
        Texture t48 = renderer.textureHash.get("smallPopUpBoxBlue.png");
        HelpInfoBox scrollDesc = new HelpInfoBox("smallPopUpBoxBlue.png", 15, renderer.RENDER_HEIGHT -t48.getHeight(), "Top bar is a scrollbar of buildings\n" , GamifyColor.GREEN);
        HelpInfoBox scrollBoxDesc = new HelpInfoBox("smallPopUpBoxBlue.png", 100, renderer.RENDER_HEIGHT - t48.getHeight()-60, "Pull a building down to see description and cost" , GamifyColor.GREEN);
        HelpInfoBox buildingSpaceDesc = new HelpInfoBox("smallPopUpBoxBlue.png", renderer.RENDER_WIDTH /2-t48.getWidth()
                        , renderer.RENDER_HEIGHT /2 - t48.getHeight()/2, "If there are valid spaces they will appear green" , GamifyColor.GREEN);

        HelpInfoBox expandDesc = new HelpInfoBox("smallPopUpBoxBlue.png", renderer.RENDER_WIDTH /2, renderer.RENDER_HEIGHT /6 , "Buy a new levels to build up to 3 levels deep!" , GamifyColor.GREEN);



        return ;
    }

}
