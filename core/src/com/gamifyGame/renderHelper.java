package com.gamifyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;

/**
 * Created by Stephen on 11/2/2014.
 */
public class renderHelper {

    public static final float RENDERED_SCREEN_HEIGHT=296;
    public static final float RENDERED_SCREEN_WIDTH=180;

    HashMap<String,Texture> textureHash;
    HashMap<String,TextureRegionDrawable> drawableHash;
    Texture boxBottomFace, boxBottomLeft, boxBottomLeftFace, boxBottomRight,
            boxBottomRightFace, boxRightFace, boxTopLeft, boxTopRight, boxLeftFace;

    int scrWidth, scrHeight;

    ShapeRenderer shapes;
    ScalingViewport view;
    Stage backgroundLayer, activeLayer, topLayer;
    SpriteBatch batch;
    BitmapFont font, font2;

    final Color boxColor = new Color(new Float(56)/255,new Float(7)/255,new Float(24)/255,1);


    private static renderHelper renderer;

    public static renderHelper getRenderHelper()
    {
        if(renderer==null)
           renderer=new renderHelper();
        return renderer;
    }
    public static void forceRemake()
    {
        renderer=new renderHelper();
    }


    private renderHelper(){
        scrWidth = Gdx.graphics.getWidth();
        scrHeight = Gdx.graphics.getHeight();
        shapes = new ShapeRenderer();
        shapes.scale(Float.valueOf(scrWidth)/180,Float.valueOf(scrHeight)/296,1);
        view = new ScalingViewport(Scaling.stretch, 180, 296);

        backgroundLayer = new Stage(view);
        activeLayer = new Stage(view);
        topLayer = new Stage(view);
        batch = new SpriteBatch();

        // Load all image files
        textureHash = new HashMap<String,Texture>();
        drawableHash = new HashMap<String,TextureRegionDrawable>();

        // Box Parts
        boxBottomFace = imageLoad("boxBottomFace.png");
        boxBottomLeft = imageLoad("boxBottomLeft.png");
        boxBottomLeftFace = imageLoad("boxBottomLeftFace.png");
        boxBottomRight = imageLoad("boxBottomRight.png");
        boxBottomRightFace = imageLoad("boxBottomRightFace.png");
        boxLeftFace = imageLoad("boxLeftFace.png");
        boxRightFace = imageLoad("boxRightFace.png");
        boxTopLeft = imageLoad("boxTopLeft.png");
        boxTopRight = imageLoad("boxTopRight.png");

        /* //These likely don't need to be in the HashMap
        textureHash.put("boxBottomFace.png",imageLoad("boxBottomFace.png"));
        textureHash.put("boxBottomLeft.png",imageLoad("boxBottomLeft.png"));
        textureHash.put("boxBottomLeftFace.png",imageLoad("boxBottomLeftFace.png"));
        textureHash.put("boxBottomRight.png",imageLoad("boxBottomRight.png"));
        textureHash.put("boxBottomRightFace.png",imageLoad("boxBottomRightFace.png"));
        textureHash.put("boxLeftFace.png",imageLoad("boxLeftFace.png"));
        textureHash.put("boxRightFace.png",imageLoad("boxRightFace.png"));
        textureHash.put("boxTopLeft.png",imageLoad("boxTopLeft.png"));
        textureHash.put("boxTopRight.png",imageLoad("boxTopRight.png"));
        */
        textureHash.put("day.png",imageLoad("day.png"));
        textureHash.put("night.png",imageLoad("night.png"));
        textureHash.put("midnight.png",imageLoad("midnight.png"));
        textureHash.put("sunrise.png",imageLoad("sunrise.png"));
        textureHash.put("background.png",imageLoad("background.png"));
        textureHash.put("activeHour.png",imageLoad("ActiveHour.png"));
        textureHash.put("inactiveHour.png",imageLoad("InactiveHour.png"));
        textureHash.put("streakBox.png",imageLoad("Streakbox.png"));
        textureHash.put("stepBox.png",imageLoad("StepBox.png"));
        textureHash.put("trophyBox.png",imageLoad("Trophybox.png"));
        textureHash.put("midBox.png",imageLoad("Midbox.png"));
        textureHash.put("48Box.png",imageLoad("48box.png"));

        textureHash.put("print_scan.png", imageLoad("print_scan.png"));

        textureHash.put("itemBar.png",imageLoad("ItemBar.png"));
        textureHash.put("placeholder128x24.png",imageLoad("placeholder128x24.png"));
        textureHash.put("placeholder140x140.png",imageLoad("placeholder140x140.png"));
        textureHash.put("placeholder64x64.png",imageLoad("placeholder64x64.png"));
        textureHash.put("largeScreenBox.png",imageLoad("LargeScreenBox.png"));
        textureHash.put("buyBar.png", imageLoad("buyBar.png"));

        textureHash.put("Armory1.png",imageLoad("Armory1.png"));
        textureHash.put("Computer1.png",imageLoad("Computer1.png"));
        textureHash.put("Costume1.png",imageLoad("Costume1.png"));
        textureHash.put("Elevator1.png",imageLoad("Elevator1.png"));
        textureHash.put("Bridge1.png",imageLoad("Bridge1.png"));
        textureHash.put("Empty1.png",imageLoad("Empty1.png"));
        textureHash.put("Forgery1.png",imageLoad("Forgery1.png"));
        textureHash.put("Garage1.png",imageLoad("Garage1.png"));
        textureHash.put("Generator1.png",imageLoad("Generator1.png"));
        textureHash.put("HQ1.png",imageLoad("HQ1.png"));
        textureHash.put("Lab1.png",imageLoad("Lab1.png"));
        textureHash.put("Smuggler1.png",imageLoad("Smuggler1.png"));


        //font3=new BitmapFont(("subway.fnt"), Gdx.files.internal("subway.png"), false);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("subFree.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        font2 = generator.generateFont(parameter); // font size 12 pixels
        font2.setColor(1.0f,1.0f,1.0f,1.0f);
        parameter.size = 24;
        font= generator.generateFont(parameter);
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        generator.dispose();

        Gdx.input.setInputProcessor(activeLayer);
    }

    /*
     **********Image Setup functions***********
                                              */
    public Image imageSetupCenter(String key, Stage stage, int hOffset, int vOffset){
        Texture texture = textureHash.get(key);
        Image image = new Image(texture);
        setPositionCenter(stage,image,hOffset,vOffset);
        image.setSize(texture.getWidth(),texture.getHeight());
        image.setName(key);
        stage.addActor(image);
        return image;
    }
    public void textSet(String text, int x, int y, String str){
        BitmapFont curFont;
        if (str.equals("small")){
            curFont = font;
        }
        else {
            curFont = font2;
        }
        curFont.drawMultiLine(batch, text, (x * scrWidth) / 180, (y * scrHeight) / 296);
    }

    public void textSet(String text, int x, int y){
        textSet(text,x,y,"normal");
    }
    public void textSetCenter(String text, int offsetx, int offsety){
        BitmapFont.TextBounds bounds = font2.getBounds(text); //TODO: Use text boundaries to center text
        Point textLoc= convertImageCoorsToTextCoors(new Point(RENDERED_SCREEN_HEIGHT/2+offsetx, RENDERED_SCREEN_WIDTH/2+offsety));
        font2.draw(batch, text, (textLoc.x),
                (textLoc.y));
    }
    public void drawTextOnImage(String text, Image image, int offsetx, int offsety)
    {
        Point textCoorsLoc=new Point(image.getX()+image.getImageWidth()/2 , image.getY()+image.getImageHeight()/2);
        font2.draw(batch, text, textCoorsLoc.x, textCoorsLoc.y );
    }

    public Point convertImageCoorsToTextCoors(Point point)
    {
        return new Point(point.x*scrWidth/RENDERED_SCREEN_WIDTH, point.y*scrHeight/RENDERED_SCREEN_HEIGHT);
    }


    public static Texture imageLoad(String file)
    { return new Texture(file);}

    public static Image applyTexture(Texture texture, Stage stage, float hOrigin, float vOrigin){
        Image image = new Image(texture);
        image.setPosition(hOrigin,vOrigin);
        image.setSize(texture.getWidth(),texture.getHeight());
        image.setName(texture.toString());
        stage.addActor(image);
        return image;
    }

    public Image imageSetup(String key, Stage stage, float hOrigin, float vOrigin){
        Texture texture = textureHash.get(key);
        Image image = new Image(texture);
        image.setPosition(hOrigin,vOrigin);
        image.setSize(texture.getWidth(),texture.getHeight());
        image.setName(key);
        stage.addActor(image);
        return image;
    }

    public static void setImageTexture(Image image, String file){
        image.setDrawable(new TextureRegionDrawable( new TextureRegion(new Texture(file))));
    }

    // This acts like the inherent Image.setPosition, but at center to pair with
    // ImageSetupCenter
    public static void setPositionCenter(Stage stage,Image image,int hOffset,int vOffset){
        image.setPosition((stage.getWidth() / 2) - (image.getWidth() / 2) + hOffset,
                ((stage.getHeight() / 2) - (image.getHeight() / 2)) + vOffset);
    }

    // Moves an image to a corner;
    // quad = corner (1,2,3 or 4, quadrant definition)
    // frames = estimated # of frames to complete action
    public void moveCorner(Image toMove, Corner quad, int frames){
        if (quad == Corner.UPPER_RIGHT)
        {
            movePosition(toMove, new Point(RENDERED_SCREEN_WIDTH - toMove.getImageWidth(), RENDERED_SCREEN_HEIGHT - toMove.getImageHeight()), frames, 2);
        }
        else if (quad == Corner.UPPER_LEFT)
        {
            movePosition(toMove, new Point(0, RENDERED_SCREEN_HEIGHT - toMove.getImageHeight()), frames, 2);
        }
        else if (quad == Corner.LOWER_LEFT)
        {
            movePosition(toMove, new Point(0, 0), frames, 2);
        }
        else
        {
           movePosition(toMove, new Point(RENDERED_SCREEN_WIDTH - toMove.getImageWidth(), 0), frames, 2);
        }
    }

    public void movePosition(Image toMove, Point desiredPoint, int frames, float minSpeed)
    {
        minSpeed=Math.abs(minSpeed);

        Point location=new Point(toMove.getX(), toMove.getY());
        Point distances=desiredPoint.getXYDistances(location);

        if(Math.abs(distances.x)<1)
        {
            distances.x = 0;
            toMove.setPosition(desiredPoint.x, location.y);
        }
        else
        {
            distances.scaleXBy((float) 1 / (float) frames);
            if(Math.abs(distances.x)<minSpeed)
            {
                distances.x=Math.abs(distances.x)/distances.x*minSpeed;
            }
        }
        if(Math.abs(distances.y)<1)
        {
           distances.y = 0;
           toMove.setPosition(location.x, desiredPoint.y);
        }
        else
        {
            distances.scaleYBy((float) 1 / (float) frames);
            if(Math.abs(distances.y)<minSpeed)
            {
                distances.y=Math.abs(distances.y)/distances.y*minSpeed;
            }
        }
        toMove.moveBy(distances.x, distances.y);
    }

    // Getters
    public int getWidth(){return scrWidth;}
    public int getHeight(){return scrHeight;}
    public ShapeRenderer getShapeRenderer(){return shapes;}
    public ScalingViewport getView(){return view;}
    public SpriteBatch getBatch(){return batch;}
    public Color getBoxColor(){return boxColor;}
    public BitmapFont getFont(){return font2;}
    public Stage getLayer(int level){
        if (level == 0){return backgroundLayer;}
        else if (level == 1){return activeLayer;}
        else return topLayer;
    }
    public TextureRegionDrawable getTextureRegionDrawable(String file){
        if (drawableHash.containsKey(file)){
            return drawableHash.get(file);
        }
        else {
            TextureRegionDrawable toReturn = new TextureRegionDrawable(new TextureRegion(textureHash.get(file)));
            drawableHash.put(file,toReturn);
            return toReturn;
        }
    }






    public Image imageActor(String key, float hOrigin, float vOrigin){
        Texture texture = textureHash.get(key);
        Image image = new Image(texture);
        image.setPosition(hOrigin,vOrigin);
        image.setSize(texture.getWidth(),texture.getHeight());
        image.setName(key);
        return image;
    }


    public ChangingImage[] makeUnderground(Stage stage, String[] buildings){
        ChangingImage[] imageList = new ChangingImage[buildings.length];
        // Figure out the width and height from the HQ1
        int bridgelen = 8; //TODO: change this
        int width = textureHash.get(buildings[1]).getWidth() + bridgelen;
        int height = textureHash.get(buildings[1]).getHeight() + 2;
        int wOffset = 4; //TODO: Find the optimal lengths
        int hOffset = (int) (height * 2.9);
        int row = 0;
        int column = 0;
        for(int i=0; i <= buildings.length-1; i++){
            column = i %3;
            row = i/3;
            imageList[i] = new ChangingImage(buildings[i], "Empty1.png", stage,wOffset + column*width, hOffset - row*height);
            imageList[i].putExtra("undergroundIndex", i);
        }
        return imageList;
    }

    public int buildCheck(ChangingImage[] possibleBuildingSites,Image toBuy ){
        int foundIndex = -1;
        float minX = toBuy.getX();
        float maxX = toBuy.getRight();
        float minY = toBuy.getY();
        float maxY = toBuy.getTop();
        for(int i=0; i<possibleBuildingSites.length; i ++){
            ChangingImage tmp = possibleBuildingSites[i];
            if (rectangleCollided(minX, maxX, minY, maxY, tmp.getX(), tmp.getRight(), tmp.getY(), tmp.getTop())){
                //Here we change the value!
                if(foundIndex == -1){
                    possibleBuildingSites[i].nameChange(toBuy.getName());
                    foundIndex = i;
                    return i;
                }
            }
            else{
                //possibleBuildingSites[i].setColor(Color.BLUE);
            }
        }
        return -1;
    }



    public boolean rectangleCollided(float minX1, float maxX1, float minY1, float maxY1,
                                     float minX2, float maxX2, float minY2, float maxY2){
        if(maxX1>minX2 && maxX2>minX1 && maxY1>minY2 && maxY2>minY1){return true;}
        return false;
    }

    public void colorUnderground(Stage stage, ChangingImage[] undergroundHandles){
        for(int i=0; i <undergroundHandles.length; i++){
            undergroundHandles[i].setColor(Color.GREEN);
        }
    }

    public void makeBridges(Stage stage, Integer[] bridges){
        // Figure out the width and height from the HQ1
        int bridgelen = 8; //TODO: change this
        int hqWidth = textureHash.get("HQ1.png").getWidth();
        int width = textureHash.get("HQ1.png").getWidth()+bridgelen;
        int height = textureHash.get("HQ1.png").getHeight() + 2;
        int wOffset = 4; //TODO: Find the optimal lengths
        int hOffset = (int) (height * 2.9);
        int row = 0;
        int column = 0;
        for(int i=0; i <= bridges.length-1; i++){
            column = i%2;
            row = i/2;

//            if(bridges[i] == 1){imageSetup("Bridge1.png", stage, (int)(wOffset + hqWidth+(column)*width), hOffset - row*height);}
//            if(bridges[i] == 2){imageSetup("Elevator1.png", stage, (int)(wOffset+hqWidth+(column)*width), hOffset - row*height);}
            if(bridges[i] == 1){new ChangingImage("Bridge1.png", "Elevator1.png", stage, (int)((wOffset + hqWidth+(column)*width)-1), hOffset - row*height);}
            if(bridges[i] == 2){new ChangingImage("Elevator1.png","Bridge1.png", stage, (int)((wOffset+hqWidth+(column)*width)-1), hOffset - row*height);}
        }
    }

    public void makeBox(ShapeRenderer shapes, Stage stage, int x, int y, int w, int h){
        assert w > 4;
        assert h > 4;
        Image tImage;
        shapes.rect(x,y,w,h);
        applyTexture(boxBottomLeft, stage, x, y-2);
        for (int i = 2; i < w - 2; i++) {
            if (i < (w - 2) / new Float(4)){
                applyTexture(boxBottomLeftFace, stage, x+i, y-2);
            }
            else if (i > 3*((w - 2) / new Float(4))){
                applyTexture(boxBottomRightFace, stage, x+i, y-2);
            }
            else applyTexture(boxBottomFace, stage, x+i, y-2);
        }
        applyTexture(boxBottomRight, stage, x+w-2,y-2);
        for (int i = y; i < y+h - 2; i++) {
            applyTexture(boxRightFace, stage, x+w-2, i);
        }
        applyTexture(boxTopRight, stage, x+w-2,y+h-11);
        for (int i = 0; i < w - 4; i++) {
            if (i < (w - 2) / new Float(4)){
                tImage = applyTexture(boxBottomRightFace, stage, x + w -2 -i, y+h-2);
            }
            else if (i > 3*((w - 2) / Float.valueOf(4))){
                tImage = applyTexture(boxBottomLeftFace, stage, x + w -2 -i, y+h-2);
            }
            else tImage = applyTexture(boxBottomFace, stage, x + w -2 -i, y+h-2);
            tImage.setRotation(180);
        }
        applyTexture(boxTopLeft, stage, x, y+h-11);
        for (int i = y+h-2; i > y - 2; i--) {
            applyTexture(boxLeftFace, stage, x, i);
        }
    }
}
