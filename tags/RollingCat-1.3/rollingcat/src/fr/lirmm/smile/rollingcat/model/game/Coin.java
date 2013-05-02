package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.screen.GameScreen;
import fr.lirmm.smile.rollingcat.utils.GdxRessourcesGetter;

public class Coin extends Entity{

	public static final String BRONZE = "bronze";
	public static final String SILVER = "silver";
	public static final String GOLD = "gold";
	
	private String type;
	private boolean pickedUp;
	boolean b;
	
	public Coin(float x, float y, String type) {
		super(x, y, GameConstants.TEXTURE_EMPTY);
		this.setTouchable(Touchable.disabled);
		this.type = type;
        this.anim = new Animation(0.25f, GdxRessourcesGetter.getRegions("coin"+type));
        pickedUp = false;
        this.setWidth(GameConstants.BLOCK_WIDTH*0.7f);
        this.setHeight(GameConstants.BLOCK_WIDTH*0.7f);
        this.setX(this.getX() + getWidth()/4);
        this.setY(this.getY() + getHeight()/4);
        b = false;
	}
	
	/**
	 * 
	 * @return the coin's type
	 */
	public String getType(){
		return this.type;
	}
	
	/**
	 * 
	 * @return true if the coin has been picked up
	 */
	public boolean pickedUp(){
		return pickedUp;
	}
	
	/**
	 * picks up a coin and sends it with his friends in the coin counting area
	 */
	public void pickUp(){
		pickedUp = true;
		float x = this.getStage().getCamera().position.x - GameConstants.DISPLAY_WIDTH * 0.5f;
		float y;
		
		if(type.equals(BRONZE)){
			x += GameScreen.bronze.x;
			y = GameScreen.bronze.y;
		}
		
		else if(type.equals(SILVER)){
			x += GameScreen.silver.x;
			y = GameScreen.silver.y;
		}
		
		else{
			x += GameScreen.gold.x;
			y = GameScreen.gold.y;
		}
		
		this.addAction(Actions.sequence(
				Actions.moveTo(x, y, (float) (( Math.sqrt(Math.pow(this.getX() - x, 2) + Math.pow(this.getY() - y, 2)) * GameConstants.SPEED* 0.01f)), Interpolation.pow2Out),
				new Action() {
					
					@Override
					public boolean act(float delta) {
						setVisible(false);
						return true;
					}
				})
				);
		
	}
	
//	@Override
//	public void draw(SpriteBatch batch, float deltaParent)
//	{		
////		super.draw(batch, deltaParent);
////		if(this.getActions().size == 0)
////		{
////			if(!b)
////			{
////				this.addAction(Actions.moveBy(0, getHeight()/2, 1, Interpolation.fade));			
////				b = true;
////			}
////			else
////			{
////				b = false;
////				this.addAction(Actions.moveBy(0, -getHeight()/2, 1, Interpolation.fade));
////			}
////		}
//	}
}