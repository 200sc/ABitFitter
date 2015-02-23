package com.gamifyGame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

/**
 * Created by Andrew on 2/22/2015.
 */
public abstract class BuyScreen extends GamifyScreen
{
    private Buyable selectedBuyable;
    private MovingTextDisplayBox movingTextDisplayBox;

    public BuyScreen(gamifyGame game)
    {
        super(game);
        selectedBuyable =null;
    }

    public void show()
    {
        movingTextDisplayBox=new MovingTextDisplayBox("midBox.png");
        movingTextDisplayBox.addAt(renderHelper.getRenderHelper().getLayer(1), 180, 175);
    }

    public MovingTextDisplayBox getMovingTextDisplayBox()
    {
        return movingTextDisplayBox;
    }

    public void setSelectedBuyable(Buyable newBuyable)
    {
        selectedBuyable =newBuyable;
    }

    @Override
    public void render(float delta)
    {
        super.render(delta);
        renderHelper.getRenderHelper().getLayer(1).draw();
        renderHelper.getRenderHelper().getLayer(2).draw();
        if(selectedBuyable !=null)
            movingTextDisplayBox.addText(new Point(0, 30), selectedBuyable.toString(), GamifyTextSize.MEDIUM);
        movingTextDisplayBox.addText(new Point(0, -15),"Vitality "+game.getVitality(), GamifyTextSize.MEDIUM);
        renderHelper.getRenderHelper().endRender();
    }


}
