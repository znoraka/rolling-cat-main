package fr.lirmm.smile.rollingcat.model.game;

import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
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
	
	public Coin(float x, float y, String type) {
		super(x, y, GameConstants.TEXTURE_EMPTY);
		this.setTouchable(Touchable.disabled);
		this.type = type;
        this.anim = new Animation(0.25f, GdxRessourcesGetter.getRegions("coin"+type));
        pickedUp = false;

	}
	
	public String getType(){
		return this.type;
	}
	
	public boolean pickedUp(){
		return pickedUp;
	}
	
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
		
		this.addAction(Actions.moveTo(x, y, (float) (( Math.sqrt(Math.pow(this.getX() - x, 2) + Math.pow(this.getY() - y, 2)) * GameConstants.SPEED* 0.01f)), Interpolation.pow2Out));
		
	}
}
