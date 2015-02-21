package com.gamifyGame;

/**
 * Created by Folly on 2/20/2015.
 */
public class HelpInfoBox extends TextDisplayBox {

    public HelpInfoBox(String path, float xOrigin, float yOrigin, String description, String color){

        super(path);
        this.addAt(renderHelper.getRenderHelper().getLayer(3), xOrigin, yOrigin);
        this.setColor(renderHelper.getRenderHelper().yellowOutline);
        this.getColor().a = 0.45f;
        this.addText(new Point(0,20), description, "medium", color);

    }

}
