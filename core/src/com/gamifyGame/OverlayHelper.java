package com.gamifyGame;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * Created by Folly on 2/20/2015.
 * A helper for all classes that want a layer 3 overlay that can write over text with shape renderer
 * Static class
 * Make sure to run setup, if it returns false then an overlay already is being displayed and better be dealt with first.
 */
public class OverlayHelper extends GamifyImage {
    private gamifyGame game;
    private static OverlayHelper overlay;
    private Array<Actor> toBeRestored;
    private ArrayList<ShapeInfo> toDraw;
    boolean displayingFlag;

    public static OverlayHelper getOverlayHelper(String path, gamifyGame game){
        if(overlay==null)
            overlay=new OverlayHelper(path , game);
        return overlay;
    }

    public OverlayHelper(String path, gamifyGame game){
        super(path);
        this.game = game;
        displayingFlag = false;
    }
    public boolean setup(){ // Sets stuff up if an overlay is not already active
        if(displayingFlag == true){
            return false;
        }
        displayingFlag = true;
        renderHelper renderer =renderHelper.getRenderHelper();
        toBeRestored= new Array<Actor>( renderer.getLayer(3).getActors());
        //Make it so the other layers are not interactable
        renderer.setProcessor(3);
        setSize(renderer.RENDER_WIDTH, renderer.RENDER_HEIGHT);
        addAt(renderer.getLayer(3), 0, 0);
        getColor().a = 0.4f;


        return true;
    }

    ClickListener resumeListener = new ClickListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){ resumeGame(); return true;}};
    public void resumeGame(){ // Gets the user back to the main screen away from help overlay
        toDraw = null;
        game.getActionResolver().putSharedPrefs("isSleeping", "false");
        renderHelper.getRenderHelper().getLayer(3).clear();
        for(Actor actor: toBeRestored){
            renderHelper.getRenderHelper().getLayer(3).addActor(actor);
        }
        renderHelper.getRenderHelper().resetProcessor();
    }

    public void draw(Batch b, float parentAlpha){
        super.draw(b, parentAlpha);
        renderHelper renderer = renderHelper.getRenderHelper();



//        if(toDraw != null){
//            b.end();
//            renderer.getShapeRenderer().begin();
//
//            for(ShapeInfo shape : toDraw){
//                renderer.makeBox(renderer.getShapeRenderer(), renderer.getLayer(3),
//                        shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
//            }
//            renderer.getShapeRenderer().end();
//            b.begin();
//        }

    }

    public void addShape(int x, int y, int width, int height){
        if(toDraw == null){ toDraw = new ArrayList<ShapeInfo>();}
        toDraw.add(new ShapeInfo(x, y, width, height));
        return;
    }

    private class ShapeInfo{
        private int x, y, width, height;
        public ShapeInfo(int x, int y, int width, int height){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public int getX() { return x; }

        public int getY() { return y; }

        public int getWidth() { return width; }

        public int getHeight() { return height; }



    }

}
