package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fr.lirmm.smile.rollingcat.GameConstants;

public class Block extends Entity {
	
	protected Vector2 velocityModifier;
	
	/**
	 * Touchable disabled
	 * les Block's sont des entity qui modifient le déplacement du chat lorsqu'ils sont en contact
	 * @param x
	 * @param y
	 * @param vector le vecteur vitesse que va prendre le chat lorsqu'il est en contact avec le bloc
	 * @param name le nom de l'entity pour le texture atlas
	 */
	public Block (float x, float y, Vector2 vector, String name)
	{
		super(x, y, name);
		this.velocityModifier = vector;
		this.setTouchable(Touchable.disabled);
		this.bounds.height = 1;
		this.bounds.y += GameConstants.BLOCK_HEIGHT;
	}
	
	/**
	 * 
	 * @return le vecteur vitesse à donner au chat du bloc
	 */
	public Vector2 getVelocityModifier(){
		return this.velocityModifier;
	}
	
	@Override
	public Action getActionOnCat(){
		return Actions.moveBy(velocityModifier.x * GameConstants.BLOCK_WIDTH, velocityModifier.y * GameConstants.BLOCK_HEIGHT, GameConstants.SPEED);
	}
	
}
