package com.gamifyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gamifyGame.Corner;
import com.gamifyGame.GamifyScreen;
import com.gamifyGame.gamifyGame;
import com.gamifyGame.renderHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Stephen on 2/1/2015.
 */
public class Quad1Screen extends GamifyScreen implements Screen {

    GamifyGraph[] testGraphs;


    public Quad1Screen(gamifyGame game) {
        super(game);
    }

    public void render(float delta) {
        super.render(delta);
        renderHelper.getRenderHelper().moveCorner(this.retBox, Corner.LOWER_LEFT, 30);

        int i = game.getPrefs().getInteger("currentScreen1Graph",0) % 7;
        if (i == -1){i = 6; game.getPrefs().putInteger("currentScreen1Graph",1);}

        renderHelper.getRenderHelper().getLayer(1).draw();
        testGraphs[i].shapeRender();
        renderHelper.getRenderHelper().getBatch().begin();


        testGraphs[i].textRender();
        renderHelper.getRenderHelper().getBatch().end();
        renderHelper.getRenderHelper().endRender();
        renderHelper.getRenderHelper().getLayer(2).draw();
    }

    public void show() {
        renderHelper renderer = renderHelper.getRenderHelper();
        retBox = renderer.imageSetupCenter("stepBox.png", renderer.getLayer(1), 37, 50);
        Image leftBox = renderer.imageSetup("arrowBoxLeft.png", renderer.getLayer(1),132,0);
        Image rightBox = renderer.imageSetup("arrowBoxRight.png", renderer.getLayer(1),156,0);

        retBox.addListener(new GoScreenClickListener(game.mainS, game));
        leftBox.addListener(game.getListenerHelper().setInt("currentScreen1Graph","--"));
        rightBox.addListener(game.getListenerHelper().setInt("currentScreen1Graph","++"));

        testGraphs = new GamifyGraph[7];
        renderer.imageSetup("largeScreenBox.png", renderer.getLayer(1), 36, 42);


        ArrayList<HashMap<Long, Integer>> graphData;
        graphData = new ArrayList<HashMap<Long, Integer>>(6);
        for(int i=0; i < 7; i++){
            graphData.add(new HashMap<Long, Integer>());
        }

        HashMap<Long,Integer> testData = new HashMap<Long,Integer>();

        long currentTime = System.currentTimeMillis();

        long today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);


        File context = game.getActionResolver().getContextString();

        String todayString = String.valueOf(today);
        for(int i=0; i < 7 ;i++){
            long day = today -i;
            if(day < 0){
                day += 365;
            }

            File dayFile = new File(context ,"day" + String.valueOf(day));
            try{
                BufferedReader reader = new BufferedReader( new FileReader(dayFile));
                String line = null;
                String[] lineParts;
                int j = 0;
                long curDay = System.currentTimeMillis() - (i * 1000 * 60 * 60 * 24);
                while ((line = reader.readLine()) != null) {
                    if(j < 5) {
                        lineParts = line.split(",");
                        graphData.get(i).put(curDay, Integer.parseInt(lineParts[1]));
                        j++;
                    }
                }


            }catch(Exception e){
                System.out.println(e.getMessage());

            }

        }

//        testData.put(System.currentTimeMillis()-40000,78);


        testGraphs[0] = new LineGraph(graphData.get(0),"Steps Taken",GamifyColor.GREEN,36,42);
        testGraphs[1] = new LineGraph(graphData.get(1),"Minutes Slept",GamifyColor.GREEN,36,42);
        testGraphs[2] = new LineGraph(graphData.get(2),"Minutes Walked",GamifyColor.GREEN,36,42);
        testGraphs[3] = new LineGraph(graphData.get(3),"Minutes Ran",GamifyColor.GREEN,36,42);
        testGraphs[4] = new LineGraph(graphData.get(4),"Minutes Biked",GamifyColor.GREEN,36,42);
        testGraphs[5] = new LineGraph(graphData.get(5),"Minutes Danced",GamifyColor.GREEN,36,42);

        HashMap<Integer,Integer> spiderData = new HashMap<Integer,Integer>();
        spiderData.put(0,50);
        spiderData.put(1,22);
        spiderData.put(2,0);
        spiderData.put(3,33);
        spiderData.put(4,46);
        spiderData.put(5,64);
        String[] labels = {"Percent Time Active", "Percent Time Excercising", "Vitamin Intake", "Percent Daily Values Reached", " Percent Days Well-Slept", "Percent Challenges Completed"};
        testGraphs[6] = new SpiderGraph(spiderData,labels,"Activity Distribution (this week)",GamifyColor.YELLOW, 38, 54);
    }


}
