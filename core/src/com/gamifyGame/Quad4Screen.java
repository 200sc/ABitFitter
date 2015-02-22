package com.gamifyGame;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;


/**
 * Created by Stephen on 2/1/2015.
 */
public class Quad4Screen extends GamifyScreen implements Screen {


    private TextDisplayBox textDisplayBox;

    public Quad4Screen(gamifyGame game) {

        super(game);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        renderHelper renderer = renderHelper.getRenderHelper();
        renderer.moveCorner(retBox, Corner.UPPER_LEFT, 30);


        renderer.getBatch().begin();


        renderer.textSet("Click for number", 70, 277);
        renderer.textSet(" of servings", 70, 267);
        renderer.getBatch().end();

        //Set up the data from the last food seen
        String latestFood;
        if(game.getPrefs().getString("latestFood") != null) {
            latestFood = game.getPrefs().getString("latestFood");

            // Parse the data from its Json form
            Json json = new Json();
            HashMap food = json.fromJson(HashMap.class, latestFood);
            if (food != null && food.get("nutrition") != null) {
                JsonValue nutritionJson = (JsonValue) food.get("nutrition");
                HashMap nutritionMap = json.readValue(HashMap.class, nutritionJson);

                //HashMap nutrients = json.fromJson(HashMap.class, (String) food.get("nutrition"));


                String brand = (String) food.get("brand_name");
                String product = (String) food.get("product_name");


                String[] foodInfo = new String[9];
                foodInfo[0] = (String) food.get("brand_name");
                foodInfo[1] = (String) food.get("product_name");
                foodInfo[2] = (String) nutritionMap.get("calcium");
                foodInfo[3] = (String) nutritionMap.get("calories");
                foodInfo[4] = (String) nutritionMap.get("carbohydrate");
                foodInfo[5] = (String) nutritionMap.get("protein");
                foodInfo[6] = (String) nutritionMap.get("sugar");
                foodInfo[7] = (String) nutritionMap.get("fiber");
                foodInfo[8] = (String) nutritionMap.get("serving_description");

                if (foodInfo[0] == null) {
                    foodInfo[0] = "No Food Recently Scanned";
                    foodInfo[1] = "Use the scan button to scan barcode of food.";
                }

                String[] foodDescriptor = {"", "", "Calcium : ", "Calories : ", "Carbs: ", "Protein: ", "Sugar: ", "Fiber: ", "Serving Size: "};
                renderer.getBatch().begin();
                renderer.textSet("Your last eaten food information:", 45, 150, GamifyTextSize.BIG);
                for (int i = 0; i < 9; i++) {
                    if (foodInfo[i] != null) {
                        renderer.textSet(foodDescriptor[i] + foodInfo[i], 45, 130 - 10 * i);
                    }
                }
                renderer.getBatch().end();
            } else {
                renderer.getBatch().begin();
                renderer.textSet("Your last eaten food information:", 45, 150, GamifyTextSize.BIG);
                renderer.textSet("Scan food to see data here:", 45, 130);
                renderer.getBatch().end();
            }
        }
        showRecentFoods(textDisplayBox);

    }


    @Override
    public void show() {
        renderHelper renderer = renderHelper.getRenderHelper().getRenderHelper();

        retBox = renderer.imageSetupCenter("48Box.png", renderer.getLayer(1), 37, -25);
        retBox.addListener(new GoScreenClickListener(game.mainS, game));

        // Set up scanning Image and its background
        TextDisplayBox scanBox = new TextDisplayBox("48Box.png");
        scanBox.addAt(renderer.getLayer(1), -30, 60);

        Image dumbServingsBox = renderer.imageSetupCenter("48Box.png", renderer.getLayer(1), 0,110);
        dumbServingsBox.addListener(game.getListenerHelper().getServingsChosen());

        //Image graphBox = renderer.imageSetupCenter("48Box.png", renderer.getLayer(1), 30, 60);

        // Silly scanning image with tongue
        Image scanImage = renderer.imageSetup("print_scan.png", renderer.getLayer(1), 38, 185);
        scanImage.setSize(scanImage.getWidth()/8, scanImage.getHeight()/8);
        //scanImage.setColor(com.badlogic.gdx.graphics.Color.MAGENTA);
        scanImage.addListener(game.getListenerHelper().scanningAction());

        Image basicBox = renderer.imageSetup("topHalfBox.png", renderer.getLayer(1), 0, 30);

        textDisplayBox=new TextDisplayBox("midBox.png");
        textDisplayBox.addAt(renderHelper.getRenderHelper().getLayer(1), 120, 150);
    }

    private void showRecentFoods(TextDisplayBox displayBox){
        displayBox.addText(new Point(-30, 10), "Hi");
    }

}
