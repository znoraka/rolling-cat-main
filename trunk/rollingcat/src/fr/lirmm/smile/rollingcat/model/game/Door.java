package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;

public class Door extends Entity {
	
	public static int RIGHT = 1;
	public static int LEFT = 2;
	
	private int type;
	
	private float nextX, nextY;
	
	public Door(float x, float y, int type, float nextX, float nextY) {
		super(x, y, GameConstants.TEXTURE_BOUEE+"_"+((type == LEFT)?"blue":"red"));
		this.type = type;
		this.nextX = nextX;
		this.nextY = nextY + 2;
		this.setZIndex(0);
		this.setTouchable(Touchable.disabled);
	}
	
	public int getType(){
		return this.type;
	}
	
	public float getNextX(){
		Gdx.app.log(RollingCat.LOG, ""+nextX);
		return nextX;
	}
	
	public float getNextY(){
		Gdx.app.log(RollingCat.LOG, ""+nextY);
		return nextY;
	}

	
}
