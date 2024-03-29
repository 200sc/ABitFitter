package com.gamifyGame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;

/**
 * Created by Patrick Stephen on 2/1/2015.
 */
public class MainScreen extends GamifyScreen implements Screen
{
    private Image theSky;
    private TextDisplayBox quad3;
    private float deltaCount;
    private double deltaSum;
    private LoadingImage loadingBox;

    public MainScreen(gamifyGame game) {
         super(game);
    }

        @Override
        public void render(float delta)
        {
            super.render(delta);
            renderHelper.getRenderHelper().getLayer(1).draw();
            int challengeProgress = game.getPrefs().getInteger("challengeProgress",0);

            renderHelper.getRenderHelper().getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
            if (challengeProgress == 100){renderHelper.getRenderHelper().getShapeRenderer().setColor(renderHelper.getRenderHelper().yellowLight);}
            else renderHelper.getRenderHelper().getShapeRenderer().setColor(new Color(0.30f,1.0f,0.0f,1.0f));
            renderHelper.getRenderHelper().getShapeRenderer().box(quad3.getX()+4,quad3.getY()+4,0,(float)(challengeProgress/2.5),3,0);
            renderHelper.getRenderHelper().getShapeRenderer().end();

            renderHelper.getRenderHelper().getBatch().begin();

            //renderHelper.getRenderHelper().textSet(game.challengeText,15,50);
            if (!game.getPrefs().getString("isSleeping","false").equals("true")) {
                renderHelper.getRenderHelper().textSetCenter("Vitality", GamifyColor.GREEN, -28, 35, GamifyTextSize.BIG, "left", 0);
                renderHelper.getRenderHelper().textSetCenter(String.valueOf(game.getVitality()), GamifyColor.GREEN, -28 , 25, GamifyTextSize.XTRABIG,"left",0);
            }
            //renderHelper.getRenderHelper().textSet(String.valueOf(game.getPrefs().getString("graphTmp", "null")), 15, 30);
            renderHelper.getRenderHelper().getBatch().end();

            renderHelper.getRenderHelper().getLayer(2).draw();

            deltaCount = (deltaCount+1) % 32;
            renderHelper.getRenderHelper().endRender();
        }

        @Override
        public void show() {
            // called when this screen is set as the screen with game.setScreen();
            Preferences pref=game.getPrefs();
            Stage layer0 = renderHelper.getRenderHelper().getLayer(0);
            Stage layer1 = renderHelper.getRenderHelper().getLayer(1);

            // Only items that need listeners should be maintained as Images

            SkyImage.getSkyImage("nightSky.png");

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
            Integer[] bridges = json.fromJson(Integer[].class, pref.getString("undergroundBridges"));

            renderHelper.getRenderHelper().makeUnderground(0, game);
            renderHelper.getRenderHelper().makeBridges(layer0, bridges);


            TextDisplayBox quad1 = new TextDisplayBox("stepBox.png");
            renderHelper.getRenderHelper().imageSetupCenter("stepBox.png", layer1, 37, 50);
            quad1.addAtCenter(layer1,37,50);

            quad1.addText(new Point(22,18),String.valueOf(game.getPrefs().getInteger("stepsTakenToday",0)),GamifyTextSize.BIG,GamifyColor.WHITE, "right");

            TextDisplayBox quad2 = new TextDisplayBox("streakBox.png");
            quad2.addAtCenter(layer1,-37,50);
            quad2.addText(new Point(-12,-14),String.valueOf(game.getPrefs().getInteger("challengeStreak",0)),GamifyTextSize.BIG,GamifyColor.BLACK, "left");

            quad3 = new TextDisplayBox("trophyBox.png");
            quad3.addAtCenter(layer1,-37,-25);

            String challengeVariety = "No Challenge";

            File context = game.getActionResolver().getContextString();
            File challengeFile = new File(context ,"outChallenge");
            try{
                BufferedReader reader = new BufferedReader( new FileReader(challengeFile));
                String line;
                String[] lineParts;
                while ((line = reader.readLine()) != null) {
                    lineParts = line.split(",");
                    if (lineParts[0].equals("challengeVariety")){
                        challengeVariety = lineParts[1];
                    }
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }

            quad3.addText(new Point(-4,-3),String.valueOf(challengeVariety),GamifyTextSize.MEDIUM,GamifyColor.YELLOW,"left");
            //quad3 = renderHelper.getRenderHelper().imageSetupCenter("trophyBox.png", layer1, -37, -25);
            Image quad4 = renderHelper.getRenderHelper().imageSetupCenter("strawberryBox.png", layer1, 37, -25);
            Image midbox = renderHelper.getRenderHelper().imageSetupCenter("midBox.png", layer1, 0, 12);
            renderHelper.getRenderHelper().imageSetupCenter("leagueSilver.png", layer1, 0, -3);

            loadingBox = new LoadingImage(layer1,3,2,game,"loaded.png");



            //TODO: Decide if this is the right place for help
            //Sets up the button for triggering help
            HelpDisplay helpBox =new HelpDisplay("inactiveHour.png", game);
            helpBox.addAt(renderHelper.getRenderHelper().getLayer(3), renderHelper.getRenderHelper().RENDER_WIDTH/2 - renderHelper.getRenderHelper().textureHash.get("inactiveHour.png").getWidth()/2, 1);
            helpBox.setColor(Color.WHITE);

            //Set up the sleep bar
            TextDisplayBox sleepBox = new TextDisplayBox("longBox.png");
            float sleepX = (renderHelper.getRenderHelper().RENDER_WIDTH - renderHelper.getRenderHelper().textureHash.get("longBox.png").getWidth())/2;
            sleepBox.addAt(renderHelper.getRenderHelper().getLayer(1), sleepX,12);
            sleepBox.addText(new Point(0,0), "Click to Start Sleep Logging");
            SleepOverlayListener sleepListener = new SleepOverlayListener(game);
            sleepBox.addListener(sleepListener);

            pref.putString("isSleeping","false");
            pref.flush();
            if(pref.getString("isSleeping", "false").equals("true")){sleepListener.setSleepOverlay();}

            // Assign items their listeners
            quad1.addListener(new GoScreenClickListener(game.quad1S, game));
            //quad1.addListener(listenerH.setInt("toScreen",1));
            quad2.addListener(new GoScreenClickListener(game.consumableScreen, game));
            quad3.addListener(new GoScreenClickListener(game.quad3S, game));
            quad4.addListener(new GoScreenClickListener(game.quad4S, game));
            midbox.addListener(new GoScreenClickListener(game.buyS, game));
            deltaCount = 0;
        }
    }
