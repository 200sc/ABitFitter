package com.gamifyGame;

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
        if(selectedBuyable !=null)
            movingTextDisplayBox.addText(new Point(0, 30), selectedBuyable.toString());
        movingTextDisplayBox.addText(new Point(0, -15),"Vitality: "+game.getVitality( ));
    }


}
