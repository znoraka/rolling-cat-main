package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.lirmm.smile.rollingcat.GameConstants;

public class Dog extends Entity {
	
	/**
	 * Touchable enabled
	 * le chien arrete le chat lorsqu'ils sont en contact
	 * @param x
	 * @param y
	 */
	public Dog(float x, float y){
		super(x, y, GameConstants.TEXTURE_DOG);
	}
	
	@Override
	public Action getAction(){
		return Actions.visible(false);
	}
	
	@Override
	public Action getActionOnCat(){
		return Actions.moveTo(this.getX() - GameConstants.BLOCK_WIDTH, this.getY());
				
	}
	
	
}
