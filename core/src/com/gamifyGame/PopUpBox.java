package com.gamifyGame;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;


/**
 * Created by Andrew on 2/18/2015.
 */
public class PopUpBox extends GamifyImage
{
    private float lifeSpan;
    private String myString;

    public PopUpBox(float x, float y, final float startLifespan, String myString)
    {
        super("popUpBoxBlue.png");
        this.addAt(renderHelper.getRenderHelper().getLayer(2), x, y);
        this.lifeSpan=startLifespan;
        this.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                //super.act(delta);
               lifeSpan-=delta;
                if(lifeSpan<=0) {
                    remove();
                    return true;
                }
                return false;
            }
        });
        this.myString=myString;
    }
    public void draw(Batch b, float parentAlpha)
    {
        super.draw(b, parentAlpha);
        b.end();
        renderHelper.getRenderHelper().getBatch().begin();
        renderHelper.getRenderHelper().drawTextOnImage(myString, this, 0, 0);
        renderHelper.getRenderHelper().getBatch().end();
        b.begin();
    }
}
