package com.gamifyGame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 2/15/2015.
 */
public abstract class GamifyGraph {


    String type;
    public Image background;
    public boolean dumped;
    Color color1, color2, color3;
    public HashMap<Long,Integer> data;
    List<Long> keys;

    public final int botLabelCount = 5;
    public final int leftLabelCount = 10;

    public long end;
    public long start;

    public final int graphHeight = 240;
    public final int graphWidth = 130;

    int borderX = 36;
    int borderY = 42;

    public void shapeRender(){

    }

    public void textRender(){

    }

}
