package com.gamifyGame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Stephen on 11/11/2014.
 */
public class AnimatingImage extends Image {


    ArrayList<String> names;
    ArrayList<TextureRegionDrawable> textures;
    int imageCount, curFile;
    private HashMap<String,String> Strings;
    private HashMap<String,Integer> Integers;

    public AnimatingImage(){

    }

    public AnimatingImage(String[] files, Stage stage, int hOrigin, int vOrigin){
        int i = 0;
        for(String file: files){
            names.add(file);
            textures.add(renderHelper.getRenderHelper().getTextureRegionDrawable(file));
            i++;
        }
        Texture text1 = renderHelper.getRenderHelper().textureHash.get(names.get(i));
        this.setDrawable(textures.get(0));
        this.setPosition(hOrigin, vOrigin);
        this.setSize(text1.getWidth(), text1.getHeight());
        this.setName(names.get(i));
        stage.addActor(this);

        imageCount = files.length;
        curFile = 0;

        Strings = new HashMap<String, String>();
        Integers = new HashMap<String, Integer>();

        this.addAction(new Action(){
                public boolean act(float delta) {
                    curFile++;
                    setName(names.get(curFile));
                    setDrawable(textures.get(curFile));
                    return false;
                }
        });
   }



    public void putExtra(String key, String val){
        Strings.put(key,val);
    }

    public void putExtra(String key, int val){
        Integers.put(key,val);
    }

    public String getString(String key){
        return Strings.get(key);
    }

    public Integer getInt(String key){
        return Integers.get(key);
    }


}
