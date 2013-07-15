package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.scenes.scene2d.Touchable;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.RollingCat;

public class GroundBlock extends Entity {

	/**
	 * le bloc qui constitue le sol
	 * @param x
	 * @param y
	 */
	public GroundBlock(float x, float y) {
		super(x, y, GameConstants.TEXTURE_GROUNDBLOCK+RollingCat.skin);
		this.setTouchable(Touchable.disabled);
	}
	
	
}
