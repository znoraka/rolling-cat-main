package fr.lirmm.smile.rollingcat.model.game;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import fr.lirmm.smile.rollingcat.GameConstants;

public class Bone extends Entity{

	/**
	 * Touchable disabled
	 * Bone modifie le comportement du Dog au moment du contact entre le Dog et le Cat
	 * @param x
	 * @param y
	 */
	public Bone(float x, float y) {
		super(x, y, GameConstants.TEXTURE_BONE);
		this.setTouchable(Touchable.disabled);
	}
	
	@Override
	public Action getAction(){
		return new Action() {
			
			@Override
			public boolean act(float delta) {
				setVisible(false);
				return true;
			}
		};
	}

}
