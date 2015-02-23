package com.gamifyGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Stephen on 2/22/2015.
 */
public class LoadingImage extends AnimatingImage {

    public LoadingImage(Stage stage, int hOrigin, int vOrigin, final gamifyGame game, final String lastImage){
        String[] loadings = {"loading0.png","loading1.png","loading2.png","loading3.png"};
        names = new ArrayList<String>();
        textures = new ArrayList<TextureRegionDrawable>();
        for(int i = 0; i < loadings.length; i++){
            names.add(loadings[i]);
            textures.add(renderHelper.getRenderHelper().getTextureRegionDrawable(loadings[i]));
        }
        Texture text1 = renderHelper.getRenderHelper().textureHash.get(names.get(0));
        this.setDrawable(textures.get(0));
        this.setPosition(hOrigin, vOrigin);
        this.setSize(text1.getWidth(), text1.getHeight());
        this.setName(names.get(0));
        stage.addActor(this);

        imageCount = loadings.length;
        curFile = 0;

        this.addAction(new Action(){
            public boolean act(float delta) {
                if (game.getLoadingFlag()) {
                    curFile++;
                    setName(names.get(curFile));
                    setDrawable(textures.get(curFile));
                    return false;
                }
                else {
                    if (getName() != lastImage){
                        remove();
                    }
                }
                return false;
            }
        });
    }
}
