package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.lirmm.smile.rollingcat.GameConstants;

public class Wasp extends Entity {
	
	/**
	 * Touchable enabled
	 * bloque le passage du chat
	 * @param x
	 * @param y
	 */
	public Wasp(float x, float y) {
		super(x, y, GameConstants.TEXTURE_WASP);
	}
	
	@Override
	public Action getAction(){
		//return Actions.moveTo(this.getX(), 16*GameConstants.BLOCK_HEIGHT, 8, Interpolation.circleOut);
		return Actions.visible(false);
	}
	
	@Override
	public Action getActionOnCat(){
		return Actions.moveTo(this.getX(), this.getY() - GameConstants.BLOCK_HEIGHT*2, 0.25f);
	}
	
}
