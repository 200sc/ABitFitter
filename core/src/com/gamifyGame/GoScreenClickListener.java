package com.gamifyGame;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Andrew on 2/20/2015.
 */
public class GoScreenClickListener extends ClickListener
{
    private GamifyScreen screen;
    private gamifyGame game;

    public GoScreenClickListener(GamifyScreen screen, gamifyGame game)
    {
        this.screen=screen;
        this.game=game;
    }

    @Override

    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
    {
        game.setScreen(screen);
        return true;
    }
}
