package com.gamifyGame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.HashMap;

/**
 * Created by Stephen on 2/1/2015.
 */
public class Quad3Screen extends GamifyScreen implements Screen {
    private ChangingImage[][] Week;
    private boolean shown = false;
    private SpriteBatch batch;

    private Image border;

    GamifyGraph[] testGraphs;

    public Quad3Screen(gamifyGame game) {
        super(game);
        batch = renderHelper.getRenderHelper().getBatch();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Preferences pref = game.getPrefs();
        boolean showChallengeHours = pref.getBoolean("showChallengeHours", false);

        if (!showChallengeHours){
            renderHelper.getRenderHelper().getLayer(1).draw();
            renderHelper.getRenderHelper().getLayer(2).draw();
        }


        ShapeRenderer shapes = renderHelper.getRenderHelper().getShapeRenderer();

        String showText;
        if (!showChallengeHours) {
            showText = "Challenge\nHours";
            /*int challengeProgress = pref.getInteger("challengeProgress", 0);

            shapes.begin(ShapeRenderer.ShapeType.Filled);
            if (challengeProgress == 100) {
                shapes.setColor(renderHelper.getRenderHelper().yellowLight);
            } else shapes.setColor(new Color(0.30f, 1.0f, 0.0f, 1.0f));
            shapes.box(retBox.getX() + 4, retBox.getY() + 4, 0, (float) (challengeProgress / 2.5), 3, 0);
            shapes.end();*/
        } else showText = "Close \nWindow";

        if (!showChallengeHours) {
            testGraphs[0].shapeRender();
        }

        renderHelper.getRenderHelper().moveCorner(retBox, Corner.UPPER_RIGHT, 30);
        if (showChallengeHours){
             renderHelper.getRenderHelper().getLayer(1).draw();
             renderHelper.getRenderHelper().getLayer(2).draw();
        }
        batch.begin();
        if (!showChallengeHours) {
            testGraphs[0].textRender();
        }
        renderHelper.getRenderHelper().textSet(showText, 2, 16, GamifyTextSize.BIG);
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 24; j++) {
                String text = Week[i][j].getString("time");
                showText = text.substring(2);
                int showTextInt = Integer.valueOf(showText);
                if (showTextInt < 12) {
                    if (showTextInt == 0) {
                        showText = "12AM";
                    } else showText = showText + "AM";
                } else if (showTextInt == 12) {
                    showText = "12PM";
                } else showText = String.valueOf(showTextInt % 12) + "PM";
                renderHelper.getRenderHelper().textSet(showText, (int) Week[i][j].getX(), (int) Week[i][j].getY() + 10);
                if (j + i == 0) {
                    renderHelper.getRenderHelper().textSet("SUN", (int) Week[i][j].getX(), (int) Week[i][j].getY() + 20);
                    renderHelper.getRenderHelper().textSet("MON", (int) Week[i][j].getX() + 20, (int) Week[i][j].getY() + 20);
                    renderHelper.getRenderHelper().textSet("TUE", (int) Week[i][j].getX() + 40, (int) Week[i][j].getY() + 20);
                    renderHelper.getRenderHelper().textSet("WED", (int) Week[i][j].getX() + 60, (int) Week[i][j].getY() + 20);
                    renderHelper.getRenderHelper().textSet("THU", (int) Week[i][j].getX() + 80, (int) Week[i][j].getY() + 20);
                    renderHelper.getRenderHelper().textSet("FRI", (int) Week[i][j].getX() + 100, (int) Week[i][j].getY() + 20);
                    renderHelper.getRenderHelper().textSet("SAT", (int) Week[i][j].getX() + 120, (int) Week[i][j].getY() + 20);
                }
            }
        }
        bringChallengeScreen();
        batch.end();
        renderHelper.getRenderHelper().endRender();
    }

    private void bringChallengeScreen() {
        boolean showChallengeHours = game.getPrefs().getBoolean("showChallengeHours", false);
        if (showChallengeHours && !shown) {


            border.moveBy(-300, 0);
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 24; j++) {
                    Week[i][j].moveBy(-300, 0);
                }
            }

            shown = true;

        } else if (!showChallengeHours && shown) {

            border.moveBy(300, 0);
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 24; j++) {
                    Week[i][j].moveBy(300, 0);
                }
            }
            shown = false;
        }
    }

    @Override
    public void show() {
        Stage layer1 = renderHelper.getRenderHelper().getLayer(1);
        renderHelper renderer = renderHelper.getRenderHelper();


        retBox = renderHelper.getRenderHelper().imageSetupCenter("trophyBox.png", layer1, -37, -25);
        Image showBox = renderHelper.getRenderHelper().imageSetup("48Box.png", layer1, 0, 0);

        testGraphs  = new GamifyGraph[2];

        renderer.imageSetup("largeScreenBox.png", renderer.getLayer(1), 19, 20);


        long aDay = 86400000;

        HashMap<Integer,Integer> testData3 = new HashMap<Integer,Integer>();
        testData3.put(0,63);
        testData3.put(1,20);
        testData3.put(2,70);
        testData3.put(3,45);
        testData3.put(4,0);
        testData3.put(5,50);
        String[] labels = {"Activity","Biking","Food","Running","Dancing","Sleep"};
        testGraphs[0] = new SpiderGraph(testData3,labels,"Types of Challenges",GamifyColor.GREEN, 19,20);

        int borderX = 19;
        int borderY = 20;
        int day = 0;
        int hour = 0;

        border = renderHelper.getRenderHelper().imageSetup("largeScreenBox.png", layer1, borderX + 300, borderY);
        Week = new ChangingImage[7][24];
        for (int i = 0; i < 7; i++) {
            int newX = borderX + 2 + (i * 20);
            for (int j = 0; j < 24; j++) {
                int newY = borderY + 232 - (j * 10);
                ChangingImage tempImage = new ChangingImage("inactiveHour.png", "activeHour.png", layer1, newX + 300, newY);
                String representation = String.valueOf(day) + ',' + String.valueOf(hour);
                tempImage.putExtra("time", representation);
                if (game.getPrefs().getBoolean(representation, true)) {
                    tempImage.swapTexture();
                }
                tempImage.addListener(game.getListenerHelper().getChallengeListener());
                Week[i][j] = tempImage;
                hour = (hour + 1) % 24;
            }
            day++;
        }

        retBox.addListener(new GoScreenClickListener(game.mainS, game));
        showBox.addListener(game.getListenerHelper().setBoolean("showChallengeHours", 'a'));

    }

    @Override
    public void hide() {
        border.moveBy(300, 0);
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 24; j++) {
                Week[i][j].moveBy(300, 0);
            }
        }
        shown = false;
        game.getPrefs().putBoolean("showChallengeHours",false);
        super.hide();
    }



}
