package com.gamifyGame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Andrew on 2/16/2015.
 */
public class GamifyImage extends Image
{
    public GamifyImage(String path)
    {
        Texture text1 = renderHelper.getRenderHelper().textureHash.get(path);
        TextureRegionDrawable textured1 = renderHelper.getRenderHelper().getTextureRegionDrawable(path);
        this.setDrawable(textured1);
        this.setSize(text1.getWidth(), text1.getHeight());
        this.setName(path);
    }

    public void addAtCenter(Stage stage, float hOffset, float vOffset){
        renderHelper.setPositionCenter(stage,this,hOffset,vOffset);
        stage.addActor(this);
    }


    public void addAt(Stage stage, float hOrigin, float vOrigin)
    {
        this.setPosition(hOrigin, vOrigin);
        stage.addActor(this);
    }
}
