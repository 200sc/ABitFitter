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
    private float Ax, A2x, A5x, Ay, A2y, A5y, Az, A2z, A5z;

    public MainScreen(gamifyGame game) {
         super(game);
    }

        @Override
        public void render(float delta)
        {
            super.render(delta);

            int challengeProgress = game.getPrefs().getInteger("challengeProgress",50);

            renderHelper.getRenderHelper().getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
            if (challengeProgress == 100){renderHelper.getRenderHelper().getShapeRenderer().setColor(new Color(0.99f,0.99f,0.0f,1.0f));}
            else renderHelper.getRenderHelper().getShapeRenderer().setColor(new Color(0.30f,1.0f,0.0f,1.0f));
            renderHelper.getRenderHelper().getShapeRenderer().box(quad3.getX()+4,quad3.getY()+4,0,(float)(challengeProgress/2.5),3,0);
            renderHelper.getRenderHelper().getShapeRenderer().end();

            renderHelper.getRenderHelper().getBatch().begin();



            // ***** DEBUG PRINTING ***** //
            if (frameCount % 5 == 0) {
                challengeProgress--;
                if (challengeProgress < 0){
                    challengeProgress = 100;
                }
                game.getPrefs().putInteger("challengeProgress", challengeProgress);
                game.getPrefs().flush();
                A5x = A2x;
                A5y = A2y;
                A5z = A2z;
                A2x = Ax;
                A2y = Ay;
                A2z = Az;
                Ax = Gdx.input.getAccelerometerX();
                Ay = Gdx.input.getAccelerometerY();
                Az = Gdx.input.getAccelerometerZ();
            }
            /*
            if (Ax < 5 && Ax > 3 && Ay > 1 && Ay < 4 && Az > 6 && Az < 9){
                renderHelper.getRenderHelper().getSmallFont().draw(renderHelper.getRenderHelper().getBatch(), "Sitting",50,260);
            }
            else if (Ax < 0 && Ax > -4 && Ay > 7 && Ay < 10 && Az > -1 && Az < 4){
                renderHelper.getRenderHelper().getSmallFont().draw(renderHelper.getRenderHelper().getBatch(), "Standing",50,260);
            }
            else renderHelper.getRenderHelper().getSmallFont().draw(renderHelper.getRenderHelper().getBatch(), "Neithering",50,260);
            */

            renderHelper.getRenderHelper().textSetCenter("Your Vitality:", -25 , 25, "large");
            renderHelper.getRenderHelper().textSetCenter(String.valueOf(game.getVitality()), -20 , 0, "large");
            renderHelper.getRenderHelper().textSet(String.valueOf(game.getPrefs().getString("graphTmp","null")),40,20);
            renderHelper.getRenderHelper().textSet(String.valueOf(game.getPrefs().getInteger("updaterRunning",0)),20,20);
            renderHelper.getRenderHelper().textSet("This is a really long string that shouldn't fit into a single line", 80,230,30);
            renderHelper.getRenderHelper().getSmallFont().draw(renderHelper.getRenderHelper().getBatch(), String.valueOf(frameCount),50,200);
            renderHelper.getRenderHelper().getBatch().end();
            // If we want to do more things with frame counting in groups of 30
            frameCount = (frameCount + 1) % 30;
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
            Image quad2 = renderHelper.getRenderHelper().imageSetupCenter("streakBox.png", layer1, -37, 50);
            quad3 = renderHelper.getRenderHelper().imageSetupCenter("trophyBox.png", layer1, -37, -25);
            Image quad4 = renderHelper.getRenderHelper().imageSetupCenter("48Box.png", layer1, 37, -25);
            Image midbox = renderHelper.getRenderHelper().imageSetupCenter("midBox.png", layer1, 0, 12);

            // Assign items their listeners
            quad1.addListener(game.getListenerHelper().goScreen(1));
            //quad1.addListener(listenerH.setInt("toScreen",1));
            quad2.addListener(game.getListenerHelper().goScreen(2));
            quad3.addListener(game.getListenerHelper().goScreen(3));
            quad4.addListener(game.getListenerHelper().goScreen(4));
            midbox.addListener(game.getListenerHelper().goScreen(5));
            frameCount = 0;
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
