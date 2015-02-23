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


    private TextDisplayBox mainBox;

    public Quad4Screen(gamifyGame game) {

        super(game);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        renderHelper renderer = renderHelper.getRenderHelper();
        renderer.moveCorner(retBox, Corner.UPPER_LEFT, 30);
        renderHelper.getRenderHelper().getLayer(1).draw();
        renderHelper.getRenderHelper().getLayer(2).draw();



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

                String[] foodDescriptor = {"Brand ", "", "Calcium  ", "Calories  ", "Carbs ", "Protein ", "Sugar ", "Fiber ", "Serving Size "};
                renderer.getBatch().begin();
                mainBox.addText(new Point(0, renderer.textureHash.get("largeScreenBox.png").getHeight()/2-8) ,"Your last eaten food information", GamifyTextSize.BIG);
                String foodDescriptorString= "";
                for (int i = 0; i < 9; i++) {
                    if (foodInfo[i] != null) {
                        foodDescriptorString += foodDescriptor[i] + " " + foodInfo[i] + "\n\n";
                        //renderer.textSet(foodDescriptor[i] + foodInfo[i], 45, 130 - 10 * i);
                    }
                    mainBox.addText(new Point(0,renderer.textureHash.get("largeScreenBox.png").getHeight()/2 - 40), foodDescriptorString, GamifyTextSize.MEDIUM);
                }
                renderer.getBatch().end();
            } else {
                renderer.getBatch().begin();
                renderer.textSet("Your last eaten food information:", 45, 150, GamifyTextSize.BIG);
                renderer.textSet("Scan food to see data here:", 45, 130);
                renderer.getBatch().end();
            }
        }

        if(game.getPrefs().getInteger("FoodPopUp", 0) == 1){
            game.getPrefs().putInteger("FoodPopUp", 0);
            game.getPrefs().flush();
            new PopUpBox(60, 150, 3, "Barcode was not found in database");
        }

        renderHelper.getRenderHelper().endRender();
    }


    @Override
    public void show() {
        renderHelper renderer = renderHelper.getRenderHelper().getRenderHelper();

        retBox = renderer.imageSetupCenter("strawberryBox.png", renderer.getLayer(1), 37, -25);
        retBox.addListener(new GoScreenClickListener(game.mainS, game));



        mainBox = new TextDisplayBox("largeScreenBox.png");
        mainBox.addAt(renderer.getLayer(1), renderer.RENDER_WIDTH- renderer.textureHash.get("largeScreenBox.png").getWidth(), 0);

        // Set up scanning Image and its background
        TextDisplayBox scanBox = new TextDisplayBox("scannerBox.png");
        scanBox.addAt(renderer.getLayer(1), renderer.textureHash.get("48Box.png").getWidth(), renderer.RENDER_HEIGHT-renderer.textureHash.get("scannerBox.png").getHeight());
        scanBox.addListener(game.getListenerHelper().scanningAction());



        TextDisplayBox servingsBox = new TextDisplayBox("servingBox.png");
        servingsBox.addAt(renderer.getLayer(1), renderer.textureHash.get("48Box.png").getWidth() + renderer.textureHash.get("scannerBox.png").getWidth()
                , renderer.RENDER_HEIGHT-renderer.textureHash.get("servingBox.png").getHeight());
        servingsBox.addListener(game.getListenerHelper().getServingsChosen());

    }

    private void showRecentFoods(TextDisplayBox displayBox){
        displayBox.addText(new Point(-30, 10), "Hi");
    }

}
