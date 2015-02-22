package com.gamifyGame;

/**
 * Created by Folly on 2/20/2015.
 */
public class HelpInfoBox extends TextDisplayBox {

    public HelpInfoBox(String path, float xOrigin, float yOrigin, String description, GamifyColor color){

        super(path);
        this.addAt(renderHelper.getRenderHelper().getLayer(3), xOrigin, yOrigin);
        this.addText(new Point(0,20), description, GamifyTextSize.MEDIUM, color);

    }

}
