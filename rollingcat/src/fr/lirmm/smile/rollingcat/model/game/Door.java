package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;

public class Door extends Entity {

	public static int RIGHT = 1;
	public static int LEFT = 2;

	private int type;

	private float nextX, nextY;
	
	private Door door;

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

	@Override
	public void draw(SpriteBatch batch, float deltaParent){
		if(type == LEFT)
			super.draw(batch, deltaParent);
	}

	public Door getNextDoor(int decalage){
		door = null;
		for (int i = 0; i < this.getStage().getActors().size & door == null; i++) {
			if(this.getStage().getActors().get(i) instanceof Door && this.getStage().getActors().get(i).getX() == this.getX() && this.getStage().getActors().get(i).getY() == (this.getY() + decalage))
			{
				door = (Door) this.getStage().getActors().get(i);
			}
		}
		return door;
	}


}
