package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.math.Vector2;

import fr.lirmm.smile.rollingcat.GameConstants;

public class GroundBlock extends Block {

	/**
	 * le bloc qui constitue le sol
	 * @param x
	 * @param y
	 */
	public GroundBlock(float x, float y) {
		super(x, y, new Vector2(1, 0), GameConstants.TEXTURE_GROUNDBLOCK);
	}
	
	
}
