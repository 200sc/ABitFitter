package com.gamifyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Json;

import java.util.Calendar;

/**
 * Created by Patrick Stephen on 2/1/2015.
 */
public class MainScreen extends GamifyScreen implements Screen
{
    private Image quad3;
    private float Ax, A2x, A5x, Ay, A2y, A5y, Az, A2z, A5z, deltaCount;
    private TextDisplayBox loadingBox;

    public MainScreen(gamifyGame game) {
         super(game);
    }

        @Override
        public void render(float delta)
        {
            super.render(delta);
            int challengeProgress = game.getPrefs().getInteger("challengeProgress",0);

            renderHelper.getRenderHelper().getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
            if (challengeProgress == 100){renderHelper.getRenderHelper().getShapeRenderer().setColor(renderHelper.getRenderHelper().yellowLight);}
            else renderHelper.getRenderHelper().getShapeRenderer().setColor(new Color(0.30f,1.0f,0.0f,1.0f));
            renderHelper.getRenderHelper().getShapeRenderer().box(quad3.getX()+4,quad3.getY()+4,0,(float)(challengeProgress/2.5),3,0);
            renderHelper.getRenderHelper().getShapeRenderer().end();

            renderHelper.getRenderHelper().getBatch().begin();

            if(game.getLoadingFlag()){
                if(deltaCount/8 % 4 == 0){   loadingBox.addText(new Point(0, 0), "Loading   ", "small");}
                if(deltaCount/8 % 4 == 1){   loadingBox.addText(new Point(0, 0), "Loading.  ", "small");}
                if(deltaCount/8 % 4 == 2){   loadingBox.addText(new Point(0, 0), "Loading.. ", "small");}
                if(deltaCount/8 % 4 == 3){   loadingBox.addText(new Point(0, 0), "Loading...", "small");}
            }
            else{
                loadingBox.addText(new Point(0, 0), "Loaded   ", "small");
            }

            renderHelper.getRenderHelper().textSetCenter("Your Vitality:", -25 , 25, "large");
            renderHelper.getRenderHelper().textSetCenter(String.valueOf(game.getVitality()), -20 , 0, "large");
            renderHelper.getRenderHelper().textSet(String.valueOf(game.getPrefs().getString("graphTmp","null")),15,30);
            renderHelper.getRenderHelper().textSet(String.valueOf(game.getPrefs().getInteger("minutesWalkedThisHour",0)),"black", 5,30, "medium");
            renderHelper.getRenderHelper().getBatch().end();
            deltaCount = (deltaCount+1) % 32;
        }

        @Override
        public void show() {
            // called when this screen is set as the screen with game.setScreen();
            Preferences pref=game.getPrefs();
            Stage layer0 = renderHelper.getRenderHelper().getLayer(0);
            Stage layer1 = renderHelper.getRenderHelper().getLayer(1);
            Stage layer2 = renderHelper.getRenderHelper().getLayer(2);

            // Only items that need listeners should be maintained as Images I.E
            // These two don't need listeners--
            renderHelper.getRenderHelper().imageSetup(timeOfDay(), layer0, 0, 0);
            renderHelper.getRenderHelper().imageSetup("background.png", layer0, 0, 0);

            // Create now for put/get
            Json json = new Json();
            String[] tmpImgs = null;
            // ToDO: Change start conditions once buildings can be added

            if(pref.getString("undergroundBuildings") != null) {
               tmpImgs = json.fromJson(String[].class, pref.getString("undergroundBuildings"));
            }
            if(pref.getString("undergroundBuildings") == null || tmpImgs == null || tmpImgs.length < 1 ){
                String[] imgs = {"Empty","HQ", "Empty", "Empty", "Empty",
                        "Empty", "Empty","Empty","Empty"};
                pref.putString("undergroundBuildings", json.toJson(imgs));
                pref.flush();
            }

            if(pref.getString("undergroundBridges") == null || json.fromJson(Integer[].class, pref.getString("undergroundBridges")) == null ){
                Integer[] tmpBridges = {1, 1, 1, 1, 2, 2};
                pref.putString("undergroundBridges", json.toJson(tmpBridges));
                pref.flush();
            }



            String[] underground = json.fromJson(String[].class, pref.getString("undergroundBuildings"));
            Integer[] bridges        = json.fromJson(Integer[].class, pref.getString("undergroundBridges"));

            //for(int i =0; i< underground.length; i++)
            //game.getActionResolver().showToast(underground[i]);

            renderHelper.getRenderHelper().makeUnderground(layer0, underground);
            renderHelper.getRenderHelper().makeBridges(layer0, bridges);


            // These five do.
            Image quad1 = renderHelper.getRenderHelper().imageSetupCenter("stepBox.png", layer1, 37, 50);
            //Image quad2 = renderHelper.getRenderHelper().imageSetupCenter("streakBox.png", layer1, -37, 50);
            TextDisplayBox quad2 = new TextDisplayBox("streakBox.png");
            quad2.addAtCenter(layer1,-37,50);
            quad2.addText(new Point(-12,-8),String.valueOf(game.getPrefs().getInteger("challengeStreak",0)),"medium","black");



            quad3 = renderHelper.getRenderHelper().imageSetupCenter("trophyBox.png", layer1, -37, -25);
            Image quad4 = renderHelper.getRenderHelper().imageSetupCenter("48Box.png", layer1, 37, -25);
            Image midbox = renderHelper.getRenderHelper().imageSetupCenter("midBox.png", layer1, 0, 12);



            loadingBox =new TextDisplayBox("activeHour.png");
            loadingBox.addAt(renderHelper.getRenderHelper().getLayer(0), 3, 2);
            loadingBox.setColor(Color.NAVY);


            //TODO: Decide if this is the right place for help
            //Sets up the button for triggering help
            HelpDisplay helpBox =new HelpDisplay("inactiveHour.png", game);
            helpBox.addAt(renderHelper.getRenderHelper().getLayer(3), 85, 1);
            helpBox.setColor(Color.WHITE);

            //Set up the sleep bar
            TextDisplayBox sleepBox = new TextDisplayBox("longBox.png");
            float sleepX = (renderHelper.getRenderHelper().RENDERED_SCREEN_WIDTH - renderHelper.getRenderHelper().textureHash.get("longBox.png").getWidth())/2;
            sleepBox.addAt(renderHelper.getRenderHelper().getLayer(1), sleepX,12);
            sleepBox.addText(new Point(0,0), "Click to Start Sleep Logging");



            // Assign items their listeners
            quad1.addListener(new GoScreenClickListener(game.quad1S, game));
            //quad1.addListener(listenerH.setInt("toScreen",1));
            quad2.addListener(new GoScreenClickListener(game.consumableScreen, game));
            quad3.addListener(new GoScreenClickListener(game.quad3S, game));
            quad4.addListener(new GoScreenClickListener(game.quad4S, game));
            midbox.addListener(new GoScreenClickListener(game.buyS, game));
            frameCount = 0;
            deltaCount = 0;

        }

        private String timeOfDay(){
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            if (hour < 5){ return "midnight.png";}
            else if (hour < 9){ return "sunrise.png";}
            else if (hour < 17){ return "day.png";}
            else return "night.png";
        }
    }
