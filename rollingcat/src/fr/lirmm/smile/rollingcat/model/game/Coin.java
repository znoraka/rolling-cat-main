package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.scenes.scene2d.Touchable;

import fr.lirmm.smile.rollingcat.GameConstants;

public class Coin extends Entity{

	/**
	 * Touchable disabled
	 * indicateur de score
	 * @param x
	 * @param y
	 */
	public Coin(float x, float y) {
		super(x, y, GameConstants.TEXTURE_COIN);
		this.setTouchable(Touchable.disabled);
	}
}
