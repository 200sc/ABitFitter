package com.gamifyGame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.HashMap;

/**
 * Created by Stephen on 11/11/2014.
 */
public class ChangingImage extends Image {

        TextureRegionDrawable textured1, textured2;
        String name1,name2;
        private HashMap<String,String> Strings;
        private HashMap<String,Integer> Integers;

        public ChangingImage(String file, String file2, Stage stage, int hOrigin, int vOrigin){
            name1 = file;
            name2 = file2;
            Texture text1 = renderHelper.getRenderHelper().textureHash.get(file);
            textured1 = renderHelper.getRenderHelper().getTextureRegionDrawable(file);
            textured2 = renderHelper.getRenderHelper().getTextureRegionDrawable(file2);
            this.setDrawable(textured1);
            this.setPosition(hOrigin, vOrigin);
            this.setSize(text1.getWidth(), text1.getHeight());
            this.setName(file);
            stage.addActor(this);
            Strings = new HashMap<String, String>();
            Integers = new HashMap<String, Integer>();
        }

        public void swapTexture(){
            if (this.getName().equals(name1)){
                this.setName(name2);
                this.setDrawable(textured2);
            }
            else {
                this.setName(name1);
                this.setDrawable(textured1);
            }
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

        public void nameChange(String newName){
            if (this.getName().equals(name1)){
                this.name1 = newName;
                this.textured1 = renderHelper.getRenderHelper().getTextureRegionDrawable(newName);
                this.setDrawable(textured1);
            }
            else {
                this.name2 = newName;
                this.textured2 = renderHelper.getRenderHelper().getTextureRegionDrawable(newName);
                this.setDrawable(textured2);
            }
        }

}
