package com.gamifyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Stephen on 11/2/2014.
 */
public class renderHelper {

    public static final float RENDER_HEIGHT =296;
    public static final float RENDER_WIDTH =180;

    HashMap<String,Texture> textureHash;
    HashMap<String,TextureRegionDrawable> drawableHash;
    Texture boxBottomFace, boxBottomLeft, boxBottomLeftFace, boxBottomRight,
            boxBottomRightFace, boxRightFace, boxTopLeft, boxTopRight, boxLeftFace;

    int scrWidth, scrHeight;

    ShapeRenderer shapes;
    ScalingViewport view;
    Stage backgroundLayer, activeLayer, effectsLayer, overlayLayer;
    SpriteBatch batch;
    BitmapFont smallFont, medFont, bigFont, smallGreenFont, smallBlackFont, medBlackFont, bigBlackFont, medGreenFont, bigGreenFont, xtraBigFont, xtraBigBlackFont, xtraBigGreenFont;

    InputMultiplexer normalProcessor;

    // TODO: Because colors are being used in RGB / 255 values, make a function that does this nicer.
    final Color boxColor = new Color(56f/255,7f/255,24f/255,1);
    public final Color blueDark = new Color(0f/255,54f/255,99f/255,1);
    public final Color blueLight = new Color(0f/255,67f/255,122f/255,1);
    public final Color blueOutline = new Color(0f/255,39f/255,71f/255,1);
    public final Color greenDark = new Color(0f/255, 99f/255, 9f/255, 1);
    public final Color greenLight = new Color(0f/255, 150f/255, 15f/255, 1);
    public final Color greenOutline = new Color(0f/255, 125f/255, 14f/255, 1);
    public final Color yellowDark = new Color(150f/255, 122f/255, 0f/255, 1);
    public final Color yellowLight = new Color(252f/255, 206f/255, 0f/255,1);
    public final Color yellowOutline = new Color(201f/255, 164f/255, 0f/255, 1);


    private static renderHelper renderer;

    public static renderHelper getRenderHelper()
    {
        if(renderer==null)
           renderer=new renderHelper(1200, 1824);
        return renderer;
    }


    public static void forceRemake(float width, float height)
    {
        renderer=new renderHelper(width, height);
    }


    private renderHelper(float width, float height){
        scrWidth = Gdx.graphics.getWidth();
        scrHeight = Gdx.graphics.getHeight();

        float screenXRatio =  width/1200f;
        float screenYRatio =  height/1824f;

        shapes = new ShapeRenderer();
        shapes.scale(Float.valueOf(scrWidth)/180,Float.valueOf(scrHeight)/296,1);
        view = new ScalingViewport(Scaling.stretch, 180, 296);

        backgroundLayer = new Stage(view);
        activeLayer = new Stage(view);
        effectsLayer = new Stage(view);
        overlayLayer = new Stage(view);
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
        textureHash.put("nightSky.png",imageLoad("nightSky.png"));
        textureHash.put("background.png",imageLoad("background.png"));
        textureHash.put("activeHour.png",imageLoad("ActiveHour.png"));
        textureHash.put("inactiveHour.png",imageLoad("InactiveHour.png"));
        textureHash.put("streakBox.png",imageLoad("Streakbox.png"));
        textureHash.put("stepBox.png",imageLoad("StepBox.png"));
        textureHash.put("trophyBox.png",imageLoad("Trophybox.png"));
        textureHash.put("midBox.png",imageLoad("Midbox.png"));
        textureHash.put("48Box.png",imageLoad("48box.png"));
        textureHash.put("topHalfBox.png",imageLoad("tophalfbox.png"));
        textureHash.put("servingBox.png",imageLoad("servingBox.png"));
        textureHash.put("scannerBox.png",imageLoad("scannerBox.png"));

        textureHash.put("help.png", imageLoad("help.png"));
        textureHash.put("loaded.png", imageLoad("loaded.png"));
        textureHash.put("loading0.png",imageLoad("loading0.png"));
        textureHash.put("loading1.png",imageLoad("loading1.png"));
        textureHash.put("loading2.png",imageLoad("loading2.png"));
        textureHash.put("loading3.png",imageLoad("loading3.png"));

        textureHash.put("arrowBoxLeft.png",imageLoad("arrowBoxLeft.png"));
        textureHash.put("arrowBoxRight.png",imageLoad("arrowBoxRight.png"));

        textureHash.put("star.png", imageLoad("star.png"));

        textureHash.put("scrollBarKnub.png", imageLoad("scrollBarKnub.png"));

        textureHash.put("print_scan.png", imageLoad("print_scan.png"));

        textureHash.put("nightCap.png", imageLoad("nightCap.png"));
        textureHash.put("rightZs.png", imageLoad("rightZs.png"));
        textureHash.put("leftZs.png", imageLoad("leftZs.png"));

        textureHash.put("itemBar.png",imageLoad("ItemBar.png"));
        textureHash.put("longBox.png",imageLoad("longBox.png"));
        textureHash.put("placeholder128x24.png",imageLoad("placeholder128x24.png"));
        textureHash.put("placeholder140x140.png",imageLoad("placeholder140x140.png"));
        textureHash.put("placeholder64x64.png",imageLoad("placeholder64x64.png"));
        textureHash.put("largeScreenBox.png",imageLoad("LargeScreenBox.png"));
        textureHash.put("buyBar.png", imageLoad("buyBar.png"));
        textureHash.put("buildingBackground.png", imageLoad("buildingBackground.png"));
        textureHash.put("overlay.png", imageLoad("overlay.png"));

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
        textureHash.put("popUpBoxBlue.png",imageLoad("popUpBoxBlue.png"));
        textureHash.put("smallPopUpBoxBlue.png",imageLoad("smallPopUpBoxBlue.png"));

        textureHash.put("battery.png",imageLoad("battery.png"));
        textureHash.put("dollar.png",imageLoad("dollar.png"));
        textureHash.put("feather.png",imageLoad("feather.png"));

        //font3=new BitmapFont(("subway.fnt"), Gdx.files.internal("subway.png"), false);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("lastStory.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Math.round(32 * screenXRatio);
        medFont = generator.generateFont(parameter); // smallFont size 12 pixels
        medFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        medBlackFont = generator.generateFont(parameter); // smallFont size 12 pixels
        medBlackFont.setColor(Color.BLACK);
        medGreenFont = generator.generateFont(parameter); // smallFont size 12 pixels
        medGreenFont.setColor(greenLight);


        parameter.size = Math.round(24 * screenXRatio);
        smallFont = generator.generateFont(parameter);
        smallFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        smallBlackFont = generator.generateFont(parameter);
        smallBlackFont.setColor(Color.BLACK);
        smallGreenFont = generator.generateFont(parameter);
        smallGreenFont.setColor(greenLight);

        parameter.size = Math.round(48 * screenXRatio);
        bigFont = generator.generateFont(parameter);
        bigFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        bigBlackFont = generator.generateFont(parameter);
        bigBlackFont.setColor(Color.BLACK);
        bigGreenFont = generator.generateFont(parameter);
        bigGreenFont.setColor(greenLight);

        parameter.size = Math.round(64*screenXRatio);
        xtraBigFont = generator.generateFont(parameter);
        bigFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        xtraBigBlackFont = generator.generateFont(parameter);
        xtraBigBlackFont.setColor(Color.BLACK);
        xtraBigGreenFont = generator.generateFont(parameter);
        xtraBigGreenFont.setColor(Color.GREEN);

        generator.dispose();

        normalProcessor = new InputMultiplexer(activeLayer, overlayLayer);
        Gdx.input.setInputProcessor(normalProcessor);

    }

    public void resetProcessor(){
        Gdx.input.setInputProcessor(normalProcessor);
    }

    public void setProcessor(int which){
        Gdx.input.setInputProcessor(new InputMultiplexer(overlayLayer));
    }

    public void endRender(){
        getLayer(3).draw();
    }

    /*
     **********Image Setup functions***********
                                              */
    public Image imageSetupCenter(String key, Stage stage, float hOffset, float vOffset){
        Texture texture = textureHash.get(key);
        Image image = new Image(texture);
        setPositionCenter(stage,image,hOffset,vOffset);
        image.setSize(texture.getWidth(),texture.getHeight());
        image.setName(key);
        stage.addActor(image);
        return image;
    }

    public void drawTextOnImageNicely(String text, Image image, float offsetx, float offsety) {
        drawTextOnImageNicely(text,image,offsetx,offsety,GamifyTextSize.MEDIUM,GamifyColor.WHITE, "left");
    }

    public void drawTextOnImageNicely(String text, Image image, float offsetx, float offsety, GamifyTextSize size, GamifyColor color, String align) {
        BitmapFont curFont = getFont(color, size);
        BitmapFont.TextBounds curBounds = curFont.getBounds(text);
        Point convertedDimensions=new Point(curBounds.width, curBounds.height);
        convertedDimensions=convertTextCoorsToImageCoors(convertedDimensions);

        float toOffset = convertedDimensions.x/2;
        if(convertedDimensions.x/2 > (image.getImageWidth()-4)/2){toOffset = image.getImageWidth()/2;}

        Point textCoorsLoc=new Point(offsetx+image.getX()+(image.getImageWidth()+4)/2- toOffset , offsety+image.getY()+image.getImageHeight()/2);

        textSet(text, color,  (int) textCoorsLoc.x , (int) textCoorsLoc.y, size, align,  image.getWidth());
        //medFont.draw(batch, text, textCoorsLoc.x, textCoorsLoc.y);
    }

    public Point getBounds(String text)
    {
        BitmapFont.TextBounds curBounds = medFont.getBounds(text);
        Point convertedDimensions=convertTextCoorsToImageCoors(new Point(curBounds.width, curBounds.height));
        return new Point(convertedDimensions.x, convertedDimensions.y);
    }


    public Point convertImageCoorsToTextCoors(Point point)
    {
        return new Point(point.x*scrWidth/ RENDER_WIDTH, point.y*scrHeight/ RENDER_HEIGHT);
    }
    public Point convertTextCoorsToImageCoors(Point point)
    {
        return new Point(point.x* RENDER_WIDTH /scrWidth, point.y* RENDER_HEIGHT /scrHeight);
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
    public static void setPositionCenter(Stage stage,Image image,float hOffset,float vOffset){
        image.setPosition((stage.getWidth() / 2) - (image.getWidth() / 2) + hOffset,
                ((stage.getHeight() / 2) - (image.getHeight() / 2)) + vOffset);
    }

    // Moves an image to a corner;
    // quad = corner (1,2,3 or 4, quadrant definition)
    // frames = estimated # of frames to complete action
    public void moveCorner(Image toMove, Corner quad, int frames){
        if (quad == Corner.UPPER_RIGHT)
        {
            movePosition(toMove, new Point(RENDER_WIDTH - toMove.getImageWidth(), RENDER_HEIGHT - toMove.getImageHeight()), frames, 2);
        }
        else if (quad == Corner.UPPER_LEFT)
        {
            movePosition(toMove, new Point(0, RENDER_HEIGHT - toMove.getImageHeight()), frames, 2);
        }
        else if (quad == Corner.LOWER_LEFT)
        {
            movePosition(toMove, new Point(0, 0), frames, 2);
        }
        else
        {
           movePosition(toMove, new Point(RENDER_WIDTH - toMove.getImageWidth(), 0), frames, 2);
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
    public float getRenderedWidth() { return RENDER_WIDTH;}
    public ShapeRenderer getShapeRenderer(){return shapes;}
    public ScalingViewport getView(){return view;}
    public SpriteBatch getBatch(){return batch;}
    public Color getBoxColor(){return boxColor;}
    public BitmapFont getSmallFont(){return medFont;}
    public Stage getLayer(int level){
        if (level == 0){return backgroundLayer;}
        else if (level == 1){return activeLayer;}
        else if (level == 2){return effectsLayer;}
        else return overlayLayer;
    }

    public ShapeRenderer newShapeRenderer(){
        ShapeRenderer toReturn = new ShapeRenderer();
        toReturn.scale(Float.valueOf(scrWidth)/180,Float.valueOf(scrHeight)/296,1);
        return toReturn;
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


    public ArrayList<GamifyImage> makeUnderground(int stageNum, gamifyGame game)
    {
        Stage stage=renderHelper.getRenderHelper().getLayer(stageNum);
        Json json = new Json();
        String[] buildings=json.fromJson(String[].class, game.getPrefs().getString("undergroundBuildings"));
        ArrayList<GamifyImage> toReturn=new ArrayList<GamifyImage>(buildings.length);
        //ChangingImage[] imageList = new ChangingImage[buildings.length];
        // Figure out the width and height from the HQ1
        int bridgelen = 6; //TODO: change this

        int width = textureHash.get("HQ1.png").getWidth() + bridgelen;
        int height = textureHash.get("HQ1.png").getHeight() + 2;
        int wOffset = 6; //TODO: Find the optimal lengths
        int hOffset = (int) (height * 2.9);
        int row = 0;
        int column = 0;

        int firstZIndex = 0;



        for(int i=0; i <= buildings.length-1; i++)
        {

            GamifyImage toAdd=new GamifyImage("Empty1.png");
            //if(buildings[i].equals(""))
                //toAdd=new GamifyImage("Empty1.png");
            if(!buildings[i].equals("Empty"))
            {
                toAdd = Building.getDefaultBuildings().get(buildings[i]);
                if(toAdd==null) {
                    toAdd = new GamifyImage("Empty1.png");
                    toAdd.setColor(Color.CYAN);
                }

            }
            column = i %3;
            row = i/3;
            toAdd.addAt(stage, wOffset + column*width, hOffset - row*height);
            toReturn.add(toAdd);
            if(i ==0){firstZIndex = toAdd.getZIndex();}
        }


        Image basicBox = renderer.imageSetup("buildingBackground.png", renderHelper.getRenderHelper().getLayer(0), 3+wOffset/2, hOffset-(row)*height);
        basicBox.setSize( width*3-bridgelen, height* (row+1)-2);
        basicBox.setZIndex(firstZIndex);


        return toReturn;
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
        int bridgelen = 6; //TODO: change this
        int hqWidth = textureHash.get("HQ1.png").getWidth();
        int width = textureHash.get("HQ1.png").getWidth()+bridgelen;
        int height = textureHash.get("HQ1.png").getHeight() + 2;
        int wOffset = 6; //TODO: Find the optimal lengths
        int hOffset = (int) (height * 2.9);
        int row;
        int column;
        for(int i=0; i <= bridges.length-1; i++){
            column = i%2;
            row = i/2;

//            if(bridges[i] == 1){imageSetup("Bridge1.png", stage, (int)(wOffset + hqWidth+(column)*width), hOffset - row*height);}
//            if(bridges[i] == 2){imageSetup("Elevator1.png", stage, (int)(wOffset+hqWidth+(column)*width), hOffset - row*height);}
            if(bridges[i] == 1){new ChangingImage("Bridge1.png", "Elevator1.png", stage, ((wOffset + hqWidth+(column)*width)-3), hOffset - row*height);}
            if(bridges[i] == 2){new ChangingImage("Elevator1.png","Bridge1.png", stage, ((wOffset+hqWidth+(column)*width)-3), hOffset - row*height);}
        }
    }

    // Font and Text functions

    // getFont
    private BitmapFont getFont(GamifyColor color, GamifyTextSize size){
        switch (color){
            case BLUE:
            case YELLOW:
            case PINK:
            case WHITE:
                switch (size){
                    case SMALL: return smallFont;
                    case BIG: return bigFont;
                    case XTRABIG: return xtraBigFont;
                    default: return medFont;
                }
            case BLACK:
                switch (size){
                    case SMALL: return smallBlackFont;
                    case BIG: return bigBlackFont;
                    case XTRABIG: return xtraBigBlackFont;
                    default: return medBlackFont;
                }
            case GREEN:
                switch (size){
                    case SMALL: return smallGreenFont;
                    case BIG: return bigGreenFont;
                    case XTRABIG: return xtraBigGreenFont;
                    default: return medGreenFont;
                }
            default: return medFont;
        }
    }
    public void textSet(String text, float x, float y){
        textSet(text,GamifyColor.WHITE,x,y,GamifyTextSize.MEDIUM,"left",0);
    }
    public void textSet(String text, float x, float y, GamifyTextSize size){
        textSet(text,GamifyColor.WHITE,x,y,size,"left",0);
    }
    public void textSetCenter(String text, float x, float y){
        textSet(text,GamifyColor.WHITE,x+ RENDER_WIDTH/2,y+ RENDER_HEIGHT/2,GamifyTextSize.MEDIUM,"left",0);
    }
    public void textSetCenter(String text, GamifyColor color, float x, float y, GamifyTextSize size, String align, float lineWidth){
        textSet(text,color,x+ RENDER_WIDTH/2,y+ RENDER_HEIGHT/2,size,align,lineWidth);
    }
    public void textSet(String text, GamifyColor color, float x, float y, GamifyTextSize size, String align, float lineWidth){
        BitmapFont curFont = getFont(color,size);
        if (align.equals("right")) x -= (180*Math.min(curFont.getBounds(text).width,lineWidth*scrWidth/180))/scrWidth;
        else if (align.equals("center")) x-= (90*Math.min(curFont.getBounds(text).width,lineWidth*scrWidth/180))/scrWidth;
        if (lineWidth == 0){ curFont.drawMultiLine(batch, text, (x * scrWidth) / 180, (y * scrHeight) / 296);}
        else curFont.drawWrapped(batch,text,(x * scrWidth) / 180,(y * scrHeight) / 296, (lineWidth * scrWidth) / 180);
    }
}
